package com.katerynamykh.taskprofitsoft.backend.service.impl;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import com.katerynamykh.taskprofitsoft.backend.exception.SaveDuplicateException;
import com.katerynamykh.taskprofitsoft.backend.mapper.RestorantChainMapper;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import com.katerynamykh.taskprofitsoft.backend.repository.RestorantChainRepository;
import com.katerynamykh.taskprofitsoft.backend.service.RestorantChainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestorantChainServiceImpl implements RestorantChainService {
    private final RestorantChainRepository chainRepository;
    private final RestorantChainMapper mapper;

    @Transactional
    @Override
    public ChainResponseDto save(CreateChainRequestDto chainDto) {
        if (chainRepository.findByNameIgnoreCase(chainDto.name()).isPresent()) {
            throw new SaveDuplicateException("Faild to save duplicate chain " + chainDto.name());
        }
        return mapper.toDto(chainRepository.save(mapper.toModel(chainDto)));
    }

    @Override
    public List<ChainWithLocationsDto> findAll(Pageable pageable) {
        return chainRepository.findAll(pageable).stream()
                .map(mapper::toDtoWithLocations)
                .toList();
    }

    @Transactional
    @Override
    public ChainResponseDto update(Long id, CreateChainRequestDto chainDto) {
        if (chainRepository.findByNameIgnoreCase(chainDto.name()).isPresent()) {
            throw new SaveDuplicateException("Faild to save duplicate chain " + chainDto.name());
        }
        RestorantChain chain = mapper.toModel(chainDto);
        chain.setId(id);
        return mapper.toDto(chainRepository.save(chain));
    }

    @Override
    public void deleteById(Long id) {
        chainRepository.deleteById(id);
    }
}
