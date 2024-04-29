package com.katerynamykh.taskprofitsoft.backend.service.impl;

import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.exception.EntityNotFoundException;
import com.katerynamykh.taskprofitsoft.backend.exception.SaveDuplicateException;
import com.katerynamykh.taskprofitsoft.backend.mapper.RestorantMapper;
import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import com.katerynamykh.taskprofitsoft.backend.repository.RestorantChainRepository;
import com.katerynamykh.taskprofitsoft.backend.repository.RestorantRepository;
import com.katerynamykh.taskprofitsoft.backend.service.RestorantService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestorantServiceImpl implements RestorantService {
    private final RestorantRepository restorantRepository;
    private final RestorantChainRepository chainRepository;
    private final RestorantMapper mapper;

    @Transactional
    @Override
    public RestorantResponseDto save(CreatedRestorantRequestDto restorantDto) {
        if (restorantRepository.findByLocationAddressIgnoreCase(restorantDto.locationAddress()).isPresent()) {
            throw new SaveDuplicateException(
                    "Faild to save duplicate restorant " + restorantDto.locationAddress());
        }
        RestorantChain chain = chainRepository.findById(restorantDto.restorantChainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find chain by id: " + restorantDto.restorantChainId()));
        return mapper.toDto(restorantRepository.save(mapper.toModel(restorantDto, chain)));
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
        return mapper.toDto(restorantRepository.save(mapper.toModel(restorantDto, chain)));
    }

    @Override
    public void deleteById(Long id) {
        restorantRepository.deleteById(id);
    }
}
