package com.simplecms.adminportal.blog.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface BlogCategoryRepository extends JpaRepository<BlogCategoryEntity, UUID> {

    Optional<BlogCategoryEntity> findByName(String name);

    boolean existsByName(String name);
}
