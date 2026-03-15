package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ContactResponseRepository extends JpaRepository<ContactResponseEntity, UUID> {

    Optional<ContactResponseEntity> findByContactMessageId(UUID contactMessageId);

    boolean existsByContactMessageId(UUID contactMessageId);

    /**
     * Find responses that have not been sent yet (sentAt IS NULL).
     * Used by the async email Quartz job.
     *
     * Traces: NFRA00108
     */
    List<ContactResponseEntity> findBySentAtIsNull();
}
