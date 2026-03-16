import { test, expect } from '@playwright/test';

const BUG_FOLDER = 'context/bug/hero-section/BUG-002';

test.describe('BUG-002: Carousel refresh and non-smooth rendering', () => {

    test('Detect full page refreshes during auto-scroll', async ({ page }) => {
        let navigationCount = 0;
        page.on('load', () => { navigationCount++; });

        await page.goto('/');
        navigationCount = 0; // reset after initial load

        // Take initial screenshot
        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_initial.png`,
            fullPage: false,
        });

        // Wait for 2 auto-scroll cycles (>10 seconds)
        await page.waitForTimeout(11000);

        // Take screenshot after auto-scroll
        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_after_autoscroll.png`,
            fullPage: false,
        });

        console.log(`Page navigations during auto-scroll: ${navigationCount}`);
        expect(navigationCount).toBe(0);
    });

    test('Check for layout shift during slide transition', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        // Get hero section dimensions before transition
        const heroBefore = await page.locator('#hero').boundingBox();

        // Get currently visible headline
        const headlineBefore = await page.locator('#hero h1:visible').textContent();

        // Click next
        await page.click('#hero button[aria-label="Next slide"]');
        await page.waitForTimeout(800); // wait for transition

        // Get hero section dimensions after transition
        const heroAfter = await page.locator('#hero').boundingBox();

        // Get new headline
        const headlineAfter = await page.locator('#hero h1:visible').textContent();

        console.log(`Before: height=${heroBefore?.height}, headline="${headlineBefore}"`);
        console.log(`After: height=${heroAfter?.height}, headline="${headlineAfter}"`);

        // Take screenshot after manual navigation
        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_after_manual_nav.png`,
            fullPage: false,
        });

        // Headline should have changed
        expect(headlineAfter).not.toBe(headlineBefore);

        // Hero section height should remain stable (no layout shift)
        if (heroBefore && heroAfter) {
            expect(Math.abs(heroAfter.height - heroBefore.height)).toBeLessThan(10);
        }
    });

    test('Observe transition smoothness - check for stacking/flicker', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        // Count how many slide divs are visible at any point during transition
        // Click next and immediately check
        await page.click('#hero button[aria-label="Next slide"]');

        // During transition, check how many slides are visible
        await page.waitForTimeout(100); // mid-transition
        const visibleSlides = await page.locator('#hero template + div').evaluateAll(
            (els) => els.filter(el => window.getComputedStyle(el).display !== 'none').length
        );

        console.log(`Visible slides during transition: ${visibleSlides}`);

        // Check if slides are positioned absolutely (overlapping) or relatively (stacking)
        const positioning = await page.evaluate(() => {
            const heroSection = document.getElementById('hero');
            if (!heroSection) return 'no hero section';
            const slideDivs = heroSection.querySelectorAll(':scope > template + div, :scope > div[x-show]');
            const positions = Array.from(slideDivs).map(el => {
                const style = window.getComputedStyle(el);
                return { position: style.position, display: style.display, height: style.height };
            });
            return JSON.stringify(positions, null, 2);
        });

        console.log(`Slide positioning: ${positioning}`);

        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_transition_check.png`,
            fullPage: false,
        });
    });

    test('Monitor network requests during carousel transitions', async ({ page }) => {
        const requests: string[] = [];
        page.on('request', (req) => {
            // Ignore image requests which are expected
            if (!req.url().includes('/images/')) {
                requests.push(`${req.method()} ${req.url()}`);
            }
        });

        await page.goto('/');
        requests.length = 0; // reset after initial load

        // Wait for auto-scroll
        await page.waitForTimeout(6000);

        // Click navigation
        await page.click('#hero button[aria-label="Next slide"]');
        await page.waitForTimeout(1000);

        console.log(`Network requests during carousel use: ${requests.length}`);
        requests.forEach(r => console.log(`  ${r}`));

        // No page navigation requests expected
        const pageNavigations = requests.filter(r => r.includes('GET http://localhost:8000/') && !r.includes('/images/'));
        expect(pageNavigations.length).toBe(0);
    });
});
