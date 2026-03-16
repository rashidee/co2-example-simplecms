# Fix Plan — BUG-001

**Bug**: Carousel refreshes entire page instead of scrolling items
**Module**: Hero Section
**Status**: CANNOT_REPRODUCE

---

## Investigation Summary

### What was tested

1. **Manual navigation** — Clicked "Next slide" and "Previous slide" buttons, and dot indicators. Slides transitioned smoothly with Alpine.js opacity transitions. No page reload.
2. **Auto-scroll** — Waited for the 5-second auto-advance interval. Slides changed correctly without any page reload or network requests.
3. **Network monitoring** — No HTTP requests were made after the initial page load during carousel interactions. The carousel is entirely client-side (Alpine.js `setInterval`).
4. **Navigation monitoring** — Installed `beforeunload` listener and `PerformanceObserver` for navigation events. Zero page navigations detected during carousel use.
5. **Existing E2E tests** — `CAR-HRS-001` (auto-advance) and `CAR-HRS-002` (manual navigation) both pass.

### Code analysis

- The hero carousel uses inline Alpine.js `x-data` with `setInterval` for auto-advance (`home.blade.php` lines 5-19)
- Navigation buttons use `@click` handlers that call `next()`/`prev()` — no form submissions or links
- The CTA button uses `target="_blank"` so it opens in a new tab, not the current page
- HTMX is loaded globally but has no `hx-*` attributes on the hero section elements
- No `hx-boost` or global HTMX interceptors that could cause full page navigations

### Conclusion

The carousel works as expected. The reported "whole page refresh" could not be reproduced. Possible explanations:
- The issue may have been a browser cache/extension conflict on the reporter's machine
- The issue may have been transient (network glitch causing image re-fetch perceived as reload)
- The issue may have already been fixed in a previous deployment

### Evidence

- `screenshot_cannot_reproduce.png` — Carousel showing a slide (no reload)
- `screenshot_autoscroll_working.png` — Carousel auto-advanced to a different slide
