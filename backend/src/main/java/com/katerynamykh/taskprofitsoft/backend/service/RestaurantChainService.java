package com.katerynamykh.taskprofitsoft.backend.service;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RestaurantChainService {
    ChainResponseDto save(CreateChainRequestDto chainDto);

    List<ChainWithLocationsDto> findAll(Pageable pageable);

    ChainResponseDto update(Long id, CreateChainRequestDto chainDto);

    void deleteById(Long id);
}
