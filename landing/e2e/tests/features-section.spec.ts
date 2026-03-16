import { test, expect } from '@playwright/test';

test.describe('Features Section', () => {

    test('VIEW-FTS-001: Verify features section renders all items', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#features');
        await expect(section).toBeVisible();

        // Section heading
        await expect(section.locator('h2')).toHaveText('Key Features and Benefits');

        // 4 feature cards visible
        const cards = section.locator('.grid > div');
        await expect(cards).toHaveCount(4);

        // First card is Responsive Design (display_order=1)
        await expect(cards.first().locator('h3')).toHaveText('Responsive Design');

        // Last card is Easy Customisation (display_order=4)
        await expect(cards.nth(3).locator('h3')).toHaveText('Easy Customisation');
    });

    test('LAY-FTS-001: 3-column desktop layout', async ({ page }) => {
        await page.setViewportSize({ width: 1280, height: 720 });
        await page.goto('/');

        const grid = page.locator('#features .grid');
        await expect(grid).toBeVisible();

        // Check that grid has md:grid-cols-3 class
        await expect(grid).toHaveClass(/md:grid-cols-3/);

        // Check bg-page-bg background on section
        const section = page.locator('#features');
        await expect(section).toHaveClass(/bg-page-bg/);
    });

    test('CONT-FTS-001: Feature card contains icon, title, and description', async ({ page }) => {
        await page.goto('/');

        const firstCard = page.locator('#features .grid > div').first();
        await expect(firstCard).toBeVisible();

        // FontAwesome icon present
        const icon = firstCard.locator('i.fa-solid.fa-globe');
        await expect(icon).toBeVisible();

        // Title
        await expect(firstCard.locator('h3')).toHaveText('Responsive Design');

        // Description
        await expect(firstCard.locator('p')).toContainText('Every page you create looks great');

        // Check second card icon
        const secondCard = page.locator('#features .grid > div').nth(1);
        const lockIcon = secondCard.locator('i.fa-solid.fa-lock');
        await expect(lockIcon).toBeVisible();
        await expect(secondCard.locator('h3')).toHaveText('Secure & Reliable');
    });
});
