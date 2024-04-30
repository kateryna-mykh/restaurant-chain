package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

public record SearchRestorantDto(
        String address,
        Long chainId,
        Integer page,
        Integer size) {
}
