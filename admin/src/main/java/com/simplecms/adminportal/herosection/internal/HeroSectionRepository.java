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

    @Query(value = "SELECT * FROM hrs_hero_section h WHERE " +
           "(CAST(:status AS VARCHAR) IS NULL OR h.status = :status) AND " +
           "(CAST(:effectiveDate AS TIMESTAMP) IS NULL OR h.effective_date >= :effectiveDate) AND " +
           "(CAST(:expirationDate AS TIMESTAMP) IS NULL OR h.expiration_date <= :expirationDate) " +
           "ORDER BY h.effective_date DESC",
           countQuery = "SELECT COUNT(*) FROM hrs_hero_section h WHERE " +
           "(CAST(:status AS VARCHAR) IS NULL OR h.status = :status) AND " +
           "(CAST(:effectiveDate AS TIMESTAMP) IS NULL OR h.effective_date >= :effectiveDate) AND " +
           "(CAST(:expirationDate AS TIMESTAMP) IS NULL OR h.expiration_date <= :expirationDate)",
           nativeQuery = true)
    Page<HeroSectionEntity> findWithFilters(
        @Param("status") String status,
        @Param("effectiveDate") LocalDateTime effectiveDate,
        @Param("expirationDate") LocalDateTime expirationDate,
        Pageable pageable);

    Page<HeroSectionEntity> findAllByOrderByEffectiveDateDesc(Pageable pageable);

    List<HeroSectionEntity> findByStatusAndExpirationDateBefore(
        HeroSectionStatus status, LocalDateTime date);
}
