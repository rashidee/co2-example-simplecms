package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for HeroSectionEntity.
 *
 * Traces: USA000033, NFRA00033
 */
@Repository
interface HeroSectionRepository extends JpaRepository<HeroSectionEntity, UUID> {

    @Query("SELECT h FROM HeroSectionEntity h WHERE " +
           "(:status IS NULL OR h.status = :status) AND " +
           "(:effectiveDate IS NULL OR h.effectiveDate >= :effectiveDate) AND " +
           "(:expirationDate IS NULL OR h.expirationDate <= :expirationDate) " +
           "ORDER BY h.effectiveDate DESC")
    Page<HeroSectionEntity> findWithFilters(
        @Param("status") HeroSectionStatus status,
        @Param("effectiveDate") LocalDateTime effectiveDate,
        @Param("expirationDate") LocalDateTime expirationDate,
        Pageable pageable);

    List<HeroSectionEntity> findByStatusAndExpirationDateBefore(
        HeroSectionStatus status, LocalDateTime date);
}
