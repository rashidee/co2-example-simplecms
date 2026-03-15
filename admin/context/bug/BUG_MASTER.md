# Bug Master — Admin Portal

**Started**: 2026-03-16
**Context**: admin/context
**Version Filter**: v1.0.2
**Module Filter**: All
**Status**: COMPLETED

---

## Common

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-002 | v1.0.2 | Menu color is dark and hard to read since the sidebar background is also dark | FIXED | Root cause: CSP blocked Alpine.js eval, so `:class` bindings failed and sidebar text had no color set. Fixed by adding `unsafe-eval` to CSP in CspNonceFilter.java. |
| BUG-003 | v1.0.2 | Dark mode toggle is not working | FIXED | Root cause: (1) CSP blocked Alpine.js eval, (2) Tailwind v4 dark mode used `@media prefers-color-scheme` instead of class-based. Fixed: CSP `unsafe-eval` + `@custom-variant dark (&:is(.dark *))` in main.css. |
| BUG-004 | v1.0.2 | User avatar dropdown menu is not working | FIXED | Root cause: CSP blocked Alpine.js eval so `x-data` couldn't initialize. Fixed by same CSP change. |
| BUG-005 | v1.0.2 | Footer not positioned at bottom and overlaps with content | FIXED | Root cause: Outer flex container missing `flex-1`, so it didn't fill the body height. Fixed: Changed `flex pt-16 min-h-[calc(100vh-4rem)]` to `flex-1 flex pt-16` in MainLayout.jte. |
| BUG-006 | v1.0.2 | Logging in as editor causing 500 error | FIXED | Root cause: PostgreSQL `IS NULL OR` JPQL pattern fails with `could not determine data type of parameter`. Fixed: Added `findAllByOrderByEffectiveDateDesc` for no-filter case; converted filtered query to native SQL with CAST. |

---

## Authentication and Authorization

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-007 | v1.0.2 | Logout URL not working and causing 403 error | FIXED | Root cause: CSRF token not passed from page templates → MainLayout → Header fragment. Fixed: Added `_csrf` param to MainLayout.jte and all 29 page templates, passed it to Header.jte. |

---

## User

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-008 | v1.0.2 | System > User menu is not working when logging in as admin | FIXED | Root cause: Sidebar links used role-prefixed paths (`/admin/users`) that had no controller. Fixed: Updated Sidebar.jte to use actual controller paths (`/users`, `/hero-section`, etc.). Also fixed Header.jte profile/account links. |
| BUG-009 | v1.0.2 | In the create/edit user form, the role selection of USER should be removed | FIXED | Root cause: UserFormView.forCreate/forEdit used `UserRole.values()` which includes USER. Fixed: Added `ASSIGNABLE_ROLES` filter excluding `UserRole.USER` in UserFormView.java. |

---

## Summary

| Status | Count |
|--------|-------|
| NEW | 0 |
| IN_PROGRESS | 0 |
| FIXED | 8 |
| CANNOT_REPRODUCE | 0 |
| HIGH_IMPACT | 0 |
| **Total** | **8** |
