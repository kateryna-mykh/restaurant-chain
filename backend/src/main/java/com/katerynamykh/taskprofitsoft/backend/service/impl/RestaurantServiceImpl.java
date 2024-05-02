package com.katerynamykh.taskprofitsoft.backend.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.FilteredRestaurantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.SearchRestaurantDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.UploadResultDto;
import com.katerynamykh.taskprofitsoft.backend.exception.EntityNotFoundException;
import com.katerynamykh.taskprofitsoft.backend.exception.FileUploadException;
import com.katerynamykh.taskprofitsoft.backend.exception.SaveDuplicateException;
import com.katerynamykh.taskprofitsoft.backend.mapper.RestaurantMapper;
import com.katerynamykh.taskprofitsoft.backend.model.Restaurant;
import com.katerynamykh.taskprofitsoft.backend.model.RestaurantChain;
import com.katerynamykh.taskprofitsoft.backend.repository.RestaurantChainRepository;
import com.katerynamykh.taskprofitsoft.backend.repository.RestaurantRepository;
import com.katerynamykh.taskprofitsoft.backend.repository.spec.RestaurantSpec;
import com.katerynamykh.taskprofitsoft.backend.service.RestaurantService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final String CSV_DELIMITER = ",";
    private final RestaurantRepository restaurantRepository;
    private final RestaurantChainRepository chainRepository;
    private final RestaurantMapper mapper;
    private static final ObjectMapper jsonMapper;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        jsonMapper = mapper;
    }

    @Transactional
    @Override
    public RestaurantResponseDto save(CreatedRestaurantRequestDto restaurantDto) {
        if (restaurantRepository.findByLocationAddressIgnoreCase(restaurantDto.locationAddress())
                .isPresent()) {
            throw new SaveDuplicateException(
                    "Faild to save duplicate restaurant " + restaurantDto.locationAddress());
        }
        RestaurantChain chain = chainRepository.findById(restaurantDto.restaurantChainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find chain by id: " + restaurantDto.restaurantChainId()));
        return mapper
                .toDto(restaurantRepository.save(mapper.toModel(restaurantDto, chain.getId())));
    }

    @Override
    public DetaildRestaurantResponseDto findById(Long id) {
        Restaurant restaurantById = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find restaurant by id " + id));
        return mapper.toDtoDetaild(restaurantById);
    }

    @Transactional
    @Override
    public RestaurantResponseDto update(Long id, CreatedRestaurantRequestDto restaurantDto) {
        Optional<Restaurant> restaurantByLocationAddress = restaurantRepository
                .findByLocationAddressIgnoreCase(restaurantDto.locationAddress());
        if (restaurantByLocationAddress.isPresent()
                && !id.equals(restaurantByLocationAddress.get().getId())) {
            throw new SaveDuplicateException(
                    "Faild to save duplicate restaurant " + restaurantDto.locationAddress());
        }
        RestaurantChain chain = chainRepository.findById(restaurantDto.restaurantChainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find chain by id: " + restaurantDto.restaurantChainId()));
        Restaurant model = mapper.toModel(restaurantDto, chain.getId());
        model.setId(id);
        return mapper.toDto(restaurantRepository.save(model));
    }

    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public FilteredRestaurantsDto search(SearchRestaurantDto searchParams) {
        PageRequest pageble = buildPageble(searchParams.page(), searchParams.size());
        Page<Restaurant> filteredRestaurants;
        if (searchParams.address() == null && searchParams.chainId() == null) {
            filteredRestaurants = restaurantRepository.findAll(pageble);
        } else {
            filteredRestaurants = restaurantRepository
                    .findAll(RestaurantSpec.filterBy(searchParams), pageble);
        }
        return new FilteredRestaurantsDto(
                filteredRestaurants.stream().map(mapper::toShortDto).toList(),
                filteredRestaurants.getTotalPages());
    }

    @Transactional
    @Override
    public UploadResultDto uploadFromFile(MultipartFile multipart) {
        try {
            byte[] fileBytes = multipart.getBytes();
            List<CreatedRestaurantRequestDto> parsedDtos = jsonMapper.readValue(fileBytes,
                    new TypeReference<List<CreatedRestaurantRequestDto>>() {
                    });
            long allNumber = parsedDtos.size();
            List<Long> allChainIds = chainRepository.findAll().stream().map(ch -> ch.getId())
                    .toList();
            List<Restaurant> restaurants = parsedDtos.stream()
                    .filter(r -> restaurantRepository
                            .findByLocationAddressIgnoreCase(r.locationAddress()).isEmpty())
                    .filter(r -> allChainIds.contains(r.restaurantChainId()))
                    .map(r -> mapper.toModel(r, r.restaurantChainId()))
                    .map(restaurantRepository::save).toList();
            long savedNumber = restaurants.size();
            return new UploadResultDto(savedNumber, allNumber - savedNumber, allNumber);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @Override
    public void generateReport(HttpServletResponse response, SearchRestaurantDto searchParams) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=restaurants_report.csv");
        List<Restaurant> restaurants;
        if (searchParams.address() == null && searchParams.chainId() == null) {
            restaurants = restaurantRepository.findAll();
        } else {
            restaurants = restaurantRepository.findAll(RestaurantSpec.filterBy(searchParams));
        }
        String reportHeader = String.format("%s,%s,%s,%s,%s", "id", "chainName", "locationAddress",
                "seetsCapacity", "menuItems");
        StringBuilder sb = new StringBuilder().append(reportHeader);
        List<RestaurantShortResponseDto> shortDtos = restaurants.stream().map(mapper::toShortDto)
                .toList();
        try {
            for (RestaurantShortResponseDto dto : shortDtos) {
                sb.append(System.lineSeparator()).append(dto.id()).append(CSV_DELIMITER)
                        .append(dto.chainName()).append(CSV_DELIMITER).append(dto.locationAddress())
                        .append(CSV_DELIMITER).append(dto.seetsCapacity()).append(CSV_DELIMITER)
                        .append(dto.menuItems());
            }
            response.getOutputStream().write(sb.toString().getBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Error generating csv file", e);
        }
    }

    private PageRequest buildPageble(Integer page, Integer size) {
        int p = (page == null || page < 0) ? DEFAULT_PAGE_NUMBER : page;
        int s = (size == null || size < 1) ? DEFAULT_PAGE_SIZE : size;
        return PageRequest.of(p, s);
    }
}
