package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for UserEntity.
 *
 * Traces: USA000018, USA000021, USA000024, USA000027
 */
@Repository
interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserEntity> findByRole(UserRole role);

    long count();
}
