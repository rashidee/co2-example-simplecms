# Test Spec — BUG-002

**Bug**: Carousel keeps refreshing every 5 seconds with non-smooth rendering
**Module**: Hero Section
**Reproduced**: Yes (layout stacking issue confirmed)

---

## Pre-Conditions

- Landing page is running on localhost:8000
- At least 2 active hero slides exist in the database

## Steps to Verify Fix

1. Navigate to the landing page
2. Observe the hero carousel — it should NOT auto-advance automatically
3. Click the next arrow — the slide should transition smoothly without layout jump
4. Click the previous arrow — same smooth transition
5. Click dot indicators — same smooth transition
6. Verify hero section height remains constant during transitions
7. Verify no vertical stacking of slides during transition

## Expected Result After Fix

- No auto-scroll interval — user controls navigation manually
- Slides transition smoothly using fade or crossfade effect
- No layout shift or vertical stacking during transitions
- Hero section height remains constant at 500px
- Only one slide visible at any time (no stacking)

## Playwright Verification Script

```typescript
test('BUG-002: Smooth carousel without auto-scroll', async ({ page }) => {
    await page.goto('/');
    await page.waitForSelector('#hero');

    // Verify no auto-scroll after 6 seconds
    const headlineInitial = await page.locator('#hero h1:visible').textContent();
    await page.waitForTimeout(6000);
    const headlineAfter6s = await page.locator('#hero h1:visible').textContent();
    expect(headlineAfter6s).toBe(headlineInitial);

    // Click next and verify smooth transition (no layout shift)
    const heroBefore = await page.locator('#hero').boundingBox();
    await page.click('#hero button[aria-label="Next slide"]');
    await page.waitForTimeout(600);
    const heroAfter = await page.locator('#hero').boundingBox();
    expect(Math.abs(heroAfter!.height - heroBefore!.height)).toBeLessThan(5);

    // Verify slides don't stack during transition
    await page.click('#hero button[aria-label="Next slide"]');
    await page.waitForTimeout(100); // mid-transition
    const heroHeight = await page.locator('#hero').evaluate(el => el.scrollHeight);
    expect(heroHeight).toBeLessThan(600); // should not be 1000+ from stacking
});
```
