package com.simplecms.adminportal.authentication.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for PasswordResetTokenEntity.
 *
 * Traces: USA000006, NFRA00003
 */
@Repository
interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    Optional<PasswordResetTokenEntity> findByTokenAndUsedFalse(String token);

    void deleteByUserId(UUID userId);
}
