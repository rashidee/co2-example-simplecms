# Test Spec — BUG-001

**Bug**: 403 error after successful login
**Module**: Authentication and Authorization
**Reproduced**: Yes

---

## Pre-Conditions

- Admin portal running on http://localhost:8080
- Valid credentials: admin@simplecms.com / password
- User exists in database with ACTIVE status

## Steps to Verify Fix

1. Navigate to http://localhost:8080/login
2. Enter email: admin@simplecms.com
3. Enter password: password
4. Click "Log In" button
5. Assert that the user is redirected to the dashboard/home page (not a 403 error page)
6. Assert that the page contains authenticated content (sidebar, header, etc.)

## Expected Result After Fix

- User should be redirected to the home/dashboard page after successful login
- No 403 Forbidden error should appear
- The page should display the authenticated layout with sidebar navigation

## Playwright Verification Script

```typescript
test('BUG-001: Login should succeed without 403 error', async ({ page }) => {
  await page.goto('http://localhost:8080/login');
  await page.fill('#email', 'admin@simplecms.com');
  await page.fill('#password', 'password');
  await page.click('button[type="submit"]');
  await page.waitForLoadState('networkidle');

  // Should NOT be on error page
  const content = await page.content();
  expect(content).not.toContain('status=403');
  expect(content).not.toContain('Whitelabel Error Page');

  // Should be on authenticated page
  expect(page.url()).not.toContain('/login');

  await page.screenshot({
    path: 'admin/context/bug/authentication-and-authorization/BUG-001/screenshot_fixed.png',
    fullPage: true,
  });
});
```
