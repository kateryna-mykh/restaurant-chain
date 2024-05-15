package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainShortResponseDto;
import java.util.List;

public record DetaildRestaurantResponseDto(
    Long id,
    String locationAddress, 
    String manager,
    Integer seatsCapacity, 
    Integer employeesNumber,
    ChainShortResponseDto chainShortInfo, 
    List<String> menuItems) {
}
