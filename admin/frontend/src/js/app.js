import Alpine from 'alpinejs';
import htmx from 'htmx.org';
import '../css/app.css';

// Initialize Alpine.js
window.Alpine = Alpine;
Alpine.start();

// Initialize htmx
window.htmx = htmx;

// Configure htmx defaults
document.body.addEventListener('htmx:configRequest', (event) => {
    // Include CSRF token in htmx requests
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
    if (csrfToken && csrfHeader) {
        event.detail.headers[csrfHeader.content] = csrfToken.content;
    }
});
