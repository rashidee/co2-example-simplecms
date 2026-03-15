# Fix Plan — BUG-010

**Bug**: All cards in all pages do not support dark mode — text hard to read when dark mode enabled
**Module**: Common
**Root Cause**: All page templates use hardcoded light-mode CSS classes (`bg-white`, `text-[#1e1e1e]`, `border-[#c3c4c7]`, `text-[#646970]`) without corresponding `dark:` Tailwind variants. The Card.jte component has dark mode support, but individual page templates bypass it with inline styles.
**Impact Assessment**: Low — adding dark mode variants is purely additive and does not affect light mode behavior.

---

## Fix Checklist

- [ ] 1. Add dark mode variants to all JTE page templates using bulk replacements
- [ ] 2. Rebuild frontend CSS (npm run build) to ensure Tailwind picks up new dark: classes
- [ ] 3. Verify fix with Playwright screenshots in dark mode
- [ ] 4. Update artifacts (mockups/specs if needed)

---

## Replacement Rules

| Light Mode Class | Dark Mode Addition |
|------------------|--------------------|
| `bg-white` | `dark:bg-slate-800` |
| `bg-gray-50` | `dark:bg-slate-700` |
| `text-[#1e1e1e]` | `dark:text-slate-100` |
| `text-[#646970]` | `dark:text-slate-400` |
| `border-[#c3c4c7]` | `dark:border-slate-600` |
| `hover:bg-gray-50` | `dark:hover:bg-slate-700` |

## Files to Modify

All JTE files under `admin/src/main/jte/` (except component/*.jte and layout/*.jte which already have dark mode support).

---

## Fix Log

### Step 1: Bulk dark mode class additions
2026-03-16 - Started
