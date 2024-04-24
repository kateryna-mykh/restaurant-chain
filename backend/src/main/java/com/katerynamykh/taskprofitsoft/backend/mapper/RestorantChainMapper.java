package com.katerynamykh.taskprofitsoft.backend.mapper;

import com.katerynamykh.taskprofitsoft.backend.config.MapperConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.ChainWithLocationsDto;
import com.katerynamykh.taskprofitsoft.backend.dto.chain.CreateChainRequestDto;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface RestorantChainMapper {

    ChainResponseDto toDto(RestorantChain chain);
    
    ChainWithLocationsDto toDtoWithLocations(RestorantChain chain);
    
    ChainShortResponseDto toDtoShortInfo(RestorantChain chain);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branches", ignore = true)
    RestorantChain toModel(CreateChainRequestDto chainDto);
    
    @AfterMapping
    default void setBranshesLocations(@MappingTarget ChainWithLocationsDto chainDto, RestorantChain chain) {
        chainDto.setLocationAddress(
                chain.getBranches()
                .stream()
                .map(r -> r.getLocationAddress())
                .toList());
    }
}
