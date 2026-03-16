package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface BlogMapper {

    BlogCategoryDTO toCategoryDTO(BlogCategoryEntity entity);

    List<BlogCategoryDTO> toCategoryDTOList(List<BlogCategoryEntity> entities);

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "hasImageData", source = "entity", qualifiedByName = "hasImageData")
    BlogPostDTO toPostDTO(BlogPostEntity entity);

    @Named("hasImageData")
    default boolean hasImageData(BlogPostEntity entity) {
        return entity.getImageData() != null && entity.getImageData().length > 0;
    }
}
