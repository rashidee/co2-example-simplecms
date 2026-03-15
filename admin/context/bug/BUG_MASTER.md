# Bug Master — Admin Portal

**Started**: 2026-03-16
**Context**: admin/context
**Version Filter**: v1.0.3
**Module Filter**: All
**Status**: COMPLETED

---

## Common

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-010 | v1.0.3 | All cards in all pages do not support dark mode — text hard to read when dark mode enabled | FIXED | Root cause: All page templates used hardcoded light-mode CSS (`bg-white`, `text-[#1e1e1e]`, `border-[#c3c4c7]`, `text-[#646970]`) without `dark:` Tailwind variants. Fixed by adding dark mode classes to all 44 JTE page templates. |

---

## Hero Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-011 | v1.0.3 | Error 500 when creating new hero section content | FIXED | Root cause: (1) `LocalDateTime.parse()` used for HTML date inputs that send `yyyy-MM-dd` format — fixed to `LocalDate.parse().atStartOfDay()`. (2) `@Value("${app.upload.path}")` property name mismatch with `app.upload.base-path` in YAML — fixed property name. (3) Relative upload path resolved by Tomcat differently — added `.toAbsolutePath()`. (4) DB column `expiration_date` NOT NULL but form treats it as optional — added Flyway V3 migration to DROP NOT NULL. |

---

## Product and Service Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-012 | v1.0.3 | Error 500 when creating new product/service content | FIXED | Same root cause as BUG-011: `@Value` property name mismatch and relative upload path. Fixed by same changes to `ProductServiceServiceImpl.java`. |

---

## Features Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-013 | v1.0.3 | Icon selection not showing icons in new features content form | FIXED | Root cause: (1) CSP missing `font-src` directive blocked Font Awesome font loading. (2) HTML `<option>` elements can't render web fonts — replaced Unicode chars with text labels + Alpine.js icon preview box next to select. Fixed both create and edit pages. |

---

## Team Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-014 | v1.0.3 | Error 500 when creating new team member content | FIXED | Same root cause as BUG-011: `@Value` property name mismatch and relative upload path. Fixed by same changes to `TeamMemberServiceImpl.java`. |

---

## Blog

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-015 | v1.0.3 | Menu Blog Posts not working when logging in as editor | FIXED | Root cause: Same as hero section v1.0.2 bug — JPQL `IS NULL OR` pattern fails on PostgreSQL. Fixed by converting to native SQL query with `CAST(:param AS type) IS NULL`. Also fixed `LocalDateTime.parse()` date issue in blog controllers. |

---

## Summary

| Status | Count |
|--------|-------|
| NEW | 0 |
| IN_PROGRESS | 0 |
| FIXED | 6 |
| CANNOT_REPRODUCE | 0 |
| HIGH_IMPACT | 0 |
| **Total** | **6** |
