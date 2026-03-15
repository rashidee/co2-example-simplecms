package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface TestimonialMapper {

    TestimonialDTO toDTO(TestimonialEntity entity);

    List<TestimonialDTO> toDTOList(List<TestimonialEntity> entities);
}
