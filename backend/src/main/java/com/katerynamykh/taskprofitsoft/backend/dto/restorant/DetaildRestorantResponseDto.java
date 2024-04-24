package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainShortResponseDto;
import java.util.List;

public record DetaildRestorantResponseDto(
    Long id,
    String locationAddress, 
    String manager,
    Integer seetsCapacity, 
    Integer employeesNumber,
    ChainShortResponseDto chainShortInfo, 
    List<String> menuItems) {
}
