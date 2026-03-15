package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;
import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ContactMapper {

    ContactInfoDTO toInfoDTO(ContactInfoEntity entity);

    @Mapping(target = "hasResponse", ignore = true)
    ContactMessageDTO toMessageDTO(ContactMessageEntity entity);

    ContactResponseDTO toResponseDTO(ContactResponseEntity entity);
}
