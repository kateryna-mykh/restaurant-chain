package com.katerynamykh.taskprofitsoft.backend.dto.chain;

import java.math.BigDecimal;
import java.util.List;

public record ChainResponseDto(
        Long id, 
        String name, 
        String cuisine, 
        BigDecimal annualRevenue,
        List<String> locationAddress) {
}
