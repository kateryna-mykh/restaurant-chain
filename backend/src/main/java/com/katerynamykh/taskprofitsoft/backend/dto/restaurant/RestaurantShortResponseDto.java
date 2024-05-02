package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import java.util.List;

public record RestaurantShortResponseDto(
        Long id,
        String locationAddress, 
        Integer seetsCapacity, 
        String chainName,
        List<String> menuItems) {
}
