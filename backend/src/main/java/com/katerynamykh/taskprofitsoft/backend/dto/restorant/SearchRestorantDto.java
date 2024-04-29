package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import java.util.List;

public record SearchRestorantDto(
        String address,
        Long chainId,
        List<String> menuItems,
        Integer page,
        Integer size) {
}
