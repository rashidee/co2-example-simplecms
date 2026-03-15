import { test, expect } from '@playwright/test';

test.describe('Team Section', () => {
  test('NAV-TMS-001: Team section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#team');
    await expect(section).toBeVisible();
    await expect(section.locator('h2')).toContainText('Meet Our Team');
  });

  test('VIEW-TMS-001: Team cards display photo, name, and role', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#team');
    const cards = section.locator('.grid > div');
    await expect(cards).toHaveCount(6);

    const firstCard = cards.first();
    await expect(firstCard.locator('img')).toBeVisible();
    await expect(firstCard.locator('h3')).toContainText('John Doe');
    await expect(firstCard.locator('p')).toContainText('Chief Executive Officer');
  });

  test('VIEW-TMS-002: LinkedIn links open in a new tab', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#team');
    const firstLink = section.locator('a[href*="linkedin"]').first();
    await expect(firstLink).toHaveAttribute('target', '_blank');
    await expect(firstLink).toHaveAttribute('href', 'https://linkedin.com/in/johndoe');
  });

  test('VIEW-TMS-003: Profile photos are displayed as circular images', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#team');
    const photos = section.locator('img');
    await expect(photos).toHaveCount(6);
    // First photo should have rounded-full class
    await expect(photos.first()).toHaveClass(/rounded-full/);
  });
});
