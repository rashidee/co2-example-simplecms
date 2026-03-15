import { test, expect } from '@playwright/test';

test.describe('Blog', () => {
  test('NAV-BLG-001: Blog link in navbar navigates to /blog', async ({ page }) => {
    await page.goto('/');
    await page.click('nav a[href="/blog"]');
    await expect(page).toHaveURL('/blog');
    await expect(page.locator('h1')).toContainText('Blog');
  });

  test('VIEW-BLG-001: Blog directory shows blog post cards', async ({ page }) => {
    await page.goto('/blog');
    await expect(page.locator('h1')).toContainText('Blog');
    const cards = page.locator('#blog-list .bg-surface');
    await expect(cards).toHaveCount(10);
  });

  test('VIEW-BLG-002: Blog card shows thumbnail, title, and summary', async ({ page }) => {
    await page.goto('/blog');
    const firstCard = page.locator('#blog-list .bg-surface').first();
    await expect(firstCard.locator('img')).toBeVisible();
    await expect(firstCard.locator('h2')).toContainText('The Future of AI in Web Development');
    await expect(firstCard).toContainText('Exploring how artificial intelligence');
  });

  test('VIEW-BLG-003: Blog detail page accessible via slug URL', async ({ page }) => {
    await page.goto('/the-future-of-ai-in-web-development');
    await expect(page.locator('h1')).toContainText('The Future of AI in Web Development');
  });

  test('VIEW-BLG-004: Blog detail page shows image, title, and content', async ({ page }) => {
    await page.goto('/the-future-of-ai-in-web-development');
    await expect(page.locator('article img')).toBeVisible();
    await expect(page.locator('article h1')).toContainText('The Future of AI in Web Development');
    await expect(page.locator('article')).toContainText('Artificial intelligence is transforming web development');
  });

  test('VIEW-BLG-005: Back to blog link returns to the directory', async ({ page }) => {
    await page.goto('/the-future-of-ai-in-web-development');
    const backLink = page.locator('a[href="/blog"]').filter({ hasText: 'Back to Blog' });
    await expect(backLink).toBeVisible();
    await backLink.click();
    await expect(page).toHaveURL('/blog');
  });

  test('PAGE-BLG-001: Pagination displays 10 posts per page', async ({ page }) => {
    await page.goto('/blog');
    const cards = page.locator('#blog-list .bg-surface');
    await expect(cards).toHaveCount(10);

    // Navigate to page 2
    await page.goto('/blog?page=2');
    const page2Cards = page.locator('#blog-list .bg-surface');
    await expect(page2Cards).toHaveCount(3);
  });

  test('VAL-BLG-001: Expired blog posts are not displayed', async ({ page }) => {
    await page.goto('/blog');
    const content = await page.locator('#blog-list').innerHTML();
    expect(content).not.toContain('Holiday Tech Gift Guide 2025');
    expect(content).not.toContain('Year-End Business Review Template');

    // Try accessing expired post directly - should 404
    const response = await page.goto('/holiday-tech-gift-guide-2025');
    expect(response?.status()).toBe(404);
  });

  test('VAL-BLG-002: Blog URLs use SEO-friendly slug format', async ({ page }) => {
    await page.goto('/blog');
    // Click first post
    await page.locator('#blog-list .bg-surface').first().locator('a').first().click();
    const url = page.url();
    expect(url).toMatch(/\/[a-z0-9-]+$/);
    expect(url).not.toMatch(/\?id=/);
  });
});
