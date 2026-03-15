package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService.
 *
 * Traces: USA000012-027, NFRA00006-015, CONSA0003, CONSA0006, CONSA0009
 */
@Service
@Transactional
class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    UserServiceImpl(UserRepository userRepository,
                    UserMapper userMapper,
                    PasswordEncoder passwordEncoder,
                    ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getProfile(UUID userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return userMapper.toDTO(entity);
    }

    /**
     * Only firstName and lastName are editable on profile.
     *
     * Traces: USA000012, CONSA0003
     */
    @Override
    public UserDTO updateProfile(UUID userId, String firstName, String lastName) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setUpdatedBy(entity.getEmail());

        UserEntity saved = userRepository.save(entity);
        return userMapper.toDTO(saved);
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setPassword(newPassword);
        entity.setForcePasswordChange(false);
        entity.setUpdatedBy(entity.getEmail());

        userRepository.save(entity);
        log.info("Password changed for user: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    /**
     * Create a new user with default password "password" and forcePasswordChange=true.
     *
     * Traces: USA000021, NFRA00006, NFRA00009, NFRA00012
     */
    @Override
    public UserDTO createUser(String email, String firstName, String lastName,
                              UserRole role, UserStatus status) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setRole(role);
        entity.setStatus(status);
        entity.setForcePasswordChange(true);
        entity.setCreatedBy("ADMIN");

        UserEntity saved = userRepository.save(entity);
        eventPublisher.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail(), saved.getRole()));

        log.info("User created: {} with role {}", saved.getEmail(), saved.getRole());
        return userMapper.toDTO(saved);
    }

    @Override
    public UserDTO updateUser(UUID userId, String email, String firstName, String lastName,
                              UserRole role, UserStatus status) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!entity.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        entity.setEmail(email);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setRole(role);
        entity.setStatus(status);
        entity.setUpdatedBy("ADMIN");

        UserEntity saved = userRepository.save(entity);
        eventPublisher.publishEvent(new UserUpdatedEvent(saved.getId(), saved.getEmail()));

        return userMapper.toDTO(saved);
    }

    @Override
    public void deleteUser(UUID userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        userRepository.delete(entity);
        eventPublisher.publishEvent(new UserDeletedEvent(entity.getId(), entity.getEmail()));

        log.info("User deleted: {}", entity.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDTO);
    }

    /**
     * List users with EDITOR role for blog author selection.
     *
     * Traces: CONSA0039 (cross-module)
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listEditors() {
        return userMapper.toDTOList(userRepository.findByRole(UserRole.EDITOR));
    }
}
