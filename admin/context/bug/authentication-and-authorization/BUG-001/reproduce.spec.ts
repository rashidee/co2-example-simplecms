import { test, expect } from '@playwright/test';

const BUG_FOLDER = 'admin/context/bug/authentication-and-authorization/BUG-001';

test('BUG-001: Reproduce 403 error after login', async ({ page }) => {
  // Step 1: Go to login page
  await page.goto('http://localhost:8080/login');
  await page.screenshot({
    path: `${BUG_FOLDER}/screenshot_login_page.png`,
    fullPage: true,
  });

  // Step 2: Enter valid email and password
  await page.fill('#email', 'admin@simplecms.com');
  await page.fill('#password', 'password');

  // Step 3: Click login button
  await page.click('button[type="submit"]');

  // Wait for navigation
  await page.waitForLoadState('networkidle');

  // Step 4: Capture the result - expecting 403 error
  await page.screenshot({
    path: `${BUG_FOLDER}/screenshot_reproduce.png`,
    fullPage: true,
  });

  // Check if we got a 403 error (bug reproduction)
  const pageContent = await page.content();
  const url = page.url();
  console.log('Current URL:', url);
  console.log('Page title:', await page.title());

  // The bug is confirmed if we see 403 or error page instead of dashboard
  const has403 = pageContent.includes('403') || pageContent.includes('Forbidden') || pageContent.includes('error');
  console.log('Has 403/error content:', has403);
});
