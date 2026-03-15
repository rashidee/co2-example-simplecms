package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
interface BlogPostRepository extends JpaRepository<BlogPostEntity, UUID> {

    @Query("SELECT p FROM BlogPostEntity p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:effectiveDate IS NULL OR p.effectiveDate >= :effectiveDate) AND " +
           "(:expirationDate IS NULL OR p.expirationDate <= :expirationDate) " +
           "ORDER BY p.effectiveDate DESC")
    Page<BlogPostEntity> findWithFilters(
        @Param("status") BlogPostStatus status,
        @Param("effectiveDate") LocalDateTime effectiveDate,
        @Param("expirationDate") LocalDateTime expirationDate,
        Pageable pageable);

    boolean existsByCategoryId(UUID categoryId);

    boolean existsBySlug(String slug);
}
