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
        await expect(headline.first()).toHaveText('This Is a Sample Website Generated Using CO2');

        // CTA button is visible
        const cta = hero.locator('a.bg-primary, a[class*="bg-primary"]').first();
        await expect(cta).toBeVisible();
        await expect(cta).toHaveText('Explore CO2 on GitHub');
    });

    test('CAR-HRS-001: Carousel does not auto-advance (manual navigation only)', async ({ page }) => {
        await page.goto('/');

        // Verify first slide
        const hero = page.locator('#hero');
        const headline = hero.locator('h1').first();
        await expect(headline).toHaveText('This Is a Sample Website Generated Using CO2');

        // Wait longer than old auto-slide interval
        await page.waitForTimeout(6000);

        // First slide should still be visible (no auto-advance)
        await expect(hero.getByText('This Is a Sample Website Generated Using CO2')).toBeVisible();
    });

    test('CAR-HRS-002: Manual navigation with arrow buttons', async ({ page }) => {
        await page.goto('/');

        const hero = page.locator('#hero');

        // Verify first slide
        await expect(hero.getByText('This Is a Sample Website Generated Using CO2')).toBeVisible();

        // Click next arrow
        await page.click('button[aria-label="Next slide"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('Design Landing Pages That Convert')).toBeVisible();

        // Click previous arrow
        await page.click('button[aria-label="Previous slide"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('This Is a Sample Website Generated Using CO2')).toBeVisible();

        // Click dot indicator for slide 3
        await page.click('button[aria-label="Go to slide 3"]');
        await page.waitForTimeout(600);
        await expect(hero.getByText('From Idea to Launch in Minutes')).toBeVisible();
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
