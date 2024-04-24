package com.katerynamykh.taskprofitsoft.backend.service;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import java.util.List;

public interface RestorantChainService {
    ChainResponseDto save(CreateChainRequestDto chainDto);

    List<ChainWithLocationsDto> findAll();

    ChainResponseDto update(Long id, CreateChainRequestDto chainDto);

    void deleteById(Long id);
}
