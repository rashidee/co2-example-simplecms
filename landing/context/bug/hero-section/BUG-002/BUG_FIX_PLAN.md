# Fix Plan — BUG-002

**Bug**: Carousel keeps refreshing every 5 seconds with non-smooth rendering
**Module**: Hero Section
**Root Cause**: Slides used `position: relative` with `x-show` + `x-transition`. During Alpine.js transitions, both the leaving and entering slides had `display: block` simultaneously, causing them to stack vertically (doubling the section height momentarily). Combined with `setInterval` every 5 seconds, this created a repeated jarring visual effect.
**Impact Assessment**: Low — changes isolated to hero carousel section only

---

## Fix Checklist

- [x] 1. Remove `setInterval` auto-scroll from carousel (removed `startAuto()`, `stopAuto()`, `autoSlide` and `x-init="startAuto()"`)
- [x] 2. Fix slide positioning: wrapped slides in a `relative w-full h-[500px]` container, made each slide `absolute inset-0 w-full h-full`
- [x] 3. Update navigation buttons to remove `stopAuto(); startAuto()` calls
- [x] 4. Update carousel.js component to remove auto-scroll methods
- [x] 5. Verify fix with BUG_TEST_SPEC.md — all 4 verification tests pass
- [x] 6. Update existing E2E tests (CAR-HRS-001 now verifies no auto-advance; updated test data)
- [x] 7. Update artifacts (mockup, specification, PRD.md)

---

## Files Modified

| File | Change Description |
|------|-------------------|
| `resources/views/pages/home.blade.php` | Removed auto-scroll, fixed slide positioning to absolute within fixed-height container |
| `resources/js/alpine/components/carousel.js` | Removed `startAuto()`, `stopAuto()`, `autoSlide`, `init()` |
| `e2e/tests/hero-section.spec.ts` | Updated CAR-HRS-001 to verify no auto-advance; updated slide data |
| `context/mockup/pages/home.html` | Updated mockup to match new carousel implementation |
| `context/specification/hero-section/SPEC.md` | Updated NFR and code reference |
| `context/PRD.md` | Added Bug section under Hero Section |

---

## Fix Log

### Step 1: Reproduction
2026-03-16 - Reproduced with Playwright
- Confirmed 2 slides with `display: block` + `position: relative` during transitions
- No full page navigations detected — issue is purely visual (layout stacking)
- Hero scrollHeight during transition was correct but visual jump occurred due to stacking

### Step 2: Applied Fix
2026-03-16 - Applied fix
- Removed auto-scroll interval, `startAuto()`, `stopAuto()` from Blade template and carousel.js
- Wrapped slide `<template>` in `<div class="relative w-full h-[500px]">`
- Changed each slide from `class="relative w-full" style="min-height:500px;"` to `class="absolute inset-0 w-full h-full"`
- Rebuilt frontend assets with `npm run build`

### Step 3: Verification
2026-03-16 - All tests pass
- Verification tests: 4/4 passed — no auto-scroll, no layout shift, absolute positioning confirmed, scroll height = 500 during transition
- Existing hero section tests: 4/4 passed (after updating test data and CAR-HRS-001)
