package com.katerynamykh.taskprofitsoft.backend.mapper;

import com.katerynamykh.taskprofitsoft.backend.config.MapperConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.CreatedRestorantRequestDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.DetaildRestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestoarntShortResponseDto;
import com.katerynamykh.taskprofitsoft.backend.dto.restorant.RestorantResponseDto;
import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RestorantMapper {
    
    @Mapping(target = "restorantChainId", source = "restorantChain.id")
    RestorantResponseDto toDto(Restorant restorant);
    
    @Mapping(target = "chainShortInfo", source = "restorantChain")
    DetaildRestorantResponseDto toDtoDetaild(Restorant restorant);
    
    @Mapping(target = "chainName", source = "restorantChain.name")
    RestoarntShortResponseDto toShortDto(Restorant restorant);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restorantChain", source = "chain")
    Restorant toModel(CreatedRestorantRequestDto restorantDto, RestorantChain chain);
    
}
