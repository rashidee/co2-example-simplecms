import { test, expect } from '@playwright/test';

test('BUG-010: Cards do not support dark mode', async ({ page }) => {
  // Login as admin
  await page.goto('http://localhost:8080/login');
  await page.fill('input[name="username"]', 'admin@simplecms.com');
  await page.fill('input[name="password"]', 'password');
  await page.click('button[type="submit"]');
  await page.waitForURL('**/dashboard');

  // Enable dark mode
  await page.click('[x-data] button[\\@click*="theme.toggle"]');
  await page.waitForTimeout(500);

  // Screenshot the dashboard in dark mode
  await page.screenshot({
    path: 'admin/context/bug/common/BUG-010/screenshot_reproduce_dashboard.png',
    fullPage: true
  });

  // Navigate to Hero Section list page
  await page.goto('http://localhost:8080/hero-section');
  await page.waitForTimeout(500);
  await page.screenshot({
    path: 'admin/context/bug/common/BUG-010/screenshot_reproduce_herosection.png',
    fullPage: true
  });

  // Navigate to Features Section list page
  await page.goto('http://localhost:8080/features-section');
  await page.waitForTimeout(500);
  await page.screenshot({
    path: 'admin/context/bug/common/BUG-010/screenshot_reproduce_features.png',
    fullPage: true
  });

  // Check for dark mode issues - cards should have dark backgrounds
  // The bug is that cards use hardcoded bg-white without dark: variants
  const cards = page.locator('.bg-white');
  const cardCount = await cards.count();
  console.log(`Found ${cardCount} elements with bg-white class (should have dark mode variants)`);
});
