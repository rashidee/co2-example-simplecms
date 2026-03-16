package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for ProductServiceEntity to ProductServiceDTO.
 *
 * Traces: USA000036
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ProductServiceMapper {

    @Mapping(target = "hasImageData", source = "entity", qualifiedByName = "hasImageData")
    ProductServiceDTO toDTO(ProductServiceEntity entity);

    List<ProductServiceDTO> toDTOList(List<ProductServiceEntity> entities);

    @Named("hasImageData")
    default boolean hasImageData(ProductServiceEntity entity) {
        return entity.getImageData() != null && entity.getImageData().length > 0;
    }
}
