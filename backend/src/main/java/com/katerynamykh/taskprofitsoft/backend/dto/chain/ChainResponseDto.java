package com.katerynamykh.taskprofitsoft.backend.dto.chain;

import java.math.BigDecimal;

public record ChainResponseDto(
        Long id, 
        String name, 
        String cuisine, 
        BigDecimal annualRevenue) {
}
