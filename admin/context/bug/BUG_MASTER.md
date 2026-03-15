# Bug Master — Admin Portal

**Started**: 2026-03-16
**Context**: admin/context
**Version Filter**: v1.0.1
**Module Filter**: Authentication and Authorization
**Status**: COMPLETED

---

## Authentication and Authorization

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-001 | v1.0.1 | 403 error after successful login | FIXED | Fixed missing CSRF token in all POST forms; created HomeController for `/` redirect. Updated: WebMvcConfig, 36 JTE templates, new HomeController, SPEC.md |

---

## Summary

| Status | Count |
|--------|-------|
| NEW | 0 |
| IN_PROGRESS | 0 |
| FIXED | 1 |
| CANNOT_REPRODUCE | 0 |
| HIGH_IMPACT | 0 |
| **Total** | **1** |
