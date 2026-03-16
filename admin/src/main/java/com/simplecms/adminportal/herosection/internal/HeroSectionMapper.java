package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for HeroSectionEntity to HeroSectionDTO.
 *
 * Traces: USA000030
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface HeroSectionMapper {

    @Mapping(target = "hasImageData", source = "entity", qualifiedByName = "hasImageData")
    HeroSectionDTO toDTO(HeroSectionEntity entity);

    List<HeroSectionDTO> toDTOList(List<HeroSectionEntity> entities);

    @Named("hasImageData")
    default boolean hasImageData(HeroSectionEntity entity) {
        return entity.getImageData() != null && entity.getImageData().length > 0;
    }
}
