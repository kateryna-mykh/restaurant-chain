package com.katerynamykh.taskprofitsoft.backend.service;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.UploadResult;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.FilteredRestorantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.SearchRestorantDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RestorantService {
    RestorantResponseDto save(CreatedRestorantRequestDto restorantDto);

    DetaildRestorantResponseDto findById(Long id);

    RestorantResponseDto update(Long id, CreatedRestorantRequestDto restorantDto);

    void deleteById(Long id);
    
    FilteredRestorantsDto search(SearchRestorantDto searchParams);
    
    UploadResult uploadFromFile(MultipartFile multipart);

    void generateReport(HttpServletResponse response, SearchRestorantDto searchParams);
}
