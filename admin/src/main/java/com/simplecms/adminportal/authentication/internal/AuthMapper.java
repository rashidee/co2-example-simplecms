package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.PasswordResetTokenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between PasswordResetTokenEntity and DTO.
 *
 * Traces: USA000006
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface AuthMapper {

    PasswordResetTokenDTO toDTO(PasswordResetTokenEntity entity);
}
