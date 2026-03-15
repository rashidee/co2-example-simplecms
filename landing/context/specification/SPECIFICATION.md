# Landing Page - Shared Infrastructure Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Application:** Landing Page (Simple CMS)
> **Slug:** landing-page

---

## 1. Project Overview

### 1.1 Metadata

| Field | Value |
|-------|-------|
| Project Code | SCMS |
| Application Name | Landing Page |
| Application Slug | landing-page |
| Application Type | Public-facing read-only website (with contact form write) |
| Authentication | NONE (CONSL0003) |
| Roles | Public (anonymous visitor) |
| Database | PostgreSQL 17.x (shared with Admin Portal) |
| Scheduling | No |
| Messaging | No |
| Reporting | No |

### 1.2 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Backend Framework | Laravel | 12.x |
| Language | PHP | 8.3+ |
| Template Engine | Blade | (bundled with Laravel) |
| CSS Framework | Tailwind CSS | 4.x |
| JavaScript (reactive) | Alpine.js | 3.x |
| JavaScript (AJAX) | htmx | 2.x |
| Build Tool | Vite | 6.x |
| Database | PostgreSQL | 17.x |
| Modular Architecture | nwidart/laravel-modules | latest |
| Data Transfer | spatie/laravel-data | latest |
| View Models | spatie/laravel-view-models | latest |

### 1.3 Module Index

| # | Module | Directory (nwidart) | Access Mode | Source Stories |
|---|--------|---------------------|-------------|----------------|
| 1 | Hero Section | Modules/HeroSection | Read-only | USL000003 |
| 2 | Product and Service Section | Modules/ProductAndServiceSection | Read-only | USL000006 |
| 3 | Features Section | Modules/FeaturesSection | Read-only | USL000009 |
| 4 | Testimonials Section | Modules/TestimonialsSection | Read-only | USL000012 |
| 5 | Team Section | Modules/TeamSection | Read-only | USL000015 |
| 6 | Contact Section | Modules/ContactSection | Read-only + Write (form) | USL000018 |
| 7 | Blog | Modules/Blog | Read-only | USL000021, USL000024 |

---

## 2. Composer Configuration

### 2.1 composer.json (key dependencies)

```json
{
    "name": "simplecms/landing-page",
    "type": "project",
    "description": "Simple CMS Landing Page - Public-facing marketing and blog site",
    "require": {
        "php": "^8.3",
        "laravel/framework": "^12.0",
        "nwidart/laravel-modules": "^11.0",
        "spatie/laravel-data": "^4.0",
        "spatie/laravel-view-models": "^1.6"
    },
    "require-dev": {
        "fakerphp/faker": "^1.23",
        "laravel/pail": "^1.2",
        "laravel/pint": "^1.18",
        "laravel/sail": "^1.41",
        "mockery/mockery": "^1.6",
        "nunomaduro/collision": "^8.6",
        "phpunit/phpunit": "^11.5"
    }
}
```

**Notes:**
- `nwidart/laravel-modules` provides the modular architecture. Each business module is a self-contained Laravel module.
- `spatie/laravel-data` is used for data transfer objects (DTOs) that pass data from services to views.
- `spatie/laravel-view-models` is used to encapsulate view logic in dedicated ViewModel classes.
- No authentication packages are required (CONSL0003).

---

## 3. npm Configuration

### 3.1 package.json (key dependencies)

```json
{
    "private": true,
    "type": "module",
    "scripts": {
        "dev": "vite",
        "build": "vite build"
    },
    "devDependencies": {
        "@tailwindcss/vite": "^4.0",
        "tailwindcss": "^4.0",
        "vite": "^6.0",
        "laravel-vite-plugin": "^1.2"
    },
    "dependencies": {
        "alpinejs": "^3.14",
        "htmx.org": "^2.0"
    }
}
```

---

## 4. Environment Configuration

### 4.1 .env

```dotenv
APP_NAME="Simple CMS Landing Page"
APP_ENV=local
APP_KEY=base64:GENERATE_ON_INSTALL
APP_DEBUG=true
APP_URL=http://localhost:8000

# Database (shared with Admin Portal)
DB_CONNECTION=pgsql
DB_HOST=localhost
DB_PORT=5432
DB_DATABASE=cms_db
DB_USERNAME=cms_user
DB_PASSWORD=cms_password

# No authentication required
# No mail configuration required (contact form stores to DB only)
# No cache/queue/session drivers beyond defaults needed

# Vite
VITE_APP_NAME="${APP_NAME}"
```

