import Alpine from 'alpinejs';
import htmx from 'htmx.org';

window.Alpine = Alpine;
window.htmx = htmx;

// Alpine stores
Alpine.store('toast', {
  messages: [],
  add(type, text, duration = 5000) {
    const id = Date.now();
    this.messages.push({ id, type, text });
    setTimeout(() => this.remove(id), duration);
  },
  remove(id) {
    this.messages = this.messages.filter(m => m.id !== id);
  },
});

Alpine.store('theme', {
  dark: localStorage.getItem('darkMode') === 'true',
  toggle() {
    this.dark = !this.dark;
    localStorage.setItem('darkMode', this.dark);
    document.documentElement.classList.toggle('dark', this.dark);
  },
  init() {
    document.documentElement.classList.toggle('dark', this.dark);
  },
});

Alpine.store('sidebar', {
  collapsed: false,
  toggle() {
    this.collapsed = !this.collapsed;
  },
});

Alpine.start();

// htmx extensions
document.body.addEventListener('htmx:afterRequest', (event) => {
  const xhr = event.detail.xhr;
  if (xhr && xhr.status >= 400) {
    Alpine.store('toast').add('error', 'An error occurred. Please try again.');
  }
});

document.body.addEventListener('htmx:responseError', () => {
  Alpine.store('toast').add('error', 'Server error. Please try again later.');
});
