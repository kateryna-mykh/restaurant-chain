package com.katerynamykh.taskprofitsoft.backend.service;

import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;

public interface RestorantService {
    RestorantResponseDto save(CreatedRestorantRequestDto restorantDto);

    DetaildRestorantResponseDto findById(Long id);

    RestorantResponseDto update(Long id, CreatedRestorantRequestDto restorantDto);

    void deleteById(Long id);
}
