package com.katerynamykh.taskprofitsoft.backend.mapper;

import com.katerynamykh.taskprofitsoft.backend.config.MapperConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.CreatedRestaurantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.DetaildRestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restaurant.RestaurantShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RestaurantMapper {
    
    @Mapping(target = "restaurantChainId", source = "restaurantChain.id")
    RestaurantResponseDto toDto(Restaurant restaurant);
    
    @Mapping(target = "chainShortInfo", source = "restaurantChain")
    DetaildRestaurantResponseDto toDtoDetaild(Restaurant restaurant);
    
    @Mapping(target = "chainName", source = "restaurantChain.name")
    RestaurantShortResponseDto toShortDto(Restaurant restaurant);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantChain.id", source = "chainId")
    Restaurant toModel(CreatedRestaurantRequestDto restaurantDto, Long chainId);
    
}
