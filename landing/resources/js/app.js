import Alpine from 'alpinejs';
import htmx from 'htmx.org';

// Alpine stores
import { toastStore } from './alpine/stores/toast.js';

// Alpine components
import { carousel } from './alpine/components/carousel.js';

// Register stores and components
Alpine.data('toastStore', toastStore);
Alpine.data('carousel', carousel);

// Start Alpine
Alpine.start();

// Make htmx available globally
window.htmx = htmx;

// Listen for htmx HX-Trigger "showToast" events dispatched by htmx
document.addEventListener('showToast', (event) => {
    const data = event.detail;
    const toastEl = document.querySelector('[x-data="toastStore"]');
    if (toastEl && toastEl._x_dataStack) {
        toastEl._x_dataStack[0].add(data);
    }
});

// htmx global error handler
document.addEventListener('htmx:responseError', (event) => {
    const xhr = event.detail.xhr;
    try {
        const trigger = JSON.parse(xhr.getResponseHeader('HX-Trigger'));
        if (trigger?.showToast) {
            const toastEl = document.querySelector('[x-data="toastStore"]');
            if (toastEl && toastEl._x_dataStack) {
                toastEl._x_dataStack[0].add(trigger.showToast);
            }
        }
    } catch {
        // Non-JSON trigger, ignore
    }
});
