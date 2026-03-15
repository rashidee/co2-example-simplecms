# Fix Plan — BUG-002

**Bug**: Menu color is dark and hard to read since the sidebar background is also dark
**Module**: Common
**Root Cause**: The `text-sidebar-text/80` Tailwind class in Sidebar.jte referenced a `sidebar-text` color that was not defined in tailwind.config.js. Only `sidebar: '#1d2327'` existed as a flat color string, so `sidebar-text` didn't resolve to any color, making menu text nearly invisible.
**Impact Assessment**: Low — CSS-only change, no logic affected.

---

## Fix Checklist

- [x] 1. Add `sidebar-text` color to tailwind.config.js by converting `sidebar` from flat string to object with `DEFAULT` and `text` properties
- [x] 2. Fix Vite build configuration to properly output to static/ with correct manifest
- [x] 3. Create main.js and main.css entry points matching template references
- [x] 4. Rebuild frontend assets
- [x] 5. Verify fix visually

---

## Files Modified

| File | Change Description |
|------|-------------------|
| `admin/frontend/tailwind.config.js` | Changed `sidebar: '#1d2327'` to `sidebar: { DEFAULT: '#1d2327', text: '#c3c4c7' }` |
| `admin/frontend/vite.config.js` | Updated entry points and output directory for proper manifest generation |
| `admin/frontend/src/main.js` | Created entry point with Alpine.js theme store |
| `admin/frontend/src/main.css` | Created CSS entry point importing app.css |

---

## Fix Log

### Step 1: Add sidebar-text color
2026-03-16 - Completed
- Changed `sidebar` in tailwind.config.js from flat string to object: `{ DEFAULT: '#1d2327', text: '#c3c4c7' }`
- This allows `text-sidebar-text/80` and `text-sidebar-text/40` classes to resolve correctly
- Result: Menu text now displays as light gray (#c3c4c7) at 80% opacity against dark background
