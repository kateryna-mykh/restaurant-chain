package com.katerynamykh.taskprofitsoft.backend.dto.chain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateChainRequestDto(
        @NotBlank String name, 
        @NotBlank String cuisine,
        @NotNull @Min(0) BigDecimal annualRevenue) {

}
