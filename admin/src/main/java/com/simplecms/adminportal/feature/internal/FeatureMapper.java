package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FeatureMapper {

    FeatureDTO toDTO(FeatureEntity entity);

    List<FeatureDTO> toDTOList(List<FeatureEntity> entities);
}
