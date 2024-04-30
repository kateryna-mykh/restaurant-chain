package com.katerynamykh.taskprofitsoft.backend.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.UploadResult;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.FilteredRestorantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestoarntShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.SearchRestorantDto;
import com.katerynamykh.taskprofitsoft.backend.exception.EntityNotFoundException;
import com.katerynamykh.taskprofitsoft.backend.exception.FileUploadException;
import com.katerynamykh.taskprofitsoft.backend.exception.SaveDuplicateException;
import com.katerynamykh.taskprofitsoft.backend.mapper.RestorantMapper;
import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import com.katerynamykh.taskprofitsoft.backend.repository.RestorantChainRepository;
import com.katerynamykh.taskprofitsoft.backend.repository.RestorantRepository;
import com.katerynamykh.taskprofitsoft.backend.service.RestorantService;
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
public class RestorantServiceImpl implements RestorantService {
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final String CSV_DELIMITER = ",";
    private final RestorantRepository restorantRepository;
    private final RestorantChainRepository chainRepository;
    private final RestorantMapper mapper;
    private static final ObjectMapper jsonMapper;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        jsonMapper = mapper;
    }

    @Transactional
    @Override
    public RestorantResponseDto save(CreatedRestorantRequestDto restorantDto) {
        if (restorantRepository.findByLocationAddressIgnoreCase(restorantDto.locationAddress())
                .isPresent()) {
            throw new SaveDuplicateException(
                    "Faild to save duplicate restorant " + restorantDto.locationAddress());
        }
        RestorantChain chain = chainRepository.findById(restorantDto.restorantChainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find chain by id: " + restorantDto.restorantChainId()));
        return mapper.toDto(restorantRepository.save(mapper.toModel(restorantDto, chain.getId())));
    }

    @Override
    public DetaildRestorantResponseDto findById(Long id) {
        Restorant restorantById = restorantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find restorant by id " + id));
        return mapper.toDtoDetaild(restorantById);
    }

    @Transactional
    @Override
    public RestorantResponseDto update(Long id, CreatedRestorantRequestDto restorantDto) {
        Optional<Restorant> restorantByLocationAddress = restorantRepository
                .findByLocationAddressIgnoreCase(restorantDto.locationAddress());
        if (restorantByLocationAddress.isPresent()
                && id.equals(restorantByLocationAddress.get().getId())) {
            throw new SaveDuplicateException(
                    "Faild to save duplicate restorant " + restorantDto.locationAddress());
        }
        RestorantChain chain = chainRepository.findById(restorantDto.restorantChainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find chain by id: " + restorantDto.restorantChainId()));
        return mapper.toDto(restorantRepository.save(mapper.toModel(restorantDto, chain.getId())));
    }

    @Override
    public void deleteById(Long id) {
        restorantRepository.deleteById(id);
    }

    @Override
    public FilteredRestorantsDto search(SearchRestorantDto searchParams) {
        PageRequest pageble = buildPageble(searchParams.page(), searchParams.size());
        Page<Restorant> allByParams = restorantRepository
                .findAllByParamsIgnoreCase(searchParams.address(), searchParams.chainId(), pageble);
        return new FilteredRestorantsDto(allByParams.stream().map(mapper::toShortDto).toList(),
                allByParams.getTotalPages());
    }

    @Transactional
    @Override
    public UploadResult uploadFromFile(MultipartFile multipart) {
        try {
            byte[] fileBytes = multipart.getBytes();
            List<CreatedRestorantRequestDto> parsedDtos = jsonMapper.readValue(fileBytes,
                    new TypeReference<List<CreatedRestorantRequestDto>>() {
                    });
            long allNumber = parsedDtos.size();
            List<Long> allChainIds = chainRepository.findAll().stream().map(ch -> ch.getId())
                    .toList();
            List<Restorant> restorants = parsedDtos.stream()
                    .filter(r -> restorantRepository
                            .findByLocationAddressIgnoreCase(r.locationAddress()).isEmpty())
                    .filter(r -> allChainIds.contains(r.restorantChainId()))
                    .map(r -> mapper.toModel(r, r.restorantChainId()))
                    .map(restorantRepository::save).toList();
            long savedNumber = restorants.size();
            return new UploadResult(savedNumber, allNumber - savedNumber, allNumber);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @Override
    public void generateReport(HttpServletResponse response, SearchRestorantDto searchParams) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=restorants_report.csv");

        List<Restorant> restorants = restorantRepository
                .findAllByParamsIgnoreCase(searchParams.address(), searchParams.chainId());
        String reportHeader = String.format("%s,%s,%s,%s,%s", "id", "chainName", "location",
                "seetsCapacity", "menuItems");
        StringBuilder sb = new StringBuilder().append(reportHeader);
        List<RestoarntShortResponseDto> shortDtos = restorants.stream().map(mapper::toShortDto)
                .toList();
        try {
            for (RestoarntShortResponseDto dto : shortDtos) {
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
