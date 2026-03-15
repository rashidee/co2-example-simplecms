package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ContactMessageRepository extends JpaRepository<ContactMessageEntity, UUID> {

    @Query("SELECT m FROM ContactMessageEntity m ORDER BY m.submittedAt DESC")
    Page<ContactMessageEntity> findAllOrderBySubmittedAtDesc(Pageable pageable);
}
