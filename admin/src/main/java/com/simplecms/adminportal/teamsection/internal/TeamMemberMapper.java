package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface TeamMemberMapper {

    TeamMemberDTO toDTO(TeamMemberEntity entity);

    List<TeamMemberDTO> toDTOList(List<TeamMemberEntity> entities);
}
