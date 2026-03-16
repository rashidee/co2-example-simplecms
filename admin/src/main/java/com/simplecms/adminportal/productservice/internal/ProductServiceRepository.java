package com.simplecms.adminportal.productservice.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for ProductServiceEntity.
 *
 * Traces: USA000045, NFRA00054
 */
@Repository
interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, UUID> {

    @Query("SELECT p FROM ProductServiceEntity p " +
           "ORDER BY p.displayOrder ASC, p.createdAt ASC")
    Page<ProductServiceEntity> findWithFilters(Pageable pageable);
}
