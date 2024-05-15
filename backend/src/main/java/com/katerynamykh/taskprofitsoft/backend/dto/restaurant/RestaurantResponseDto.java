package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import java.util.List;

public record RestaurantResponseDto(
        Long id,
        String locationAddress, 
        String manager,
        Integer seatsCapacity, 
        Integer employeesNumber,
        Long restaurantChainId, 
        List<String> menuItems) {

}
