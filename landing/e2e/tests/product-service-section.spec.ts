import { test, expect } from '@playwright/test';

test.describe('Product and Service Section', () => {
  test('NAV-PAS-001: Product and Service section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#products');
    await expect(section).toBeVisible();
    await expect(section.locator('h2')).toContainText('Products');
  });

  test('VIEW-PAS-001: Product cards display image, title, and description', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#products');
    const cards = section.locator('.bg-surface');
    await expect(cards).toHaveCount(4);

    // First card
    const firstCard = cards.first();
    await expect(firstCard.locator('img')).toBeVisible();
    await expect(firstCard.locator('h3')).toContainText('Web Development');
    await expect(firstCard.locator('p')).toContainText('Custom web applications built with modern technologies');
  });

  test('VIEW-PAS-002: Cards are displayed in a 3-column grid on desktop', async ({ page }) => {
    await page.setViewportSize({ width: 1280, height: 800 });
    await page.goto('/');
    const grid = page.locator('#products .grid');
    await expect(grid).toBeVisible();
    // Verify grid has md:grid-cols-3 class
    await expect(grid).toHaveClass(/grid-cols-3/);
  });

  test('VIEW-PAS-003: CTA links open in a new tab', async ({ page }) => {
    await page.goto('/');
    const firstCta = page.locator('#products .bg-surface').first().locator('a');
    await expect(firstCta).toHaveAttribute('target', '_blank');
    await expect(firstCta).toHaveAttribute('href', 'https://example.com/web-dev');
  });

  test('VAL-PAS-001: Inactive products are not displayed', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#products');
    const cards = section.locator('.bg-surface');
    await expect(cards).toHaveCount(4);
    const content = await section.innerHTML();
    expect(content).not.toContain('Legacy System Support');
  });
});
