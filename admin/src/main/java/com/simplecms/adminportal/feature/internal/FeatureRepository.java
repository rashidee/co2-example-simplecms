package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Traces: USA000057, NFRA00066
 */
@Repository
interface FeatureRepository extends JpaRepository<FeatureEntity, UUID> {

    @Query("SELECT f FROM FeatureEntity f WHERE " +
           "(:status IS NULL OR f.status = :status) " +
           "ORDER BY f.displayOrder ASC, f.createdAt ASC")
    Page<FeatureEntity> findWithFilters(
        @Param("status") FeatureStatus status,
        Pageable pageable);
}
