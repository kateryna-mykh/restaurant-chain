package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import java.util.List;

public record RestaurantShortResponseDto(
        Long id,
        String chainName,
        String locationAddress,
        Integer seetsCapacity,
        List<String> menuItems) {
}