### 4.2 config/database.php (PostgreSQL connection)

The default `pgsql` connection in `config/database.php` is used. No custom configuration beyond `.env` values is needed. The connection reads from the same `cms_db` database managed by the Admin Portal.

---

## 5. Vite and Tailwind Configuration

### 5.1 vite.config.js

```js
import { defineConfig } from 'vite';
import laravel from 'laravel-vite-plugin';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
    plugins: [
        laravel({
            input: [
                'resources/css/app.css',
                'resources/js/app.js',
            ],
            refresh: true,
        }),
        tailwindcss(),
    ],
});
```

### 5.2 resources/css/app.css

```css
@import "tailwindcss";

/* ========================================
   Design Tokens (Custom Theme)
   ======================================== */

@theme {
    --color-primary: #2271b1;
    --color-primary-dark: #135e96;
    --color-text-primary: #1e1e1e;
    --color-text-secondary: #646970;
    --color-border: #c3c4c7;
    --color-success: #00a32a;
    --color-warning: #dba617;
    --color-error: #d63638;
    --color-page-bg: #f0f0f1;
    --color-surface: #ffffff;
    --color-footer-bg: #1d2327;

    --radius-btn: 4px;
    --radius-card: 8px;

    --font-sans: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
        Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
}

/* Smooth scrolling for anchor navigation */
html {
    scroll-behavior: smooth;
}
```

### 5.3 resources/js/app.js

```js
import Alpine from 'alpinejs';
import htmx from 'htmx.org';

// Make Alpine available globally
window.Alpine = Alpine;
Alpine.start();

// Make htmx available globally
window.htmx = htmx;
```

---

## 6. .gitignore

```gitignore
/node_modules
/public/build
/public/hot
/public/storage
/storage/*.key
/vendor
.env
.env.backup
.env.production
.phpunit.result.cache
Homestead.json
Homestead.yaml
auth.json
npm-debug.log
yarn-error.log
/.fleet
/.idea
/.vscode
/.playwright-mcp
```

---

## 7. Directory Structure

```
landing/
├── app/
│   ├── Http/
│   │   └── Kernel.php
│   ├── Models/                          # (empty - models live in modules)
│   ├── Providers/
│   │   └── AppServiceProvider.php
│   └── View/
│       └── Components/                  # Shared Blade components
├── bootstrap/
├── config/
│   ├── app.php
│   ├── database.php
│   └── modules.php                      # nwidart/laravel-modules config
├── database/
│   └── (NO migrations - shared DB)
├── Modules/                             # nwidart/laravel-modules root
│   ├── HeroSection/
│   │   ├── app/
│   │   │   ├── Http/
│   │   │   │   └── Controllers/
│   │   │   │       └── HeroSectionController.php
│   │   │   ├── Models/
│   │   │   │   └── HeroSection.php
│   │   │   ├── Services/
│   │   │   │   └── HeroSectionService.php
│   │   │   ├── Data/
│   │   │   │   └── HeroSectionData.php
│   │   │   └── ViewModels/
│   │   │       └── HeroSectionViewModel.php
│   │   ├── resources/
│   │   │   └── views/
│   │   │       └── components/
│   │   │           └── hero-carousel.blade.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   └── module.json
│   ├── ProductAndServiceSection/
│   │   ├── app/
│   │   │   ├── Http/Controllers/
│   │   │   ├── Models/
│   │   │   ├── Services/
│   │   │   ├── Data/
│   │   │   └── ViewModels/
│   │   ├── resources/views/components/
│   │   ├── routes/
│   │   └── module.json
│   ├── FeaturesSection/
│   │   └── (same structure)
│   ├── TestimonialsSection/
│   │   └── (same structure)
│   ├── TeamSection/
│   │   └── (same structure)
│   ├── ContactSection/
│   │   ├── app/
│   │   │   ├── Http/
│   │   │   │   ├── Controllers/
│   │   │   │   │   └── ContactSectionController.php
│   │   │   │   └── Requests/
│   │   │   │       └── StoreContactMessageRequest.php
│   │   │   ├── Models/
│   │   │   │   ├── ContactInfo.php
│   │   │   │   └── ContactMessage.php
│   │   │   ├── Services/
│   │   │   ├── Data/
│   │   │   └── ViewModels/
│   │   ├── resources/views/components/
│   │   ├── routes/
│   │   └── module.json
│   └── Blog/
│       ├── app/
│       │   ├── Http/Controllers/
│       │   ├── Models/
│       │   │   ├── BlogCategory.php
│       │   │   └── BlogPost.php
│       │   ├── Services/
│       │   ├── Data/
│       │   └── ViewModels/
│       ├── resources/views/
│       │   ├── directory.blade.php
│       │   └── detail.blade.php
│       ├── routes/
│       └── module.json
├── public/
├── resources/
│   ├── css/
│   │   └── app.css
│   ├── js/
│   │   └── app.js
│   └── views/
│       ├── layouts/
│       │   ├── landing.blade.php
│       │   └── blog.blade.php
│       ├── partials/
│       │   ├── navbar.blade.php
│       │   └── footer.blade.php
│       ├── pages/
│       │   └── home.blade.php
│       └── errors/
│           ├── 404.blade.php
│           └── 500.blade.php
├── routes/
│   └── web.php
├── .env
├── .gitignore
├── artisan
├── composer.json
├── package.json
└── vite.config.js
```

