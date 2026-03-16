import { test, expect } from '@playwright/test';

const BUG_FOLDER = 'context/bug/hero-section/BUG-002';

test.describe('BUG-002 Verification: Smooth carousel without auto-scroll', () => {

    test('No auto-scroll after 6 seconds', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        const headlineInitial = await page.locator('#hero h1:visible').textContent();
        await page.waitForTimeout(6000);
        const headlineAfter6s = await page.locator('#hero h1:visible').textContent();

        expect(headlineAfter6s).toBe(headlineInitial);
        console.log(`Headline stayed: "${headlineInitial}" — no auto-scroll`);
    });

    test('Smooth transition without layout shift on next click', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        const heroBefore = await page.locator('#hero').boundingBox();
        const headlineBefore = await page.locator('#hero h1:visible').textContent();

        await page.click('#hero button[aria-label="Next slide"]');
        await page.waitForTimeout(600);

        const heroAfter = await page.locator('#hero').boundingBox();
        const headlineAfter = await page.locator('#hero h1:visible').textContent();

        // Headline changed
        expect(headlineAfter).not.toBe(headlineBefore);
        // No layout shift
        expect(Math.abs(heroAfter!.height - heroBefore!.height)).toBeLessThan(5);

        console.log(`Height before: ${heroBefore?.height}, after: ${heroAfter?.height}`);
        console.log(`Headline changed: "${headlineBefore}" → "${headlineAfter}"`);
    });

    test('No stacking during transition - slides use absolute positioning', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        // Check that slide container uses proper positioning
        const slidesPositioning = await page.evaluate(() => {
            const hero = document.getElementById('hero');
            if (!hero) return 'no hero';
            // Find the slides container (first child div with relative positioning)
            const container = hero.querySelector('.relative.w-full.h-\\[500px\\]');
            if (!container) return 'no container';

            const containerStyle = window.getComputedStyle(container);
            const slides = container.querySelectorAll(':scope > template + div, :scope > div');
            const slideInfo = Array.from(slides).map(el => {
                const style = window.getComputedStyle(el);
                return { position: style.position, display: style.display };
            });
            return JSON.stringify({ container: containerStyle.position, slides: slideInfo }, null, 2);
        });

        console.log(`Positioning: ${slidesPositioning}`);

        // Click next and check height during transition
        await page.click('#hero button[aria-label="Next slide"]');
        await page.waitForTimeout(100); // mid-transition

        const heroScrollHeight = await page.locator('#hero').evaluate(el => el.scrollHeight);
        console.log(`Hero scrollHeight during transition: ${heroScrollHeight}`);

        // Should not be double-height (stacking would cause ~1000px)
        expect(heroScrollHeight).toBeLessThan(600);

        await page.screenshot({
            path: `${BUG_FOLDER}/screenshot_fixed.png`,
            fullPage: false,
        });
    });

    test('Previous button and dot indicators work smoothly', async ({ page }) => {
        await page.goto('/');
        await page.waitForSelector('#hero');

        // Test prev button
        const headline0 = await page.locator('#hero h1:visible').textContent();
        await page.click('#hero button[aria-label="Previous slide"]');
        await page.waitForTimeout(600);
        const headlinePrev = await page.locator('#hero h1:visible').textContent();
        expect(headlinePrev).not.toBe(headline0);

        // Test dot indicator
        await page.click('#hero button[aria-label="Go to slide 1"]');
        await page.waitForTimeout(600);
        const headlineDot = await page.locator('#hero h1:visible').textContent();
        expect(headlineDot).toBe(headline0);

        console.log('Prev and dot navigation work correctly');
    });
});
