package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for converting between UserEntity and UserDTO.
 *
 * Traces: NFRA00006
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper {

    UserDTO toDTO(UserEntity entity);

    List<UserDTO> toDTOList(List<UserEntity> entities);
}