**Key points:**
- `database/migrations/` is empty. All tables are created and managed by the Admin Portal.
- Each nwidart module follows the convention: `Modules/{ModuleName}/app/`, `Modules/{ModuleName}/resources/views/`, `Modules/{ModuleName}/routes/`.
- Shared layouts, partials, and error pages live in `resources/views/`.

---

## 8. Layouts

### 8.1 Landing Layout: resources/views/layouts/landing.blade.php

```blade
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'Simple CMS')</title>
    <meta name="description" content="@yield('meta_description', 'Simple CMS - Create and manage your marketing pages and blog content')">
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg">

    @include('partials.navbar')

    <!-- Spacer for fixed nav -->
    <div class="h-16"></div>

    @yield('content')

    @include('partials.footer')

</body>
</html>
```

### 8.2 Blog Layout: resources/views/layouts/blog.blade.php

```blade
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'Blog - Simple CMS')</title>
    <meta name="description" content="@yield('meta_description', 'Read the latest articles from Simple CMS')">
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg">

    @include('partials.navbar', ['activePage' => 'blog'])

    <!-- Spacer for fixed nav -->
    <div class="h-16"></div>

    @yield('content')

    @include('partials.footer')

</body>
</html>
```

---

## 9. Partials

### 9.1 Navbar: resources/views/partials/navbar.blade.php

```blade
<nav class="fixed top-0 left-0 right-0 bg-white shadow z-50" x-data="{ mobileOpen: false }">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <!-- Logo -->
            <a href="/" class="text-xl font-bold text-primary">Simple CMS</a>

            <!-- Desktop menu -->
            <div class="hidden md:flex space-x-6 text-sm font-medium text-text-secondary">
                @php
                    $active = $activePage ?? 'home';
                    $homePrefix = $active === 'home' ? '' : '/';
                @endphp
                <a href="{{ $active === 'home' ? '#hero' : '/' }}"
                   class="{{ $active === 'home' ? 'hover:text-primary' : 'hover:text-primary' }} transition">Home</a>
                <a href="{{ $homePrefix }}#products" class="hover:text-primary transition">Products &amp; Services</a>
                <a href="{{ $homePrefix }}#features" class="hover:text-primary transition">Features</a>
                <a href="{{ $homePrefix }}#testimonials" class="hover:text-primary transition">Testimonials</a>
                <a href="{{ $homePrefix }}#team" class="hover:text-primary transition">Team</a>
                <a href="{{ $homePrefix }}#contact" class="hover:text-primary transition">Contact</a>
                <a href="/blog" class="{{ $active === 'blog' ? 'text-primary font-bold' : 'hover:text-primary' }} transition">Blog</a>
            </div>

            <!-- Mobile hamburger -->
            <button class="md:hidden p-2 rounded text-text-secondary hover:text-primary"
                    @click="mobileOpen = !mobileOpen" aria-label="Toggle menu">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path x-show="!mobileOpen" stroke-linecap="round" stroke-linejoin="round"
                          d="M4 6h16M4 12h16M4 18h16"/>
                    <path x-show="mobileOpen" stroke-linecap="round" stroke-linejoin="round"
                          d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>

        <!-- Mobile menu -->
        <div x-show="mobileOpen" x-transition class="md:hidden pb-4 space-y-2 text-sm font-medium text-text-secondary">
            <a href="/" class="block py-2 hover:text-primary" @click="mobileOpen = false">Home</a>
            <a href="{{ $homePrefix ?? '/' }}#products" class="block py-2 hover:text-primary" @click="mobileOpen = false">Products &amp; Services</a>
            <a href="{{ $homePrefix ?? '/' }}#features" class="block py-2 hover:text-primary" @click="mobileOpen = false">Features</a>
            <a href="{{ $homePrefix ?? '/' }}#testimonials" class="block py-2 hover:text-primary" @click="mobileOpen = false">Testimonials</a>
            <a href="{{ $homePrefix ?? '/' }}#team" class="block py-2 hover:text-primary" @click="mobileOpen = false">Team</a>
            <a href="{{ $homePrefix ?? '/' }}#contact" class="block py-2 hover:text-primary" @click="mobileOpen = false">Contact</a>
            <a href="/blog" class="block py-2 {{ ($activePage ?? '') === 'blog' ? 'text-primary font-bold' : 'hover:text-primary' }}">Blog</a>
        </div>
    </div>
</nav>
```

