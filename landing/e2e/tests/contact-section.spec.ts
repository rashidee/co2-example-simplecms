import { test, expect } from '@playwright/test';

test.describe('Contact Section', () => {

    test('VIEW-CTS-001: Verify contact info displays phone, email, address', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#contact');
        await expect(section).toBeVisible();

        // Section heading
        await expect(section.locator('h2')).toHaveText('Contact Us');

        // Phone number
        await expect(section.getByText('+1 (555) 123-4567')).toBeVisible();

        // Email
        await expect(section.getByText('hello@simplecms.example.com')).toBeVisible();

        // Address
        await expect(section.getByText('123 Innovation Drive, Suite 400')).toBeVisible();

        // White background
        await expect(section).toHaveClass(/bg-white/);
    });

    test('CRUD-CTS-001: Submit contact form message successfully', async ({ page }) => {
        await page.goto('/');

        // Fill form
        await page.fill('input[name="sender_name"]', 'Test User');
        await page.fill('input[name="sender_email"]', 'testuser@test.example.com');
        await page.fill('textarea[name="message_content"]', 'This is a test message from E2E testing.');

        // Submit
        await page.click('button:has-text("Send Message")');

        // Wait for toast notification
        await expect(page.getByText('Thank you! Your message has been sent successfully.')).toBeVisible({ timeout: 10000 });
    });

    test('VAL-CTS-001: Required field validation on contact form', async ({ page }) => {
        await page.goto('/');

        // Check that all required fields have the required attribute
        await expect(page.locator('input[name="sender_name"]')).toHaveAttribute('required');
        await expect(page.locator('input[name="sender_email"]')).toHaveAttribute('required');
        await expect(page.locator('textarea[name="message_content"]')).toHaveAttribute('required');
    });

    test('VAL-CTS-002: Message maximum 500 characters', async ({ page }) => {
        await page.goto('/');

        // Check maxlength attribute
        await expect(page.locator('textarea[name="message_content"]')).toHaveAttribute('maxlength', '500');
    });

    test('CAPTCHA placeholder is present', async ({ page }) => {
        await page.goto('/');

        await expect(page.getByText('CAPTCHA verification will be integrated here')).toBeVisible();
    });
});
