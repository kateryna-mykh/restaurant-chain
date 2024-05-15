package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import java.util.List;

public record RestaurantShortResponseDto(
        Long id,
        String chainName,
        String locationAddress,
        Integer seatsCapacity,
        List<String> menuItems) {
}
