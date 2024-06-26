package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreatedRestaurantRequestDto(
        @NotBlank String locationAddress, 
        @NotBlank String manager,
        @NotNull @Positive Integer seatsCapacity, 
        @NotNull @Positive Integer employeesNumber,
        @NotNull Long restaurantChainId, 
        List<String> menuItems) {
}
