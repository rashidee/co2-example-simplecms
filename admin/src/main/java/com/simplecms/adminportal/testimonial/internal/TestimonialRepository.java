package com.simplecms.adminportal.testimonial.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Traces: USA000069, NFRA00078
 */
@Repository
interface TestimonialRepository extends JpaRepository<TestimonialEntity, UUID> {

    @Query("SELECT t FROM TestimonialEntity t " +
           "ORDER BY t.displayOrder ASC, t.createdAt ASC")
    Page<TestimonialEntity> findWithFilters(Pageable pageable);
}
