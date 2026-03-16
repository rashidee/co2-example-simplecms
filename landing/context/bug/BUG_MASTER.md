# Bug Master — Landing Page

**Started**: 2026-03-16
**Context**: landing/context
**Version Filter**: v1.0.6
**Module Filter**: Hero Section
**Status**: COMPLETED

---

## Hero Section

| Code | Version | Description | Status | Remark |
|------|---------|-------------|--------|--------|
| BUG-002 | v1.0.6 | Carousel keeps refreshing every 5 seconds with non-smooth rendering, whole page appears to refresh | FIXED | Root cause: slides used `position: relative` causing vertical stacking during transitions. Fixed by wrapping slides in a fixed-height container with `position: absolute` for each slide. Removed auto-scroll interval. Updated: mockup, specification, PRD.md, E2E tests. |

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