### 9.2 Footer: resources/views/partials/footer.blade.php

```blade
<footer class="bg-footer-bg text-white py-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <!-- Company -->
            <div>
                <h3 class="text-lg font-bold mb-4">Simple CMS</h3>
                <p class="text-gray-400 text-sm">Empowering small businesses to create and manage their online presence with ease.</p>
            </div>

            <!-- Quick links -->
            <div>
                <h3 class="text-lg font-bold mb-4">Quick Links</h3>
                <ul class="space-y-2 text-sm text-gray-400">
                    <li><a href="/" class="hover:text-white transition">Home</a></li>
                    <li><a href="/#products" class="hover:text-white transition">Products &amp; Services</a></li>
                    <li><a href="/#features" class="hover:text-white transition">Features</a></li>
                    <li><a href="/#testimonials" class="hover:text-white transition">Testimonials</a></li>
                    <li><a href="/#team" class="hover:text-white transition">Team</a></li>
                    <li><a href="/#contact" class="hover:text-white transition">Contact</a></li>
                    <li><a href="/blog" class="hover:text-white transition">Blog</a></li>
                </ul>
            </div>

            <!-- Social media -->
            <div>
                <h3 class="text-lg font-bold mb-4">Follow Us</h3>
                <div class="flex space-x-4">
                    <a href="https://facebook.com" target="_blank" rel="noopener" aria-label="Facebook"
                       class="text-gray-400 hover:text-white transition">
                        {{-- Facebook SVG icon --}}
                        <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                    </a>
                    <a href="https://twitter.com" target="_blank" rel="noopener" aria-label="Twitter"
                       class="text-gray-400 hover:text-white transition">
                        {{-- X/Twitter SVG icon --}}
                        <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                    </a>
                    <a href="https://instagram.com" target="_blank" rel="noopener" aria-label="Instagram"
                       class="text-gray-400 hover:text-white transition">
                        {{-- Instagram SVG icon --}}
                        <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zM12 0C8.741 0 8.333.014 7.053.072 2.695.272.273 2.69.073 7.052.014 8.333 0 8.741 0 12c0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98C8.333 23.986 8.741 24 12 24c3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98C15.668.014 15.259 0 12 0zm0 5.838a6.162 6.162 0 100 12.324 6.162 6.162 0 000-12.324zM12 16a4 4 0 110-8 4 4 0 010 8zm6.406-11.845a1.44 1.44 0 100 2.881 1.44 1.44 0 000-2.881z"/></svg>
                    </a>
                    <a href="https://linkedin.com" target="_blank" rel="noopener" aria-label="LinkedIn"
                       class="text-gray-400 hover:text-white transition">
                        {{-- LinkedIn SVG icon --}}
                        <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/></svg>
                    </a>
                </div>
            </div>
        </div>

        <div class="border-t border-gray-600 mt-8 pt-8 text-center text-sm text-gray-400">
            &copy; {{ date('Y') }} Simple CMS. All rights reserved.
        </div>
    </div>
</footer>
```

---

## 10. UI Components (Pure Tailwind)

All UI components are implemented as Blade components using Tailwind CSS utility classes. No component library is required.

### 10.1 Component Catalog

| Component | Location | Description |
|-----------|----------|-------------|
| Card | Inline Tailwind | `bg-surface border border-border rounded-card shadow` container |
| Button (Primary) | Inline Tailwind | `bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition` |
| Button (Secondary) | Inline Tailwind | `border border-primary text-primary hover:bg-primary hover:text-white font-semibold py-3 px-8 rounded-btn transition` |
| Carousel | Alpine.js component | Hero section and testimonials section carousel with auto-slide |
| ContactForm | htmx-powered form | POST to `/contact` with htmx, inline success/error feedback |
| StarRating | Blade partial | SVG stars filled based on rating integer (1-5) |
| Pagination | Blade partial | Laravel paginator styled with Tailwind |

