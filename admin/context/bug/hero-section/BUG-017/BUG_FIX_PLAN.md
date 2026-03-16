# Fix Plan — BUG-017

**Bug**: Hero section effective and end date not saved properly (show incorrect values when editing)
**Module**: Hero Section
**Root Cause**: JTE edit templates use `LocalDateTime.toString()` which outputs `2026-04-01T00:00` format, but HTML `<input type="date">` requires `yyyy-MM-dd` format. The `T00:00` suffix makes the value invalid for the date input, causing dates to appear empty/incorrect.
**Impact Assessment**: Low — template-only change, no logic changes needed.

---

## Fix Checklist

- [x] 1. Change `.toString()` to `.toLocalDate().toString()` in HeroSectionEditPage.jte (effective + expiration)
- [x] 2. Fix same issue in HeroSectionListPage.jte and HeroSectionCardGrid.jte (display dates)
- [x] 3. Fix same issue in BlogPostEditPage.jte, BlogPostListPage.jte, BlogPostCardGrid.jte (same pattern)
- [x] 4. Recompile and verify with Playwright
- [x] 5. Update PRD.md with bug fix record

---

## Files Modified

| File | Change Description |
|------|-------------------|
| `admin/src/main/jte/herosection/HeroSectionEditPage.jte` | `.toString()` → `.toLocalDate().toString()` for both date fields |
| `admin/src/main/jte/herosection/HeroSectionListPage.jte` | `.toString()` → `.toLocalDate().toString()` for display dates |
| `admin/src/main/jte/herosection/fragments/HeroSectionCardGrid.jte` | `.toString()` → `.toLocalDate().toString()` for display dates |
| `admin/src/main/jte/blog/BlogPostEditPage.jte` | `.toString()` → `.toLocalDate().toString()` for both date fields |
| `admin/src/main/jte/blog/BlogPostListPage.jte` | `.toString()` → `.toLocalDate().toString()` for display dates |
| `admin/src/main/jte/blog/fragments/BlogPostCardGrid.jte` | `.toString()` → `.toLocalDate().toString()` for display dates |

---

## Fix Log

### Step 1: Fix date formatting in JTE templates
2026-03-16 - Completed
- Changed all `LocalDateTime.toString()` to `LocalDateTime.toLocalDate().toString()` across 6 JTE templates
- Outputs `yyyy-MM-dd` format compatible with HTML date inputs
- Verified: Edit form shows `2026-04-01` and `2026-06-30` correctly populated in date fields
