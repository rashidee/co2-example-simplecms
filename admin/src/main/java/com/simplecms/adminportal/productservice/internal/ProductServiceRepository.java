package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for ProductServiceEntity.
 *
 * Traces: USA000045, NFRA00054
 */
@Repository
interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, UUID> {

    @Query("SELECT p FROM ProductServiceEntity p WHERE " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.displayOrder ASC, p.createdAt ASC")
    Page<ProductServiceEntity> findWithFilters(
        @Param("status") ProductServiceStatus status,
        Pageable pageable);
}
