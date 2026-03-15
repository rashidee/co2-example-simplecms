import { test, expect } from '@playwright/test';

test.describe('Contact Section', () => {
  test('NAV-CTS-001: Contact section is visible on the landing page', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#contact');
    await expect(section).toBeVisible();
    await expect(section.locator('h2')).toContainText('Contact Us');
  });

  test('VIEW-CTS-001: Contact information is displayed correctly', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#contact');
    await expect(section).toContainText('+1 (555) 123-4567');
    await expect(section).toContainText('info@simplecms.com');
    await expect(section).toContainText('123 Innovation Drive');
  });

  test('CRUD-CTS-001: Submit contact form successfully', async ({ page }) => {
    await page.goto('/');
    const section = page.locator('#contact');

    await section.locator('#sender_name').fill('Test User');
    await section.locator('#sender_email').fill('testuser@example.com');
    await section.locator('#message_content').fill('This is a test message for the contact form.');

    await section.locator('button[type="submit"]').click();

    // Wait for htmx response
    await page.waitForTimeout(2000);
    const result = page.locator('#contact-form-result');
    await expect(result).toContainText('Thank you');
  });

  test('VAL-CTS-001: Required fields validation', async ({ page }) => {
    await page.goto('/');
    const form = page.locator('#contact form');

    // HTML5 required validation - check fields have required attribute
    await expect(form.locator('#sender_name')).toHaveAttribute('required', '');
    await expect(form.locator('#sender_email')).toHaveAttribute('required', '');
    await expect(form.locator('#message_content')).toHaveAttribute('required', '');
  });

  test('VAL-CTS-002: Email format validation', async ({ page }) => {
    await page.goto('/');
    // HTML5 email type validation
    const emailField = page.locator('#sender_email');
    await expect(emailField).toHaveAttribute('type', 'email');
  });

  test('VAL-CTS-003: Message field maximum 500 characters', async ({ page }) => {
    await page.goto('/');
    const messageField = page.locator('#message_content');
    await expect(messageField).toHaveAttribute('maxlength', '500');

    // Character counter - type instead of fill to trigger keyup
    await messageField.press('H');
    await messageField.press('e');
    await messageField.press('l');
    await messageField.press('l');
    await messageField.press('o');
    await page.waitForTimeout(200);
    await expect(page.locator('#contact')).toContainText('5/500');
  });

  test('VAL-CTS-004: CAPTCHA placeholder check', async ({ page }) => {
    // CAPTCHA is specified as an NFR but not implemented in v1.0.0
    // This test verifies the form exists and is functional without CAPTCHA blocking
    await page.goto('/');
    const form = page.locator('#contact form');
    await expect(form).toBeVisible();
    await expect(form.locator('button[type="submit"]')).toBeVisible();
  });
});
