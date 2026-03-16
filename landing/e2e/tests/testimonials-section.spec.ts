import { test, expect } from '@playwright/test';

test.describe('Testimonials Section', () => {

    test('VIEW-TST-001: Verify testimonials section renders', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#testimonials');
        await expect(section).toBeVisible();

        // Section heading (NFRL00033)
        await expect(section.locator('h2')).toHaveText('What Our Customers Say');

        // White background (NFRL00048)
        await expect(section).toHaveClass(/bg-white/);

        // Wait for Alpine.js to render
        await page.waitForTimeout(500);

        // First testimonial is visible (Sarah Johnson) — use visible filter since x-for renders all
        const visibleSlide = section.locator('div[x-show="current === idx"]:visible');
        await expect(visibleSlide).toBeVisible();

        // Star rating shows filled stars
        const stars = visibleSlide.locator('svg.w-6.h-6');
        await expect(stars.first()).toBeVisible();

        // Customer name is displayed
        const nameEl = visibleSlide.locator('p.font-bold');
        await expect(nameEl).toBeVisible();
        await expect(nameEl).toContainText('Sarah Johnson');

        // Review text is displayed
        const reviewEl = visibleSlide.locator('p.italic');
        await expect(reviewEl).toBeVisible();
        await expect(reviewEl).toContainText('Simple CMS transformed our online presence');
    });

    test('CAR-TST-001: Navigate between testimonials with arrows', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#testimonials');
        await section.scrollIntoViewIfNeeded();

        // Wait for Alpine.js to initialize
        await page.waitForTimeout(500);

        // First testimonial: Sarah Johnson
        const getVisibleName = () => section.locator('div[x-show="current === idx"]:visible p.font-bold');
        await expect(getVisibleName()).toContainText('Sarah Johnson');

        // Click next arrow
        const nextBtn = section.locator('button[aria-label="Next testimonial"]');
        await nextBtn.click();
        await page.waitForTimeout(600);

        // Should now show Michael Chen
        await expect(getVisibleName()).toContainText('Michael Chen');

        // Click previous arrow
        const prevBtn = section.locator('button[aria-label="Previous testimonial"]');
        await prevBtn.click();
        await page.waitForTimeout(600);

        // Should return to Sarah Johnson
        await expect(getVisibleName()).toContainText('Sarah Johnson');
    });

    test('CONT-TST-001: Star rating and text formatting', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#testimonials');
        await section.scrollIntoViewIfNeeded();

        // Wait for Alpine.js
        await page.waitForTimeout(500);

        const visibleSlide = section.locator('div[x-show="current === idx"]:visible');

        // Customer name is bold, at least 18px (NFRL00039) — text-lg font-bold
        const nameEl = visibleSlide.locator('p.font-bold');
        await expect(nameEl).toHaveClass(/text-lg/);
        await expect(nameEl).toHaveClass(/font-bold/);

        // Review text at least 16px (NFRL00042) — text-base
        const reviewEl = visibleSlide.locator('p.italic');
        await expect(reviewEl).toHaveClass(/text-base/);

        // Navigate to Emily Rodriguez (4 stars) - she is at index 2, so click next twice
        const nextBtn = section.locator('button[aria-label="Next testimonial"]');
        await nextBtn.click();
        await page.waitForTimeout(600);
        await nextBtn.click();
        await page.waitForTimeout(600);

        const visibleSlide2 = section.locator('div[x-show="current === idx"]:visible');
        await expect(visibleSlide2.locator('p.font-bold')).toContainText('Emily Rodriguez');

        // Verify star count: 4 filled (text-warning), 1 empty (text-gray-300)
        const filledStars = visibleSlide2.locator('svg.text-warning');
        const emptyStars = visibleSlide2.locator('svg.text-gray-300');
        await expect(filledStars).toHaveCount(4);
        await expect(emptyStars).toHaveCount(1);
    });
});
