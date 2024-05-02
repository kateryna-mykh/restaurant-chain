package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

public record SearchRestaurantDto(
        String address,
        Long chainId,
        Integer page,
        Integer size) {
}
