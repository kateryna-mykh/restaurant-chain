package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import java.util.List;

public record RestorantResponseDto(
        Long id,
        String locationAddress, 
        String manager,
        Integer seetsCapacity, 
        Integer employeesNumber,
        Long restorantChainId, 
        List<String> menuItems) {

}
