package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Custom UserDetailsService that loads user details from the database.
 * Checks that user status is ACTIVE before allowing login.
 *
 * Traces: CONSA0006
 */
@Service
class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (userEntity.getStatus() != UserStatus.ACTIVE) {
            log.warn("Login attempt for inactive user: {}", email);
            throw new UsernameNotFoundException("User account is inactive: " + email);
        }

        return new User(
            userEntity.getEmail(),
            userEntity.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()))
        );
    }
}
