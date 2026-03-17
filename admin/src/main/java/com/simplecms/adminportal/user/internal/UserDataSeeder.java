package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds the admin user on startup if no users exist in the database.
 *
 * Traces: NFRA00015
 */
@Component
class UserDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UserDataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setEmail("admin@simplecms.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setForcePasswordChange(true);
            admin.setCreatedBy("SYSTEM");

            userRepository.save(admin);
            log.info("Admin user seeded: admin@simplecms.com / password (change on first login)");
        }

        // Seed demo editor account if it does not exist
        if (userRepository.findByEmail("editor@simplecms.com").isEmpty()) {
            UserEntity editor = new UserEntity();
            editor.setEmail("editor@simplecms.com");
            editor.setPassword(passwordEncoder.encode("password"));
            editor.setFirstName("Demo");
            editor.setLastName("Editor");
            editor.setRole(UserRole.EDITOR);
            editor.setStatus(UserStatus.ACTIVE);
            editor.setForcePasswordChange(false);
            editor.setCreatedBy("SYSTEM");

            userRepository.save(editor);
            log.info("Demo editor user seeded: editor@simplecms.com / password");
        }
    }
}
