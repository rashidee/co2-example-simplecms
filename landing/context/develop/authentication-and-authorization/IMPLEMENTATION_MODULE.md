# Implementation - Authentication and Authorization (v1.0.7)

**Module**: Authentication and Authorization
**Layer**: L1
**Status**: COMPLETED
**Started**: 2026-03-17
**Completed**: 2026-03-17

---

## Implementation Checklist

### v1.0.7 User Story

- [x] 1. Add "Admin" menu link to the top right corner of the landing page navbar
  - Opens in new tab (target="_blank")
  - Redirects to https://admin.{domain}
  - Configurable via ADMIN_URL environment variable

### Changes Made

- [x] `resources/views/partials/navbar.blade.php` — Added "Admin" link to both desktop and mobile menus with target="_blank" and rel="noopener", separated by a vertical divider in desktop view
- [x] `config/app.php` — Added `admin_url` config key reading from ADMIN_URL env var
- [x] `.env` — Added ADMIN_URL=http://localhost:8080 for local development

### Verification

- [x] Landing page loads correctly with Admin link visible in navbar
- [x] Admin link has target="_blank" attribute (verified via Playwright)
- [x] Link uses configurable ADMIN_URL, falls back to https://admin.{domain}
- [x] Screenshot: .playwright-mcp/landing-navbar-v107-admin-link.png
