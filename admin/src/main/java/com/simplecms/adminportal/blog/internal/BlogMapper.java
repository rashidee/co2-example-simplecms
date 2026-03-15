package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface BlogMapper {

    BlogCategoryDTO toCategoryDTO(BlogCategoryEntity entity);

    List<BlogCategoryDTO> toCategoryDTOList(List<BlogCategoryEntity> entities);

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "authorName", ignore = true)
    BlogPostDTO toPostDTO(BlogPostEntity entity);
}
