package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for HeroSectionEntity to HeroSectionDTO.
 *
 * Traces: USA000030
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface HeroSectionMapper {

    HeroSectionDTO toDTO(HeroSectionEntity entity);

    List<HeroSectionDTO> toDTOList(List<HeroSectionEntity> entities);
}
