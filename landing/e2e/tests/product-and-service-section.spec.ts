import { test, expect } from '@playwright/test';

test.describe('Product and Service Section', () => {

    test('VIEW-PAS-001: Verify products/services section renders all items', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#products');
        await expect(section).toBeVisible();

        // Section heading
        await expect(section.locator('h2')).toHaveText('Our Products and Services');

        // 4 product cards visible
        const cards = section.locator('.grid > div');
        await expect(cards).toHaveCount(4);

        // First card is Website Builder (display_order=1)
        await expect(cards.first().locator('h3')).toHaveText('Website Builder');

        // Last card is Analytics Dashboard (display_order=4)
        await expect(cards.nth(3).locator('h3')).toHaveText('Analytics Dashboard');
    });

    test('LAY-PAS-001: 3-column desktop, 1-column mobile layout', async ({ page }) => {
        // Desktop viewport
        await page.setViewportSize({ width: 1280, height: 720 });
        await page.goto('/');

        const grid = page.locator('#products .grid');
        await expect(grid).toBeVisible();

        // Check that grid has md:grid-cols-3 class
        await expect(grid).toHaveClass(/md:grid-cols-3/);

        // Check white background on section
        const section = page.locator('#products');
        await expect(section).toHaveClass(/bg-white/);
    });

    test('CONT-PAS-001: Product card contains all required elements', async ({ page }) => {
        await page.goto('/');

        const firstCard = page.locator('#products .grid > div').first();
        await expect(firstCard).toBeVisible();

        // Title
        await expect(firstCard.locator('h3')).toHaveText('Website Builder');

        // Description
        await expect(firstCard.locator('p')).toContainText('Drag-and-drop page builder');

        // CTA link with target="_blank"
        const ctaLink = firstCard.locator('a');
        await expect(ctaLink).toContainText('Learn More');
        await expect(ctaLink).toHaveAttribute('target', '_blank');
    });
});
