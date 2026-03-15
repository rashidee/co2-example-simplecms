package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, UUID> {

    @Query("SELECT t FROM TeamMemberEntity t WHERE " +
           "(:status IS NULL OR t.status = :status) " +
           "ORDER BY t.displayOrder ASC, t.createdAt ASC")
    Page<TeamMemberEntity> findWithFilters(
        @Param("status") TeamMemberStatus status,
        Pageable pageable);
}
