package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for ProductServiceEntity to ProductServiceDTO.
 *
 * Traces: USA000036
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ProductServiceMapper {

    ProductServiceDTO toDTO(ProductServiceEntity entity);

    List<ProductServiceDTO> toDTOList(List<ProductServiceEntity> entities);
}
