# Fix Plan — BUG-016

**Bug**: Menu sidebar highlight selected menu using white background on white text color
**Module**: Common
**Root Cause**: Active menu state CSS classes use `bg-white text-white` — white background with white text on the dark sidebar (#1d2327), making the selected menu item text invisible.
**Impact Assessment**: Low — purely cosmetic fix, changes only Sidebar.jte template CSS classes.

---

## Fix Checklist

- [x] 1. Change active state `bg-white` to `bg-primary` (#2271b1) for all menu items in Sidebar.jte
- [x] 2. Change inactive hover state `hover:bg-white` to `hover:bg-white/10` for subtle hover effect
- [x] 3. Rebuild frontend CSS (npm run build)
- [x] 4. Verify fix with Playwright — screenshot shows blue active state with readable white text
- [x] 5. Update sidebar mockups (admin, editor, user) and PRD.md

---

## Files Modified

| File | Change Description |
|------|-------------------|
| `admin/src/main/jte/fragment/Sidebar.jte` | Changed active state from `bg-white` to `bg-primary`, hover from `hover:bg-white` to `hover:bg-white/10` |
| `admin/context/mockup/partials/sidebar-admin.html` | Updated active state to `bg-[#2271b1]` |
| `admin/context/mockup/partials/sidebar-editor.html` | Updated active state to `bg-[#2271b1]` |
| `admin/context/mockup/partials/sidebar-user.html` | Updated active state to `bg-[#2271b1]` |
| `admin/context/PRD.md` | Added BUG-016 fix record under v1.0.4 |

---

## Fix Log

### Step 1: Fix active and hover CSS classes
2026-03-16 - Completed
- Changed `bg-white dark:bg-slate-800/10` to `bg-primary dark:bg-primary/80` for all 10 menu items
- Changed `hover:bg-white dark:bg-slate-800/5` to `hover:bg-white/10` for all 10 menu items
- Rebuilt frontend with `npm run build`, compiled Java with `mvn compile`
- Verified via Playwright MCP: active menu items show blue (#2271b1) background with white text
