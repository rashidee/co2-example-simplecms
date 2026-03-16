package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for TeamMemberEntity to TeamMemberDTO.
 *
 * Traces: USA000072
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface TeamMemberMapper {

    @Mapping(target = "hasImageData", source = "entity", qualifiedByName = "hasImageData")
    TeamMemberDTO toDTO(TeamMemberEntity entity);

    List<TeamMemberDTO> toDTOList(List<TeamMemberEntity> entities);

    @Named("hasImageData")
    default boolean hasImageData(TeamMemberEntity entity) {
        return entity.getProfilePictureData() != null && entity.getProfilePictureData().length > 0;
    }
}
