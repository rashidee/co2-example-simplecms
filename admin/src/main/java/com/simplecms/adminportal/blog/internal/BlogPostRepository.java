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

    @Query(value = "SELECT * FROM blg_blog_post p WHERE " +
           "(CAST(:status AS VARCHAR) IS NULL OR p.status = :status) AND " +
           "(CAST(:effectiveDate AS TIMESTAMP) IS NULL OR p.effective_date >= :effectiveDate) AND " +
           "(CAST(:expirationDate AS TIMESTAMP) IS NULL OR p.expiration_date <= :expirationDate) " +
           "ORDER BY p.effective_date DESC",
           countQuery = "SELECT COUNT(*) FROM blg_blog_post p WHERE " +
           "(CAST(:status AS VARCHAR) IS NULL OR p.status = :status) AND " +
           "(CAST(:effectiveDate AS TIMESTAMP) IS NULL OR p.effective_date >= :effectiveDate) AND " +
           "(CAST(:expirationDate AS TIMESTAMP) IS NULL OR p.expiration_date <= :expirationDate)",
           nativeQuery = true)
    Page<BlogPostEntity> findWithFilters(
        @Param("status") String status,
        @Param("effectiveDate") LocalDateTime effectiveDate,
        @Param("expirationDate") LocalDateTime expirationDate,
        Pageable pageable);

    boolean existsByCategoryId(UUID categoryId);

    boolean existsBySlug(String slug);
}
