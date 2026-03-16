import { test, expect } from '@playwright/test';

test.describe('Team Section', () => {

    test('VIEW-TMS-001: Verify team section renders all members', async ({ page }) => {
        await page.goto('/');

        const section = page.locator('#team');
        await expect(section).toBeVisible();

        // Section heading (NFRL00051)
        await expect(section.locator('h2')).toHaveText('Meet Our Team');

        // 4 team member cards visible
        const cards = section.locator('.grid > div');
        await expect(cards).toHaveCount(4);

        // Cards ordered by display_order
        await expect(cards.first().locator('h3')).toHaveText('Alex Johnson');
        await expect(cards.nth(1).locator('h3')).toHaveText('Samantha Kim');
        await expect(cards.nth(2).locator('h3')).toHaveText('Marcus Rivera');
        await expect(cards.nth(3).locator('h3')).toHaveText('Olivia Wang');

        // Light gray background (NFRL00069)
        await expect(section).toHaveClass(/bg-page-bg/);
    });

    test('LAY-TMS-001: 3-column desktop, 1-column mobile', async ({ page }) => {
        // Desktop viewport
        await page.setViewportSize({ width: 1280, height: 720 });
        await page.goto('/');

        const grid = page.locator('#team .grid');
        await expect(grid).toBeVisible();

        // Check that grid has md:grid-cols-3 class (NFRL00054)
        await expect(grid).toHaveClass(/md:grid-cols-3/);

        // Cards have shadow effect (NFRL00069)
        const firstCard = grid.locator('> div').first();
        await expect(firstCard).toHaveClass(/shadow/);
    });

    test('CONT-TMS-001: Team member card shows initials, name, role, LinkedIn', async ({ page }) => {
        await page.goto('/');

        const firstCard = page.locator('#team .grid > div').first();
        await expect(firstCard).toBeVisible();

        // Circular profile placeholder with initials (no image_data seeded)
        const initialsDiv = firstCard.locator('div.rounded-full');
        await expect(initialsDiv).toBeVisible();
        await expect(initialsDiv).toContainText('AJ');

        // Name "Alex Johnson" in bold, text-lg (NFRL00060)
        const nameEl = firstCard.locator('h3');
        await expect(nameEl).toHaveText('Alex Johnson');
        await expect(nameEl).toHaveClass(/font-bold/);

        // Role "Founder & CEO" (NFRL00063)
        const roleEl = firstCard.locator('p');
        await expect(roleEl).toHaveText('Founder & CEO');

        // LinkedIn link (NFRL00066)
        const linkedinLink = firstCard.locator('a[aria-label="Alex Johnson on LinkedIn"]');
        await expect(linkedinLink).toBeVisible();
        await expect(linkedinLink).toHaveAttribute('target', '_blank');
        await expect(linkedinLink).toHaveAttribute('href', /linkedin\.com\/in\/alex-johnson-test/);

        // LinkedIn SVG icon at least 24px (w-6 h-6)
        const svgIcon = linkedinLink.locator('svg');
        await expect(svgIcon).toHaveClass(/w-6/);
        await expect(svgIcon).toHaveClass(/h-6/);
    });
});
