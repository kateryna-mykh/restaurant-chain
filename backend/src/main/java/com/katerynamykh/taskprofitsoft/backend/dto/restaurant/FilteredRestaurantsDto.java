package com.katerynamykh.taskprofitsoft.backend.dto.restaurant;

import java.util.List;

public record FilteredRestaurantsDto(
       List<RestaurantShortResponseDto> restaurants,
       Integer totalPages) {

}
