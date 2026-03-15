package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ContactInfoRepository extends JpaRepository<ContactInfoEntity, UUID> {

    /**
     * Find the single contact info record.
     * Per CONSA0027, only one record exists.
     */
    default Optional<ContactInfoEntity> findSingle() {
        return findAll().stream().findFirst();
    }
}