### 10.2 Star Rating Component: resources/views/components/star-rating.blade.php

```blade
@props(['rating' => 0, 'size' => 'w-6 h-6'])

<div class="flex space-x-1">
    @for ($i = 1; $i <= 5; $i++)
        <svg class="{{ $size }} {{ $i <= $rating ? 'text-warning' : 'text-gray-300' }}"
             fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
        </svg>
    @endfor
</div>
```

---

## 11. Frontend JavaScript Behavior

### 11.1 Alpine.js Components

| Component | Behavior | Trigger |
|-----------|----------|---------|
| Hero Carousel | Auto-slide every 5s, prev/next arrows, dot indicators, pause on manual interaction then restart | Page load (x-init) |
| Testimonials Carousel | Manual prev/next arrows, dot indicators, 1 testimonial per slide | User interaction |
| Mobile Menu | Toggle hamburger menu on mobile viewports | Click on hamburger button |
| Contact Form | Client-side character counter for message field (max 500) | Keyup on textarea |

### 11.2 htmx Interactions

| Interaction | Trigger | Method | Target | Swap |
|-------------|---------|--------|--------|------|
| Contact form submission | Form submit | POST /contact | `#contact-form-result` | innerHTML |
| Blog pagination | Click on page link | GET /blog?page=N | `#blog-list` | innerHTML |

---

## 12. Data Access Layer

### 12.1 Shared Database Tables

The Landing Page reads from the following tables in the shared `cms_db` PostgreSQL database. These tables are created and managed by the Admin Portal. **No migrations are included in the Landing Page application.**

| Table | Module | Access | Notes |
|-------|--------|--------|-------|
| `hero_sections` | Hero Section | Read | Filter: status=ACTIVE, within date range |
| `product_services` | Product & Service | Read | Filter: status=ACTIVE, order by display_order |
| `features` | Features | Read | Filter: status=ACTIVE, order by display_order |
| `testimonials` | Testimonials | Read | Filter: status=ACTIVE, order by display_order |
| `team_members` | Team | Read | Filter: status=ACTIVE, order by display_order |
| `contact_info` | Contact | Read | Single record |
| `contact_messages` | Contact | Write | Created by contact form submission |
| `blog_categories` | Blog | Read | All categories |
| `blog_posts` | Blog | Read | Filter: status=ACTIVE, order by effective_date DESC |

### 12.2 Eloquent Model Base Configuration

All Eloquent models in the Landing Page application share these conventions:

```php
<?php

// Common traits for all read-only models:
// - No $fillable needed (read-only)
// - $keyType = 'string' (UUID primary keys)
// - $incrementing = false (UUIDs are not auto-incrementing)
// - $timestamps = false (Landing Page does not write timestamps)

namespace Modules\HeroSection\Models;

use Illuminate\Database\Eloquent\Model;

class HeroSection extends Model
{
    protected $table = 'hero_sections';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];
}
```

### 12.3 ContactMessage Model (Write)

```php
<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactMessage extends Model
{
    use HasUuids;

    protected $table = 'contact_messages';

    protected $keyType = 'string';
    public $incrementing = false;

    const CREATED_AT = 'created_at';
    const UPDATED_AT = null;

    protected $fillable = [
        'sender_name',
        'sender_email',
        'message_content',
        'submitted_at',
    ];

    protected $casts = [
        'id' => 'string',
        'submitted_at' => 'datetime',
        'created_at' => 'datetime',
    ];
}
```

---

## 13. Routes

### 13.1 routes/web.php (Application-level)

```php
<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Landing Page Routes
|--------------------------------------------------------------------------
| Module-specific routes are registered via nwidart/laravel-modules
| in each module's routes/web.php file. The routes below are
| application-level routes.
|--------------------------------------------------------------------------
*/

// Home page (landing page with all sections)
Route::get('/', [\App\Http\Controllers\HomeController::class, 'index'])->name('home');
```

### 13.2 Route Summary (all routes)

| Method | URI | Controller | Name | Module |
|--------|-----|------------|------|--------|
| GET | `/` | HomeController@index | home | App (aggregates all sections) |
| GET | `/blog` | BlogController@directory | blog.directory | Blog |
| GET | `/{slug}` | BlogController@detail | blog.detail | Blog |
| POST | `/contact` | ContactSectionController@store | contact.store | Contact |

