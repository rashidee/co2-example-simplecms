import { test, expect } from '@playwright/test';

test.describe('Testimonials Section', () => {
  test('NAV-TST-001: Testimonials section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#testimonials');
    await expect(section).toBeVisible();
    await expect(section.locator('h2')).toContainText('What Our Customers Say');
  });

  test('VIEW-TST-001: Carousel displays testimonials one per slide', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#testimonials');
    // 5 dot indicators = 5 slides
    const dots = section.locator('button[aria-label^="Go to testimonial"]');
    await expect(dots).toHaveCount(5);
  });

  test('VIEW-TST-002: Testimonial shows customer name, review, and star rating', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#testimonials');
    // First slide visible
    const firstSlide = section.locator('div[x-show="current === 0"]');
    await expect(firstSlide).toBeVisible();
    await expect(firstSlide).toContainText('Sarah Johnson');
    await expect(firstSlide).toContainText('Absolutely transformed our business operations');
    // Star rating component should be present
    await expect(firstSlide.locator('svg').first()).toBeVisible();
  });

  test('VIEW-TST-003: Star rating visual matches the numeric rating value', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#testimonials');

    // First slide - Sarah Johnson, rating 5: 5 filled stars
    const firstSlide = section.locator('div[x-show="current === 0"]');
    const filledStars = firstSlide.locator('svg.text-warning');
    await expect(filledStars).toHaveCount(5);

    // Navigate to second slide - Michael Chen, rating 4
    await page.click('#testimonials [aria-label="Next testimonial"]');
    await page.waitForTimeout(400);
    const secondSlide = section.locator('div[x-show="current === 1"]');
    const filledStars2 = secondSlide.locator('svg.text-warning');
    await expect(filledStars2).toHaveCount(4);
    const emptyStars2 = secondSlide.locator('svg.text-gray-300');
    await expect(emptyStars2).toHaveCount(1);
  });
});
