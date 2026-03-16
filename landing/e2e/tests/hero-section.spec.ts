import { test, expect } from '@playwright/test';

test.describe('Hero Section', () => {

    test('VIEW-HRS-001: Verify hero carousel renders active slides', async ({ page }) => {
        await page.goto('/');

        // Hero section is visible
        const hero = page.locator('#hero');
        await expect(hero).toBeVisible();

        // First slide shows correct headline
        const headline = hero.locator('h1');
        await expect(headline.first()).toBeVisible();
        await expect(headline.first()).toHaveText('Build Your Online Presence');

        // CTA button is visible
        const cta = hero.locator('a.bg-primary, a[class*="bg-primary"]').first();
        await expect(cta).toBeVisible();
        await expect(cta).toHaveText('Get Started');

        // Expired slide should NOT be visible
        await expect(page.getByText('Expired Slide')).not.toBeVisible();
    });

    test('CAR-HRS-001: Carousel auto-advances every 5 seconds', async ({ page }) => {
        await page.goto('/');

        // Verify first slide
        const hero = page.locator('#hero');
        const headline = hero.locator('h1').first();
        await expect(headline).toHaveText('Build Your Online Presence');

        // Wait for auto-slide (5s + buffer)
        await page.waitForTimeout(5500);

        // Check headline changed to second slide
        const visibleHeadlines = hero.locator('h1');
        // One of the visible h1 elements should show the second slide
        await expect(hero.getByText('Engage Your Audience')).toBeVisible();
    });

    test('CAR-HRS-002: Manual navigation with arrow buttons', async ({ page }) => {
        await page.goto('/');

        const hero = page.locator('#hero');

        // Verify first slide
        await expect(hero.getByText('Build Your Online Presence')).toBeVisible();

        // Click next arrow
        await page.click('button[aria-label="Next slide"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('Engage Your Audience')).toBeVisible();

        // Click previous arrow
        await page.click('button[aria-label="Previous slide"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('Build Your Online Presence')).toBeVisible();

        // Click dot indicator for slide 3
        await page.click('button[aria-label="Go to slide 3"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('Grow Your Business')).toBeVisible();
    });

    test('CONS-HRS-001: No header/footer in hero section', async ({ page }) => {
        await page.goto('/');

        const hero = page.locator('#hero');
        await expect(hero).toBeVisible();

        // No header or footer elements within hero section
        await expect(hero.locator('header')).toHaveCount(0);
        await expect(hero.locator('footer')).toHaveCount(0);
    });
});
