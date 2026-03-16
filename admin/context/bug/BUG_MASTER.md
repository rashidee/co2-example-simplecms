# Bug Master — Admin Portal

**Started**: 2026-03-16
**Context**: admin/context
**Version Filter**: v1.0.4
**Module Filter**: All
**Status**: COMPLETED

---

## Common

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-016 | v1.0.4 | Menu sidebar highlight selected menu using white background on white text color | FIXED | Root cause: Active state CSS used `bg-white text-white` (white on white). Fixed by changing to `bg-primary` (#2271b1) for readable blue-on-white contrast. Updated mockups and PRD.md. |

---

## Hero Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-017 | v1.0.4 | Hero section effective and end date not saved properly | FIXED | Root cause: JTE templates used `LocalDateTime.toString()` outputting `yyyy-MM-ddT00:00` instead of `yyyy-MM-dd` required by HTML date inputs. Fixed by using `.toLocalDate().toString()` in 6 templates (hero + blog). |

---

## Summary

| Status | Count |
|--------|-------|
| NEW | 0 |
| IN_PROGRESS | 0 |
| FIXED | 2 |
| CANNOT_REPRODUCE | 0 |
| HIGH_IMPACT | 0 |
| **Total** | **2** |
