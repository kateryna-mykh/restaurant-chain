package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import java.util.List;

public record RestoarntShortResponseDto(
        Long id,
        String locationAddress, 
        Integer seetsCapacity, 
        String chainName,
        List<String> menuItems) {
}
