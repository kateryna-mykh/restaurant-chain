package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreatedRestorantRequestDto(
        @NotBlank String locationAddress, 
        @NotBlank String manager,
        @NotNull @Positive Integer seetsCapacity, 
        @NotNull @Positive Integer employeesNumber,
        @NotNull Long restorantChainId, 
        List<String> menuItems) {
}