**Important:** The blog detail route `GET /{slug}` must be registered **last** (lowest priority) to avoid catching other routes. The route uses a `where('slug', '[a-z0-9\-]+')` constraint to match only SEO-friendly slugs.

### 13.3 HomeController

```php
<?php

namespace App\Http\Controllers;

use Modules\HeroSection\Services\HeroSectionService;
use Modules\ProductAndServiceSection\Services\ProductServiceService;
use Modules\FeaturesSection\Services\FeatureService;
use Modules\TestimonialsSection\Services\TestimonialService;
use Modules\TeamSection\Services\TeamMemberService;
use Modules\ContactSection\Services\ContactInfoService;

class HomeController extends Controller
{
    public function __construct(
        private readonly HeroSectionService $heroSectionService,
        private readonly ProductServiceService $productServiceService,
        private readonly FeatureService $featureService,
        private readonly TestimonialService $testimonialService,
        private readonly TeamMemberService $teamMemberService,
        private readonly ContactInfoService $contactInfoService,
    ) {}

    public function index()
    {
        return view('pages.home', [
            'heroes' => $this->heroSectionService->getActiveHeroes(),
            'products' => $this->productServiceService->getActiveProducts(),
            'features' => $this->featureService->getActiveFeatures(),
            'testimonials' => $this->testimonialService->getActiveTestimonials(),
            'teamMembers' => $this->teamMemberService->getActiveMembers(),
            'contactInfo' => $this->contactInfoService->getContactInfo(),
        ]);
    }
}
```

---

## 14. Error Handling

### 14.1 404 Page: resources/views/errors/404.blade.php

```blade
@extends('layouts.landing')

@section('title', 'Page Not Found - Simple CMS')

@section('content')
<div class="min-h-[60vh] flex flex-col items-center justify-center text-center px-4">
    <h1 class="text-6xl font-bold text-primary mb-4">404</h1>
    <p class="text-xl text-text-secondary mb-8">The page you are looking for could not be found.</p>
    <a href="/" class="bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
        Back to Home
    </a>
</div>
@endsection
```

### 14.2 500 Page: resources/views/errors/500.blade.php

```blade
@extends('layouts.landing')

@section('title', 'Server Error - Simple CMS')

@section('content')
<div class="min-h-[60vh] flex flex-col items-center justify-center text-center px-4">
    <h1 class="text-6xl font-bold text-error mb-4">500</h1>
    <p class="text-xl text-text-secondary mb-8">Something went wrong. Please try again later.</p>
    <a href="/" class="bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
        Back to Home
    </a>
</div>
@endsection
```

---

## 15. Theming

Light mode only. No dark mode or theme switching is implemented for the public landing page.

---

## 16. Pagination

Blog pagination uses Laravel's built-in `simplePaginate()` method with 10 items per page (CONSL0009). Pagination links are styled with Tailwind CSS to match the design tokens.

---

## 17. Security

No authentication or authorization is required (CONSL0003). The only security measure is CAPTCHA on the contact form (NFRL00084) to prevent spam submissions. Google reCAPTCHA v3 is recommended for implementation.

CSRF protection is enabled by default via Laravel middleware for the POST `/contact` route.

---

## 18. Traceability Matrix

| ID | Type | Description | Version |
|----|------|-------------|---------|
| CONSL0003 | Constraint | No authentication required - public landing page | v1.0.0 |
| CONSL0006 | Constraint | No header/footer in hero section | v1.0.0 |
| CONSL0009 | Constraint | Blog pagination 10 per page | v1.0.0 |

---

## 19. Module Specification Index

| # | Module | Specification File |
|---|--------|--------------------|
| 1 | Hero Section | [hero-section/SPEC.md](./hero-section/SPEC.md) |
| 2 | Product and Service Section | [product-and-service-section/SPEC.md](./product-and-service-section/SPEC.md) |
| 3 | Features Section | [features-section/SPEC.md](./features-section/SPEC.md) |
| 4 | Testimonials Section | [testimonials-section/SPEC.md](./testimonials-section/SPEC.md) |
| 5 | Team Section | [team-section/SPEC.md](./team-section/SPEC.md) |
| 6 | Contact Section | [contact-section/SPEC.md](./contact-section/SPEC.md) |
| 7 | Blog | [blog/SPEC.md](./blog/SPEC.md) |
