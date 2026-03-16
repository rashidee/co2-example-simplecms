import { test, expect } from '@playwright/test';

const BUG_FOLDER = 'context/bug/hero-section/BUG-001';

test.describe('BUG-001: Reproduce carousel full page refresh', () => {

    test('Click next arrow should not cause full page reload', async ({ page }) => {
        let navigationCount = 0;

        // Count full page navigations (not just hash changes)
        page.on('load', () => {
            navigationCount++;
        });

        await page.goto('/');
        await page.waitForTimeout(1000);

        // Reset counter after initial load
        navigationCount = 0;

        // Take screenshot before clicking
        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_before_click.png`,
            fullPage: false,
        });

        // Verify first slide is showing
        const hero = page.locator('#hero');
        await expect(hero.getByText('Build Your Online Presence')).toBeVisible();

        // Click next arrow
        await page.click('button[aria-label="Next slide"]');
        await page.waitForTimeout(2000);

        // Take screenshot after clicking
        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_after_click.png`,
            fullPage: false,
        });

        // Check if a full page reload happened
        console.log(`Full page navigations after click: ${navigationCount}`);
        expect(navigationCount).toBe(0);
    });

    test('Auto-scroll should not cause full page reload', async ({ page }) => {
        let navigationCount = 0;

        page.on('load', () => {
            navigationCount++;
        });

        await page.goto('/');
        await page.waitForTimeout(1000);
        navigationCount = 0;

        // Wait for auto-scroll to trigger (5s interval + buffer)
        await page.waitForTimeout(6000);

        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_after_autoscroll.png`,
            fullPage: false,
        });

        console.log(`Full page navigations after auto-scroll: ${navigationCount}`);
        expect(navigationCount).toBe(0);
    });
});
