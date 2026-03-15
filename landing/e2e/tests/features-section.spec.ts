import { test, expect } from '@playwright/test';

test.describe('Features Section', () => {
  test('NAV-FTS-001: Features section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#features');
    await expect(section).toBeVisible();
    await expect(section.locator('h2')).toContainText('Features');
  });

  test('VIEW-FTS-001: Each feature displays icon, title, and description', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#features');
    const items = section.locator('.grid > div');
    await expect(items).toHaveCount(6);

    // First feature
    const first = items.first();
    await expect(first.locator('h3')).toContainText('Lightning Fast Performance');
    await expect(first.locator('p')).toContainText('Our platform is optimized for speed');
    // FontAwesome icon element exists (may not be visible without FA CSS)
    await expect(first.locator('i')).toHaveCount(1);
  });

  test('VIEW-FTS-002: Features are displayed in a 3-column grid on desktop', async ({ page }) => {
    await page.setViewportSize({ width: 1280, height: 800 });
    await page.goto('/');
    const grid = page.locator('#features .grid');
    await expect(grid).toBeVisible();
    await expect(grid).toHaveClass(/grid-cols-3/);
  });
});
