# Landing Page — Technical Specification

## Table of Contents

### Shared Infrastructure
- [1. Project Overview](#1-project-overview)
- [2. Composer & npm Configuration](#2-composer--npm-configuration)
- [3. Application Configuration](#3-application-configuration)
- [4. Build & Tooling](#4-build--tooling)
- [4b. .gitignore](#4b-gitignore)
- [5. Directory Structure](#5-directory-structure)
- [7. Layouts (Blade)](#7-layouts-blade)
- [8. Partials & Includes](#8-partials--includes)
- [9. UI Components (Blade + Tailwind)](#9-ui-components-blade--tailwind)
- [10. Frontend JS Structure](#10-frontend-js-structure)
- [11. Styles](#11-styles)
- [13. Data Access](#13-data-access)
- [14. Error Handling & Exceptions](#14-error-handling--exceptions)
- [16. Pagination Support](#16-pagination-support)
- [17. Logging Strategy](#17-logging-strategy)
- [19. Event-Driven Architecture](#19-event-driven-architecture)
- [21. DTO & Data Transformation](#21-dto--data-transformation)
- [22. Testing Strategy](#22-testing-strategy)

### Modules
- [Hero Section](hero-section/SPEC.md)
- [Product and Service Section](product-and-service-section/SPEC.md)
- [Features Section](features-section/SPEC.md)
- [Testimonials Section](testimonials-section/SPEC.md)
- [Team Section](team-section/SPEC.md)
- [Contact Section](contact-section/SPEC.md)
- [Blog](blog/SPEC.md)

---

## 1. Project Overview

**Application Name**: Landing Page
**Project Slug**: `landing-page`
**PHP Version**: 8.3+
**Laravel Version**: 12.x
**Description**: A landing page to display the marketing content and blog content to the public.
**Versions Covered**: v1.0.0

### Optional Components (Auto-Determined)

| Component | Selection | Source |
|-----------|-----------|--------|
| **Database** | PostgreSQL 14 | CLAUDE.md → depends on CMS Database |
| **Authentication** | none | PRD.md → CONSL0003: public user access, no auth required |
| **Scheduling** | no | No scheduling-related NFRs |
| **Messaging** | no | No message queue dependency |
| **Reporting** | no | No reporting requirements |

### Technology Stack

| Component | Version |
|-----------|---------|
| PHP | 8.3+ |
| Laravel | 12.x |
| Composer | 2.x |
| Blade | Built-in |
| Tailwind CSS | 4.x |
| Alpine.js | 3.x |
| htmx | 2.x |
| Vite | 6.x |
| Node.js | 22.x LTS |
| PostgreSQL | 14.x |

### User Roles

This is a **public-facing landing page**. There are no authenticated user roles. All routes are publicly accessible.

| Role | Access | Permission Role |
|------|--------|----------------|
| Public (anonymous) | All pages | _none_ |

### Navigation Items

The landing page uses a **top navigation bar** (not a sidebar). Menu items are:

| Label | Href | Type |
|-------|------|------|
| Home | `/` or `#hero` | Anchor (home page) |
| Products & Services | `/#products` | Anchor (home page section) |
| Features | `/#features` | Anchor (home page section) |
| Testimonials | `/#testimonials` | Anchor (home page section) |
| Team | `/#team` | Anchor (home page section) |
| Contact | `/#contact` | Anchor (home page section) |
| Blog | `/blog` | Separate page |

### Modules

| Module | Type | Stories | Versions | Spec |
|--------|------|---------|----------|------|
| Authentication and Authorization | System | 0 | v1.0.0 | _No spec — public access, no auth_ |
| User | System | 0 | v1.0.0 | _No spec — no user management_ |
| Hero Section | Business | 1 | v1.0.0 | [SPEC](hero-section/SPEC.md) |
| Product and Service Section | Business | 1 | v1.0.0 | [SPEC](product-and-service-section/SPEC.md) |
| Features Section | Business | 1 | v1.0.0 | [SPEC](features-section/SPEC.md) |
| Testimonials Section | Business | 1 | v1.0.0 | [SPEC](testimonials-section/SPEC.md) |
| Team Section | Business | 1 | v1.0.0 | [SPEC](team-section/SPEC.md) |
| Contact Section | Business | 1 | v1.0.0 | [SPEC](contact-section/SPEC.md) |
| Blog | Business | 2 | v1.0.0 | [SPEC](blog/SPEC.md) |

### Shared Database — Read-Only Access

The Landing Page shares the `cms_db` PostgreSQL database with the Admin Portal. **The Admin Portal owns all schema migrations.** The Landing Page does NOT create migrations — it reads from existing tables.

| Module | Table Prefix | Access Mode |
|--------|-------------|-------------|
| Hero Section | `hrs_` | Read-only (status=ACTIVE, within date range) |
| Product and Service Section | `pas_` | Read-only (ordered by display_order) |
| Features Section | `fts_` | Read-only (ordered by display_order) |
| Testimonials Section | `tst_` | Read-only (ordered by display_order) |
| Team Section | `tms_` | Read-only (ordered by display_order) |
| Contact Section | `cts_` | Read (ContactInfo) + Write (ContactMessage) |
| Blog | `blg_` | Read-only (status=ACTIVE, ordered by effective_date desc) |

### Image Serving from BLOB

For modules with BLOB image storage (Hero, Product/Service, Team, Blog), images are stored as BYTEA in the database. The Landing Page reads `image_data` and `thumbnail_data` columns and serves them via dedicated image controller endpoints that set appropriate `Content-Type` headers.

### Input Sources

- CLAUDE.md: `/co2-example-simplecms/CLAUDE.md`
- SECRET.md: `/co2-example-simplecms/SECRET.md`
- PRD.md: `landing/context/PRD.md`
- Module Model: `landing/context/model/MODEL.md` (cross-references `admin/context/model/`)
- Mockup Screens: `landing/context/mockup/MOCKUP.html`

---

## 2. Composer & npm Configuration

### 2.1 composer.json

```json
{
    "name": "simplecms/landing-page",
    "type": "project",
    "description": "Simple CMS Landing Page — public marketing and blog content",
    "require": {
        "php": "^8.3",
        "laravel/framework": "^12.0",
        "nwidart/laravel-modules": "^11.0",
        "spatie/laravel-data": "^4.0",
        "spatie/laravel-view-models": "^1.6",
        "spatie/laravel-health": "^1.29"
    },
    "require-dev": {
        "phpunit/phpunit": "^11.0",
        "phpat/phpat": "^0.10",
        "laravel/pint": "^1.0"
    },
    "autoload": {
        "psr-4": {
            "App\\": "app/",
            "Modules\\": "Modules/"
        }
    },
    "autoload-dev": {
        "psr-4": {
            "Tests\\": "tests/"
        }
    },
    "scripts": {
        "post-autoload-dump": [
            "Illuminate\\Foundation\\ComposerScripts::postAutoloadDump",
            "@php artisan package:discover --ansi"
        ]
    },
    "config": {
        "optimize-autoloader": true,
        "preferred-install": "dist",
        "sort-packages": true
    },
    "minimum-stability": "stable",
    "prefer-stable": true
}
```

**Notes:**
- No `spatie/laravel-permission` — no roles/auth needed for a public landing page.
- No `owen-it/laravel-auditing` — no user-driven mutations requiring audit trail (except contact form, which uses `submitted_at` timestamps).
- No `laravel/socialite` or `laravel/breeze` — no authentication.

### 2.2 package.json

```json
{
    "private": true,
    "type": "module",
    "scripts": {
        "dev": "vite",
        "build": "vite build"
    },
    "devDependencies": {
        "laravel-vite-plugin": "^1.2",
        "vite": "^6.0",
        "tailwindcss": "^4.0",
        "@tailwindcss/vite": "^4.0",
        "autoprefixer": "^10.4",
        "postcss": "^8.4"
    },
    "dependencies": {
        "alpinejs": "^3.14",
        "htmx.org": "^2.0"
    }
}
```

---

## 3. Application Configuration

### .env (default)

```env
APP_NAME="Landing Page"
APP_ENV=local
APP_KEY=
APP_DEBUG=true
APP_URL=http://localhost:8000

LOG_CHANNEL=stack
LOG_LEVEL=debug

DB_CONNECTION=pgsql
DB_HOST=localhost
DB_PORT=5432
DB_DATABASE=cms_db
DB_USERNAME=cms_user
DB_PASSWORD=cms_password

SESSION_DRIVER=file
CACHE_STORE=file
QUEUE_CONNECTION=sync
```

### config/database.php

```php
'connections' => [
    'pgsql' => [
        'driver' => 'pgsql',
        'host' => env('DB_HOST', 'localhost'),
        'port' => env('DB_PORT', '5432'),
        'database' => env('DB_DATABASE', 'cms_db'),
        'username' => env('DB_USERNAME', 'cms_user'),
        'password' => env('DB_PASSWORD', ''),
        'charset' => 'utf8',
        'prefix' => '',
        'prefix_indexes' => true,
        'search_path' => 'public',
        'sslmode' => 'prefer',
    ],
],
```

### config/logging.php

```php
'channels' => [
    'stack' => [
        'driver' => 'stack',
        'channels' => ['daily'],
    ],
    'daily' => [
        'driver' => 'daily',
        'path' => storage_path('logs/laravel.log'),
        'level' => env('LOG_LEVEL', 'debug'),
        'days' => 14,
    ],
],
```

---

## 4. Build & Tooling

### 4.1 Resource Directory Structure

```
resources/
├── css/
│   ├── app.css                  # Main entry — imports Tailwind + partials
│   ├── base/
│   │   ├── reset.css            # Tailwind @layer base
│   │   └── typography.css       # Font imports, text defaults
│   ├── components/
│   │   ├── buttons.css          # @layer components — button styles
│   │   ├── forms.css            # Input, select, textarea styles
│   │   ├── cards.css            # Card component styles
│   │   └── navigation.css       # Nav, breadcrumb styles
│   └── layouts/
│       ├── navbar.css           # Top navigation bar responsive
│       └── footer.css           # Footer styles
├── js/
│   ├── app.js                   # Entry — registers Alpine, htmx
│   ├── alpine/
│   │   ├── stores/
│   │   │   └── toast.js         # Toast notification store
│   │   └── components/
│   │       ├── carousel.js      # Hero/testimonial carousel
│   │       └── mobile-menu.js   # Mobile hamburger menu
│   └── htmx/
│       ├── index.js             # Global error handlers
│       └── extensions/
│           └── toast-errors.js  # Map htmx errors to toast notifications
└── views/
    ├── layouts/
    │   ├── landing.blade.php    # Landing page layout (navbar + footer)
    │   └── error.blade.php      # Error layout
    ├── partials/
    │   ├── navbar.blade.php     # Top navigation bar
    │   └── footer.blade.php     # Footer
    └── components/
        ├── alert.blade.php
        ├── button.blade.php
        ├── card.blade.php
        ├── form-control.blade.php
        ├── input.blade.php
        ├── textarea.blade.php
        ├── pagination.blade.php
        └── toast-container.blade.php
```

### 4.2 Vite Configuration

```js
// vite.config.js
import { defineConfig } from 'vite';
import laravel from 'laravel-vite-plugin';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
    plugins: [
        laravel({
            input: ['resources/css/app.css', 'resources/js/app.js'],
            refresh: true,
        }),
        tailwindcss(),
    ],
});
```

### 4.3 Tailwind Configuration

```js
// tailwind.config.js
export default {
    content: [
        './resources/**/*.blade.php',
        './resources/**/*.js',
        './Modules/*/resources/views/**/*.blade.php',
    ],
    theme: {
        extend: {
            colors: {
                primary: {
                    DEFAULT: '#2271b1',
                    dark: '#135e96',
                },
                'text-primary': '#1e1e1e',
                'text-secondary': '#646970',
                border: '#c3c4c7',
                success: '#00a32a',
                warning: '#dba617',
                error: '#d63638',
                'page-bg': '#f0f0f1',
                surface: '#ffffff',
                'footer-bg': '#1d2327',
            },
            fontFamily: {
                sans: [
                    '-apple-system', 'BlinkMacSystemFont', '"Segoe UI"',
                    'Roboto', 'Oxygen-Sans', 'Ubuntu', 'Cantarell',
                    '"Helvetica Neue"', 'sans-serif',
                ],
            },
            borderRadius: {
                btn: '4px',
                card: '8px',
            },
        },
    },
    plugins: [],
};
```

---

## 4b. `.gitignore`

```
# Laravel
/vendor/
/node_modules/
/public/build/
/public/hot
/storage/*.key
.env
.env.backup
.env.production

# IDE
.idea/
*.iml
.vscode/
*.swp
*~

# OS
.DS_Store
Thumbs.db

# Testing
/coverage/
.phpunit.result.cache

# E2E
e2e/node_modules/
e2e/test-results/
e2e/playwright-report/
e2e/visual-baselines/

# Logs
*.log
storage/logs/*
!storage/logs/.gitkeep
```

---

## 5. Directory Structure

```
landing-page/
├── app/
│   ├── Exceptions/
│   │   ├── Handler.php
│   │   └── WebApplicationException.php
│   ├── Http/
│   │   ├── Controllers/
│   │   │   ├── HomeController.php
│   │   │   └── ImageController.php
│   │   └── Middleware/
│   │       ├── CspNonceMiddleware.php
│   │       └── CorrelationIdMiddleware.php
│   ├── Providers/
│   │   ├── AppServiceProvider.php
│   │   └── EventServiceProvider.php
│   └── Services/
│       └── ImageService.php
│
├── Modules/
│   ├── HeroSection/
│   │   ├── Contracts/
│   │   │   └── HeroSectionServiceInterface.php
│   │   ├── DTOs/
│   │   │   └── HeroSectionData.php
│   │   ├── Exceptions/
│   │   │   └── HeroSectionException.php
│   │   ├── Services/
│   │   │   └── HeroSectionService.php
│   │   ├── Models/
│   │   │   └── HeroSection.php
│   │   ├── ViewModels/
│   │   │   └── HeroSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── HeroSectionServiceProvider.php
│   │   └── Tests/
│   │
│   ├── ProductAndServiceSection/
│   │   ├── Contracts/
│   │   │   └── ProductServiceServiceInterface.php
│   │   ├── DTOs/
│   │   │   └── ProductServiceData.php
│   │   ├── Exceptions/
│   │   │   └── ProductServiceException.php
│   │   ├── Services/
│   │   │   └── ProductServiceService.php
│   │   ├── Models/
│   │   │   └── ProductService.php
│   │   ├── ViewModels/
│   │   │   └── ProductServiceSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── ProductAndServiceSectionServiceProvider.php
│   │   └── Tests/
│   │
│   ├── FeaturesSection/
│   │   ├── Contracts/
│   │   │   └── FeatureServiceInterface.php
│   │   ├── DTOs/
│   │   │   └── FeatureData.php
│   │   ├── Exceptions/
│   │   │   └── FeatureException.php
│   │   ├── Services/
│   │   │   └── FeatureService.php
│   │   ├── Models/
│   │   │   └── Feature.php
│   │   ├── ViewModels/
│   │   │   └── FeaturesSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── FeaturesSectionServiceProvider.php
│   │   └── Tests/
│   │
│   ├── TestimonialsSection/
│   │   ├── Contracts/
│   │   │   └── TestimonialServiceInterface.php
│   │   ├── DTOs/
│   │   │   └── TestimonialData.php
│   │   ├── Exceptions/
│   │   │   └── TestimonialException.php
│   │   ├── Services/
│   │   │   └── TestimonialService.php
│   │   ├── Models/
│   │   │   └── Testimonial.php
│   │   ├── ViewModels/
│   │   │   └── TestimonialsSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── TestimonialsSectionServiceProvider.php
│   │   └── Tests/
│   │
│   ├── TeamSection/
│   │   ├── Contracts/
│   │   │   └── TeamMemberServiceInterface.php
│   │   ├── DTOs/
│   │   │   └── TeamMemberData.php
│   │   ├── Exceptions/
│   │   │   └── TeamMemberException.php
│   │   ├── Services/
│   │   │   └── TeamMemberService.php
│   │   ├── Models/
│   │   │   └── TeamMember.php
│   │   ├── ViewModels/
│   │   │   └── TeamSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── TeamSectionServiceProvider.php
│   │   └── Tests/
│   │
│   ├── ContactSection/
│   │   ├── Contracts/
│   │   │   └── ContactSectionServiceInterface.php
│   │   ├── DTOs/
│   │   │   ├── ContactInfoData.php
│   │   │   └── ContactMessageData.php
│   │   ├── Exceptions/
│   │   │   └── ContactSectionException.php
│   │   ├── Services/
│   │   │   └── ContactSectionService.php
│   │   ├── Models/
│   │   │   ├── ContactInfo.php
│   │   │   └── ContactMessage.php
│   │   ├── Http/
│   │   │   ├── Controllers/
│   │   │   │   └── ContactFragmentController.php
│   │   │   └── Requests/
│   │   │       └── StoreContactMessageRequest.php
│   │   ├── ViewModels/
│   │   │   └── ContactSectionViewModel.php
│   │   ├── routes/
│   │   │   └── web.php
│   │   ├── Providers/
│   │   │   └── ContactSectionServiceProvider.php
│   │   └── Tests/
│   │
│   └── Blog/
│       ├── Contracts/
│       │   └── BlogServiceInterface.php
│       ├── DTOs/
│       │   ├── BlogPostData.php
│       │   └── BlogCategoryData.php
│       ├── Exceptions/
│       │   └── BlogException.php
│       ├── Services/
│       │   └── BlogService.php
│       ├── Models/
│       │   ├── BlogPost.php
│       │   └── BlogCategory.php
│       ├── Http/
│       │   └── Controllers/
│       │       └── BlogPageController.php
│       ├── ViewModels/
│       │   ├── BlogListViewModel.php
│       │   └── BlogDetailViewModel.php
│       ├── resources/
│       │   └── views/
│       │       └── pages/
│       │           ├── index.blade.php
│       │           └── show.blade.php
│       ├── routes/
│       │   └── web.php
│       ├── Providers/
│       │   └── BlogServiceProvider.php
│       └── Tests/
│
├── resources/
│   ├── css/                    # (see Section 4.1)
│   ├── js/                     # (see Section 4.1)
│   └── views/                  # Shared views (see Section 4.1)
│
├── routes/
│   └── web.php                 # Core routes (home page)
│
├── config/
│   ├── app.php
│   ├── database.php
│   ├── logging.php
│   └── modules.php
│
├── public/
│   └── build/                  # Vite build output (gitignored)
│
├── storage/
├── tests/
│   ├── Architecture/
│   │   └── ModuleBoundaryTest.php
│   ├── Feature/
│   └── Unit/
│
├── composer.json
├── package.json
├── vite.config.js
├── tailwind.config.js
├── .env.example
└── .gitignore
```

**Note:** No `database/migrations/` directory — the Admin Portal owns all schema migrations. This application only reads from existing tables (and writes to `cts_contact_message`).

---

## 7. Layouts (Blade)

This application has **two layouts** (no auth layout since there's no authentication):

### 7.1 Landing Layout (`landing.blade.php`)

The main layout for the home page and blog pages. Includes the top navigation bar, content slot, and footer.

```blade
{{-- resources/views/layouts/landing.blade.php --}}
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>{{ $title ?? 'Simple CMS' }}</title>
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg">

    @include('partials.navbar')

    <!-- Spacer for fixed nav -->
    <div class="h-16"></div>

    <main>
        {{ $slot }}
    </main>

    @include('partials.footer')

    <x-toast-container />
</body>
</html>
```

### 7.2 Error Layout (`error.blade.php`)

```blade
{{-- resources/views/layouts/error.blade.php --}}
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{{ $title ?? 'Error' }} - Simple CMS</title>
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg min-h-screen flex items-center justify-center">
    <div class="text-center">
        <h1 class="text-6xl font-bold text-primary mb-4">{{ $code ?? '500' }}</h1>
        <p class="text-xl text-text-secondary mb-8">{{ $message ?? 'Something went wrong.' }}</p>
        <a href="/" class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
            Back to Home
        </a>
    </div>
</body>
</html>
```

---

## 8. Partials & Includes

### 8.1 Navbar (`navbar.blade.php`)

The top navigation bar with responsive mobile menu. Matches the mockup `home.html` navigation.

```blade
{{-- resources/views/partials/navbar.blade.php --}}
<nav class="fixed top-0 left-0 right-0 bg-white shadow z-50" x-data="{ mobileOpen: false }">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <!-- Logo -->
            <a href="/" class="text-xl font-bold text-primary">Simple CMS</a>

            <!-- Desktop menu -->
            <div class="hidden md:flex space-x-6 text-sm font-medium text-text-secondary">
                <a href="/#hero" class="hover:text-primary transition">Home</a>
                <a href="/#products" class="hover:text-primary transition">Products & Services</a>
                <a href="/#features" class="hover:text-primary transition">Features</a>
                <a href="/#testimonials" class="hover:text-primary transition">Testimonials</a>
                <a href="/#team" class="hover:text-primary transition">Team</a>
                <a href="/#contact" class="hover:text-primary transition">Contact</a>
                <a href="/blog" class="hover:text-primary transition {{ request()->is('blog*') ? 'text-primary font-bold' : '' }}">Blog</a>
            </div>

            <!-- Mobile hamburger -->
            <button class="md:hidden p-2 rounded text-text-secondary hover:text-primary"
                    @click="mobileOpen = !mobileOpen" aria-label="Toggle menu">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path x-show="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16"/>
                    <path x-show="mobileOpen" stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>

        <!-- Mobile menu -->
        <div x-show="mobileOpen" x-transition class="md:hidden pb-4 space-y-2 text-sm font-medium text-text-secondary">
            <a href="/#hero" class="block py-2 hover:text-primary" @click="mobileOpen = false">Home</a>
            <a href="/#products" class="block py-2 hover:text-primary" @click="mobileOpen = false">Products & Services</a>
            <a href="/#features" class="block py-2 hover:text-primary" @click="mobileOpen = false">Features</a>
            <a href="/#testimonials" class="block py-2 hover:text-primary" @click="mobileOpen = false">Testimonials</a>
            <a href="/#team" class="block py-2 hover:text-primary" @click="mobileOpen = false">Team</a>
            <a href="/#contact" class="block py-2 hover:text-primary" @click="mobileOpen = false">Contact</a>
            <a href="/blog" class="block py-2 hover:text-primary">Blog</a>
        </div>
    </div>
</nav>
```

### 8.2 Footer (`footer.blade.php`)

```blade
{{-- resources/views/partials/footer.blade.php --}}
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
                    <li><a href="/#products" class="hover:text-white transition">Products & Services</a></li>
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
                    {{-- Facebook, Twitter/X, Instagram, LinkedIn icons --}}
                    {{-- Use the same SVG icons from the mockup --}}
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

## 9. UI Components (Blade + Tailwind)

Reusable anonymous Blade components using pure Tailwind CSS utility classes.

### 9.1 Alert

```blade
{{-- resources/views/components/alert.blade.php --}}
@props(['type' => 'info', 'message'])

@php
$colors = match($type) {
    'success' => 'bg-green-50 text-success border-success/30',
    'error' => 'bg-red-50 text-error border-error/30',
    'warning' => 'bg-yellow-50 text-warning border-warning/30',
    default => 'bg-blue-50 text-primary border-primary/30',
};
@endphp

<div class="p-4 rounded-card border {{ $colors }}" role="alert">
    <span class="text-sm font-medium">{{ $message }}</span>
</div>
```

### 9.2 Button

```blade
{{-- resources/views/components/button.blade.php --}}
@props(['type' => 'button', 'tone' => 'primary', 'href' => null])

@php
$classes = match($tone) {
    'primary' => 'bg-primary hover:bg-primary-dark text-white',
    'secondary' => 'bg-white hover:bg-page-bg text-text-primary border border-border',
    default => 'bg-primary hover:bg-primary-dark text-white',
};
$baseClasses = 'inline-block font-semibold py-3 px-6 rounded-btn transition text-sm';
@endphp

@if($href)
    <a href="{{ $href }}" {{ $attributes->merge(['class' => "$baseClasses $classes"]) }}>{{ $slot }}</a>
@else
    <button type="{{ $type }}" {{ $attributes->merge(['class' => "$baseClasses $classes"]) }}>{{ $slot }}</button>
@endif
```

### 9.3 Card

```blade
{{-- resources/views/components/card.blade.php --}}
@props(['class' => ''])

<div {{ $attributes->merge(['class' => "bg-surface border border-border rounded-card shadow hover:shadow-lg transition overflow-hidden $class"]) }}>
    {{ $slot }}
</div>
```

### 9.4 Form Control / Input / Textarea

```blade
{{-- resources/views/components/form-control.blade.php --}}
@props(['label', 'name', 'required' => false])

<div>
    <label for="{{ $name }}" class="block text-sm font-medium mb-1">
        {{ $label }}@if($required) <span class="text-error">*</span>@endif
    </label>
    {{ $slot }}
    @error($name)
        <p class="text-xs text-error mt-1">{{ $message }}</p>
    @enderror
</div>
```

```blade
{{-- resources/views/components/input.blade.php --}}
@props(['name', 'type' => 'text', 'value' => ''])

<input type="{{ $type }}" name="{{ $name }}" id="{{ $name }}" value="{{ old($name, $value) }}"
       {{ $attributes->merge(['class' => 'w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary']) }}>
```

```blade
{{-- resources/views/components/textarea.blade.php --}}
@props(['name', 'rows' => 5, 'value' => ''])

<textarea name="{{ $name }}" id="{{ $name }}" rows="{{ $rows }}"
          {{ $attributes->merge(['class' => 'w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary resize-none']) }}>{{ old($name, $value) }}</textarea>
```

### 9.5 Pagination

```blade
{{-- resources/views/components/pagination.blade.php --}}
@props(['paginator'])

@if($paginator->hasPages())
<nav class="mt-12 flex justify-center items-center space-x-2" aria-label="Pagination">
    {{-- Previous --}}
    @if($paginator->onFirstPage())
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">&laquo; Previous</span>
    @else
        <a href="{{ $paginator->previousPageUrl() }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">&laquo; Previous</a>
    @endif

    {{-- Page Numbers --}}
    @foreach($paginator->getUrlRange(1, $paginator->lastPage()) as $page => $url)
        @if($page == $paginator->currentPage())
            <span class="px-3 py-2 text-sm font-bold text-white bg-primary rounded-btn">{{ $page }}</span>
        @else
            <a href="{{ $url }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary border border-border rounded-btn transition">{{ $page }}</a>
        @endif
    @endforeach

    {{-- Next --}}
    @if($paginator->hasMorePages())
        <a href="{{ $paginator->nextPageUrl() }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">Next &raquo;</a>
    @else
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">Next &raquo;</span>
    @endif
</nav>
@endif
```

### 9.6 Toast Container

```blade
{{-- resources/views/components/toast-container.blade.php --}}
<div x-data="toastStore" class="fixed top-20 right-4 z-50 space-y-2">
    <template x-for="toast in toasts" :key="toast.id">
        <div x-show="toast.visible"
             x-transition:enter="transform ease-out duration-300 transition"
             x-transition:enter-start="translate-x-full opacity-0"
             x-transition:enter-end="translate-x-0 opacity-100"
             x-transition:leave="transition ease-in duration-200"
             x-transition:leave-start="opacity-100"
             x-transition:leave-end="opacity-0"
             :class="{
                'bg-green-50 text-success border-success/30': toast.type === 'success',
                'bg-red-50 text-error border-error/30': toast.type === 'error',
             }"
             class="px-4 py-3 rounded-card border shadow text-sm font-medium max-w-sm"
             x-text="toast.message">
        </div>
    </template>
</div>
```

---

## 10. Frontend JS Structure

### 10.1 Main Entry (`app.js`)

```js
// resources/js/app.js
import Alpine from 'alpinejs';
import htmx from 'htmx.org';

// Alpine stores
import { toastStore } from './alpine/stores/toast.js';

// Register stores
Alpine.data('toastStore', toastStore);

// Start Alpine
Alpine.start();

// Make htmx available globally
window.htmx = htmx;

// htmx global error handler
document.addEventListener('htmx:responseError', (event) => {
    const xhr = event.detail.xhr;
    try {
        const trigger = JSON.parse(xhr.getResponseHeader('HX-Trigger'));
        if (trigger?.showToast) {
            Alpine.store('toast')?.add(trigger.showToast);
        }
    } catch {
        // Non-JSON trigger, ignore
    }
});
```

### 10.2 Toast Store

```js
// resources/js/alpine/stores/toast.js
export function toastStore() {
    return {
        toasts: [],
        add({ type = 'info', message = '' }) {
            const id = Date.now();
            this.toasts.push({ id, type, message, visible: true });
            setTimeout(() => {
                this.toasts = this.toasts.filter(t => t.id !== id);
            }, 5000);
        },
    };
}
```

### 10.3 Carousel Component

```js
// resources/js/alpine/components/carousel.js
export function carousel(slides, autoSlideMs = 5000) {
    return {
        current: 0,
        slides,
        autoSlide: null,
        next() { this.current = (this.current + 1) % this.slides.length; },
        prev() { this.current = (this.current - 1 + this.slides.length) % this.slides.length; },
        startAuto() { this.autoSlide = setInterval(() => this.next(), autoSlideMs); },
        stopAuto() { clearInterval(this.autoSlide); },
        init() { this.startAuto(); },
    };
}
```

---

## 11. Styles

### 11.1 Main Entry (`app.css`)

```css
/* resources/css/app.css */
@import 'tailwindcss';

@import './base/reset.css';
@import './base/typography.css';
@import './components/buttons.css';
@import './components/forms.css';
@import './components/cards.css';
@import './components/navigation.css';
@import './layouts/navbar.css';
@import './layouts/footer.css';
```

### 11.2 Base Reset

```css
/* resources/css/base/reset.css */
@layer base {
    html { scroll-behavior: smooth; }
    body { @apply antialiased; }
}
```

### 11.3 Typography

```css
/* resources/css/base/typography.css */
@layer base {
    body {
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
                     Oxygen-Sans, Ubuntu, Cantarell, 'Helvetica Neue', sans-serif;
    }
}
```

---

## 13. Data Access

### PostgreSQL — Shared Database (Read-Only)

The Landing Page connects to the shared `cms_db` PostgreSQL database. All tables are owned and migrated by the Admin Portal. **The Landing Page does NOT create migrations.**

#### Eloquent Models

Each module has an Eloquent model mapping to the Admin Portal's tables. Models use:
- `HasUuids` trait for UUID primary keys
- `$table` property set to the Admin Portal's table name (e.g., `hrs_hero_section`)
- `$timestamps = false` where the Landing Page does not modify records (or map `created_at`/`updated_at` as read-only)
- Read-only scopes for filtering active/visible content

#### BLOB Image Serving

Modules with BLOB-stored images (Hero, Product/Service, Team, Blog) need a shared `ImageController` that:
1. Accepts `/{module}/{id}/image` and `/{module}/{id}/thumbnail` routes
2. Reads the `image_data` or `thumbnail_data` BYTEA column
3. Returns the binary content with `Content-Type: image/png` (or detected type) and caching headers

```php
namespace App\Http\Controllers;

use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Cache;

class ImageController extends Controller
{
    public function show(string $table, string $id, string $column): Response
    {
        $allowedTables = [
            'hero-section' => ['table' => 'hrs_hero_section', 'columns' => ['image_data', 'thumbnail_data']],
            'product-service' => ['table' => 'pas_product_service', 'columns' => ['image_data', 'thumbnail_data']],
            'team-member' => ['table' => 'tms_team_member', 'columns' => ['image_data']],
            'blog-post' => ['table' => 'blg_blog_post', 'columns' => ['image_data', 'thumbnail_data']],
        ];

        $config = $allowedTables[$table] ?? null;
        if (!$config || !in_array($column, $config['columns'])) {
            abort(404);
        }

        $cacheKey = "img:{$table}:{$id}:{$column}";
        $data = Cache::remember($cacheKey, 3600, function () use ($config, $id, $column) {
            return DB::table($config['table'])
                ->where('id', $id)
                ->value($column);
        });

        if (!$data) {
            abort(404);
        }

        $mime = $this->detectMimeType($data);

        return response($data)
            ->header('Content-Type', $mime)
            ->header('Cache-Control', 'public, max-age=86400');
    }

    private function detectMimeType(string $data): string
    {
        $finfo = new \finfo(FILEINFO_MIME_TYPE);
        return $finfo->buffer($data) ?: 'application/octet-stream';
    }
}
```

#### DTO Mapping via spatie/laravel-data

```php
// Model → DTO mapping
$dto = HeroSectionData::from($heroSectionModel);

// Collection mapping
$dtos = HeroSectionData::collect($models);
```

---

## 14. Error Handling & Exceptions

### Base Exception

```php
namespace App\Exceptions;

use RuntimeException;

class WebApplicationException extends RuntimeException
{
    public function __construct(
        string $message,
        int $code = 500,
        public readonly ?string $userMessage = null,
    ) {
        parent::__construct($message, $code);
    }
}
```

### Handler

```php
namespace App\Exceptions;

use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;
use Throwable;

class Handler extends ExceptionHandler
{
    public function render($request, Throwable $e)
    {
        if ($request->header('HX-Request')) {
            $statusCode = method_exists($e, 'getStatusCode') ? $e->getStatusCode() : 500;
            return response('', $statusCode)
                ->header('HX-Trigger', json_encode([
                    'showToast' => [
                        'type' => 'error',
                        'message' => $e instanceof WebApplicationException
                            ? ($e->userMessage ?? $e->getMessage())
                            : 'An unexpected error occurred.',
                    ],
                ]));
        }

        return parent::render($request, $e);
    }
}
```

---

## 16. Pagination Support

Blog listing is the only paginated endpoint (10 per page per CONSL0009).

```php
// In BlogService
public function listActivePosts(int $page = 1, int $perPage = 10): LengthAwarePaginator
{
    return BlogPost::where('status', 'ACTIVE')
        ->where('effective_date', '<=', now())
        ->where('expiration_date', '>=', now())
        ->orderByDesc('effective_date')
        ->paginate($perPage, ['*'], 'page', $page);
}
```

Use the `<x-pagination>` component (Section 9.5) in the Blog list template.

---

## 17. Logging Strategy

### Correlation ID Middleware

```php
namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Str;
use Symfony\Component\HttpFoundation\Response;

class CorrelationIdMiddleware
{
    public function handle(Request $request, Closure $next): Response
    {
        $correlationId = $request->header('X-Correlation-ID', Str::uuid()->toString());

        Log::withContext(['correlation_id' => $correlationId]);

        $response = $next($request);
        $response->headers->set('X-Correlation-ID', $correlationId);

        return $response;
    }
}
```

Register in `bootstrap/app.php`:

```php
->withMiddleware(function (Middleware $middleware) {
    $middleware->web(append: [
        \App\Http\Middleware\CspNonceMiddleware::class,
        \App\Http\Middleware\CorrelationIdMiddleware::class,
    ]);
})
```

---

## 19. Event-Driven Architecture

Minimal event usage for this public-facing landing page. The only write operation is contact form submission:

```php
namespace Modules\ContactSection\Events;

use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ContactMessageReceivedEvent
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly string $messageId,
        public readonly string $senderName,
        public readonly string $senderEmail,
        public readonly \DateTimeImmutable $occurredAt,
    ) {}
}
```

---

## 21. DTO & Data Transformation

Per-module `spatie/laravel-data` DTO pattern:

```php
// Model → DTO (read path)
$dto = HeroSectionData::from($model);

// Collection of DTOs
$dtos = HeroSectionData::collect($models);

// Request → DTO (write path, Contact only)
$dto = ContactMessageData::from($request->validated());
```

DTOs are immutable value objects with `readonly` properties. They decouple the Eloquent model from the view layer.

---

## 22. Testing Strategy

### Unit Tests (PHPUnit)
- Service layer tests per module (mocking Eloquent models)
- DTO transformation tests
- Image MIME detection tests

### Feature Tests (Laravel HTTP Tests)
- Home page renders all sections
- Blog list pagination
- Blog detail by slug (SEO-friendly URL)
- Contact form submission and validation
- Image endpoint returns correct content type
- 404 for non-existent blog posts

### Architecture Tests (phpat/phpat)

```php
namespace Tests\Architecture;

use PHPat\Selector\Selector;
use PHPat\Test\Builder\Rule;
use PHPat\Test\PHPat;

class ModuleBoundaryTest
{
    public function test_modules_only_depend_on_public_api(): Rule
    {
        return PHPat::rule()
            ->classes(Selector::inNamespace('Modules\Blog'))
            ->canOnlyDependOn()
            ->classes(
                Selector::inNamespace('Modules\Blog'),
                Selector::inNamespace('Illuminate'),
                Selector::inNamespace('App'),
                Selector::inNamespace('Spatie'),
            );
    }
}
```

---

## Core Routes

### routes/web.php

```php
use App\Http\Controllers\HomeController;
use App\Http\Controllers\ImageController;
use Illuminate\Support\Facades\Route;

// Home page (composes all sections)
Route::get('/', [HomeController::class, 'index'])->name('home');

// Image serving endpoint (BLOB)
Route::get('/images/{table}/{id}/{column}', [ImageController::class, 'show'])
    ->name('image.show')
    ->where('table', 'hero-section|product-service|team-member|blog-post')
    ->where('column', 'image_data|thumbnail_data');
```

### HomeController

The `HomeController` composes data from all landing page modules into a single view:

```php
namespace App\Http\Controllers;

use Illuminate\View\View;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\FeaturesSection\Contracts\FeatureServiceInterface;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;

class HomeController extends Controller
{
    public function __construct(
        private readonly HeroSectionServiceInterface $heroService,
        private readonly ProductServiceServiceInterface $productService,
        private readonly FeatureServiceInterface $featureService,
        private readonly TestimonialServiceInterface $testimonialService,
        private readonly TeamMemberServiceInterface $teamService,
        private readonly ContactSectionServiceInterface $contactService,
    ) {}

    public function index(): View
    {
        return view('pages.home', [
            'heroSlides' => $this->heroService->getActiveSlides(),
            'products' => $this->productService->getAllVisible(),
            'features' => $this->featureService->getAllVisible(),
            'testimonials' => $this->testimonialService->getAllVisible(),
            'teamMembers' => $this->teamService->getAllVisible(),
            'contactInfo' => $this->contactService->getContactInfo(),
        ]);
    }
}
```
