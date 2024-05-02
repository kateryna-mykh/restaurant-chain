package com.katerynamykh.taskprofitsoft.backend.service;

import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.FilteredRestaurantsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.SearchRestaurantDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.UploadResultDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {
    RestaurantResponseDto save(CreatedRestaurantRequestDto restaurantDto);

    DetaildRestaurantResponseDto findById(Long id);

    RestaurantResponseDto update(Long id, CreatedRestaurantRequestDto restaurantDto);

    void deleteById(Long id);
    
    FilteredRestaurantsDto search(SearchRestaurantDto searchParams);
    
    UploadResultDto uploadFromFile(MultipartFile multipart);

    void generateReport(HttpServletResponse response, SearchRestaurantDto searchParams);
}
