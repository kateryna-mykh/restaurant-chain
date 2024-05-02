package com.katerynamykh.taskprofitsoft.backend.mapper;

import com.katerynamykh.taskprofitsoft.backend.config.MapperConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import com.katerynamykh.taskprofitsoft.backend.model.RestaurantChain;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface RestaurantChainMapper {

    ChainResponseDto toDto(RestaurantChain chain);
    
    @Mapping(target = "mainChainInfo", source = "chain")
    @Mapping(target = "locationAddress", ignore = true)
    ChainWithLocationsDto toDtoWithLocations(RestaurantChain chain);
    
    ChainShortResponseDto toDtoShortInfo(RestaurantChain chain);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branches", ignore = true)
    RestaurantChain toModel(CreateChainRequestDto chainDto);
    
    @AfterMapping
    default void setBranshesLocations(@MappingTarget ChainWithLocationsDto chainDto, RestaurantChain chain) {
        chainDto.setLocationAddress(
                chain.getBranches()
                .stream()
                .map(r -> r.getLocationAddress())
                .toList());
    }
}
