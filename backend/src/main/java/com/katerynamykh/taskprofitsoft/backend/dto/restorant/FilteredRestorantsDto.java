package com.katerynamykh.taskprofitsoft.backend.dto.restorant;

import java.util.List;

public record FilteredRestorantsDto(
       List<RestoarntShortResponseDto> restorants,
       Integer totalPages) {

}
