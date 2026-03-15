import { test, expect } from '@playwright/test';

test.describe('Hero Section', () => {
  test('NAV-HRS-001: Hero section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const heroSection = page.locator('#hero');
    await expect(heroSection).toBeVisible();
    // Verify hero contains an image, headline, sub-headline, and CTA button
    await expect(heroSection.locator('img').first()).toBeVisible();
    await expect(heroSection.locator('h1').first()).toBeVisible();
    await expect(heroSection.locator('p').first()).toBeVisible();
    await expect(heroSection.locator('a').first()).toBeVisible();
  });

  test('VIEW-HRS-001: Carousel displays all active hero slides', async ({ page }) => {
    await page.goto('/');
    const heroSection = page.locator('#hero');

    // First slide should show "Innovate Your Business Today"
    await expect(heroSection.locator('h1').first()).toContainText('Innovate Your Business Today');
    await expect(heroSection.locator('a').first()).toContainText('Explore Products');

    // Click next to go to second slide
    await page.click('[aria-label="Next slide"]');
    await page.waitForTimeout(600);
    await expect(heroSection.locator('div[x-show="current === 1"] h1')).toContainText('Trusted by Industry Leaders');

    // Click next to go to third slide
    await page.click('[aria-label="Next slide"]');
    await page.waitForTimeout(600);
    await expect(heroSection.locator('div[x-show="current === 2"] h1')).toContainText('Start Your Free Trial');
  });

  test('VIEW-HRS-002: Carousel auto-slides every 5 seconds', async ({ page }) => {
    await page.goto('/');
    const heroSection = page.locator('#hero');

    // First slide visible
    await expect(heroSection.locator('h1').first()).toContainText('Innovate Your Business Today');

    // Wait 5.5 seconds for auto-slide
    await page.waitForTimeout(5500);
    // Second slide should now be visible
    const secondSlide = heroSection.locator('div[x-show="current === 1"]');
    await expect(secondSlide).toBeVisible();
    await expect(secondSlide.locator('h1')).toContainText('Trusted by Industry Leaders');
  });

  test('VIEW-HRS-003: Arrow navigation allows manual slide control', async ({ page }) => {
    await page.goto('/');

    // Click next arrow
    await page.click('[aria-label="Next slide"]');
    await page.waitForTimeout(600);
    await expect(page.locator('#hero div[x-show="current === 1"] h1')).toContainText('Trusted by Industry Leaders');

    // Click next again
    await page.click('[aria-label="Next slide"]');
    await page.waitForTimeout(600);
    await expect(page.locator('#hero div[x-show="current === 2"] h1')).toContainText('Start Your Free Trial');

    // Click previous
    await page.click('[aria-label="Previous slide"]');
    await page.waitForTimeout(600);
    await expect(page.locator('#hero div[x-show="current === 1"] h1')).toContainText('Trusted by Industry Leaders');
  });

  test('VIEW-HRS-004: CTA button opens link in a new tab', async ({ page }) => {
    await page.goto('/');
    const ctaButton = page.locator('#hero div[x-show="current === 0"] a');
    await expect(ctaButton).toHaveAttribute('target', '_blank');
    await expect(ctaButton).toHaveAttribute('href', 'https://example.com/products');
  });

  test('VAL-HRS-001: Expired hero section is not displayed', async ({ page }) => {
    await page.goto('/');

    // Navigate through all slides - should only find 3
    const heroSection = page.locator('#hero');
    const dotButtons = heroSection.locator('button[aria-label^="Go to slide"]');
    await expect(dotButtons).toHaveCount(3);

    // Verify "Holiday Special Offer" does not appear
    const pageContent = await page.locator('#hero').innerHTML();
    expect(pageContent).not.toContain('Holiday Special Offer');
  });
});
