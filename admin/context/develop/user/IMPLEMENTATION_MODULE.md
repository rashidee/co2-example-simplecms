# Implementation - User Module

**Module**: User
**Layer**: L1 (Auth)
**Status**: IN PROGRESS
**Started**: 2026-03-15

---

## Resources

| Resource | Path |
|----------|------|
| User Stories | USA000012, USA000015, USA000018, USA000021, USA000024, USA000027 |
| NFRs | NFRA00006, NFRA00009, NFRA00012, NFRA00015 |
| Constraints | CONSA0003, CONSA0006, CONSA0009 |
| Model | `model/user/model.md` |
| Specification | `specification/user/SPEC.md` |
| Test Spec | `test/user/TEST_SPEC.md` |
| Mockups | `mockup/admin/content/users*.html`, `mockup/*/content/profile.html`, `mockup/*/content/account.html` |

---

## Implementation Checklist

### UI Layer

- [x] 1. Read and analyze module resources
- [x] 2. Implement enums (UserRole, UserStatus)
- [x] 3. Implement UserEntity (JPA)
- [x] 4. Implement UserRepository
- [x] 5. Implement UserService interface + UserDTO
- [x] 6. Implement UserMapper (MapStruct)
- [x] 7. Implement UserServiceImpl
- [x] 8. Implement UserDataSeeder (NFRA00015)
- [x] 9. Implement ProfilePageController + ProfileView
- [x] 10. Implement AccountPageController + AccountView
- [x] 11. Implement UserPageController + UserListView + UserFormView (ADMIN only)
- [x] 12. Implement JTE templates (Profile, Account, UserList, UserCreate, UserEdit)
- [ ] 13. Write Playwright E2E tests
- [ ] 14. Run E2E tests and verify

### User Stories

- [x] USA000012: View profile (ProfilePageController + ProfileView)
- [x] USA000015: View account / change password (AccountPageController + AccountView)
- [x] USA000018: View user list (UserPageController + UserListView)
- [x] USA000021: Create user (UserPageController + UserFormView)
- [x] USA000024: Edit user (UserPageController + UserFormView)
- [x] USA000027: Delete user (UserPageController.deleteUser)

### Non-Functional Requirements

- [x] NFRA00006: User fields (email, password, name, role, status) — UserEntity
- [x] NFRA00009: Default roles USER/EDITOR, admin upgrades to ADMIN — UserRole enum
- [x] NFRA00012: Default password "password", force change on first login — UserServiceImpl.createUser
- [x] NFRA00015: Seed admin user on startup if no users exist — UserDataSeeder

---

## Implementation Log

### Step 1: Analyze Module Resources
2026-03-15 - Started
- Read SPEC.md, model.md, TEST_SPEC.md
- Module has 6 user stories, 4 NFRs, 3 constraints
- Entities: UserEntity with role/status enums
- Controllers: ProfilePageController, AccountPageController, UserPageController (ADMIN)
- Templates: 5 JTE pages

### Scaffolding Fixes (2026-03-15 session 2)
- Created shared layouts: MainLayout.jte, AuthLayout.jte, ErrorLayout.jte
- Created fragments: Header.jte, Sidebar.jte, Footer.jte
- Created 9 UI components: Alert, Badge, Button, Card, FormControl, Modal, Pagination, Table, Toast
- Created error pages: 403.jte, 404.jte, 500.jte
- Created ViteManifestService, WebApplicationException, GlobalExceptionHandler, WebMvcConfig
- Created frontend build: Vite config, Tailwind config, PostCSS config, Alpine.js + htmx entry points
- Fixed SecurityConfig: role-based URL access (ADMIN, EDITOR), DaoAuthenticationProvider
- Fixed BaseEntity: UUID type instead of CHAR(36)
- Fixed entity table names: pas_product_service, cts_contact_info, cts_contact_message, cts_contact_response
- Fixed entity column names: email_address, physical_address, message_content, contact_message_id, response_content
- Fixed LocalDate→LocalDateTime for effective_date/expiration_date in HeroSection and BlogPost
- Fixed JTE: selected/disabled attribute expressions, UUID/Instant/LocalDate.toString() in outputs
- Fixed Flyway: clean V1 migration with correct spec schema, V2 for event_publication
- Fixed pom.xml: frontend working directory, JTE maven plugin, lombok-mapstruct-binding
- All 29 page templates updated with common params (vite, userRole, etc.)
- Application compiles (204 Java files, 56 JTE templates) and starts successfully
- Admin user seeded on startup
