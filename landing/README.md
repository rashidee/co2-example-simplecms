# Landing Page

A public-facing landing page that displays marketing content and blog content for Simple CMS. The application reads data from a shared PostgreSQL database (`cms_db`) managed by the Admin Portal, and serves it as a responsive, modern marketing website with a top navigation bar, hero carousel, product/service cards, feature highlights, testimonial carousel, team profiles, contact form, and a paginated blog directory.

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| PHP | 8.3+ | Runtime |
| Laravel | 12.x | Web framework |
| Blade | Built-in | Template engine |
| Tailwind CSS | 4.x | Utility-first CSS framework |
| Alpine.js | 3.x | Lightweight JS framework (carousels, toasts, mobile menu) |
| htmx | 2.x | HTML-over-the-wire (contact form submission) |
| Vite | 7.x | Frontend build tool |
| PostgreSQL | 14.x | Shared database (read-only, except contact form writes) |
| nwidart/laravel-modules | 12.x | Modular monolith packaging |
| spatie/laravel-data | 4.x | DTO / data transformation |
| spatie/laravel-view-models | 1.6 | View model pattern |
| Playwright | Latest | E2E testing |

## Architecture

The application follows a **modular monolith** architecture using `nwidart/laravel-modules`. Each business module is self-contained with its own contracts, DTOs, models, services, exceptions, and (where applicable) controllers, routes, and views. Shared infrastructure lives in the `app/` directory.

**Key architectural decisions:**
- **No migrations** — The Admin Portal owns all database schema. This app only reads existing tables (and writes to `cts_contact_message` for contact form submissions).
- **BLOB image serving** — Images stored as BYTEA in PostgreSQL are served via a shared `ImageController` with caching headers.
- **Module-based URLs** — All routes use module-based paths, not role-based paths.
- **DTO layer** — Eloquent models are mapped to Spatie Data DTOs before reaching views, decoupling the database layer from the presentation layer.

## Folder Structure

```
landing/
  app/                              # Shared application core
    Exceptions/                     # WebApplicationException base
    Http/
      Controllers/                  # HomeController, ImageController
      Middleware/                   # CorrelationIdMiddleware
  Modules/                          # Feature modules (nwidart/laravel-modules)
    HeroSection/                    # Hero carousel section
    ProductAndServiceSection/       # Product/service grid
    FeaturesSection/                # Features grid with icons
    TestimonialsSection/            # Testimonial carousel
    TeamSection/                    # Team member grid
    ContactSection/                 # Contact info + form (only write operation)
    Blog/                           # Blog directory + detail pages
  resources/
    css/                            # Tailwind CSS with custom theme
    js/                             # Alpine.js stores, components, htmx config
    views/
      components/                   # Shared Blade components (layouts, UI)
      partials/                     # Navbar, footer
      pages/                        # Home page (composes all sections)
  routes/
    web.php                         # Core routes (home, image serving)
  e2e/                              # Playwright E2E test suite
    tests/                          # Test files per module
    helpers/                        # DB helper utilities
  context/                          # Context artifacts (not source code)
```

## Prerequisites

- PHP 8.3+
- Composer 2.x
- Node.js 22.x LTS
- PostgreSQL 14.x (shared `cms_db` database, managed by Admin Portal)

## Getting Started

### Installation

```bash
composer install
npm install
```

### Configuration

Copy `.env.example` to `.env` and configure the database connection:

```env
DB_CONNECTION=pgsql
DB_HOST=localhost
DB_PORT=5432
DB_DATABASE=cms_db
DB_USERNAME=cms_user
DB_PASSWORD=cms_password
```

Generate an application key if not already set:

```bash
php artisan key:generate
```

### Building Frontend Assets

```bash
npm run build
```

For development with hot-reload:

```bash
npm run dev
```

### Running the Application

```bash
php artisan serve --port=8000
```

The application will be accessible at `http://localhost:8000`.

### Running Tests

```bash
# E2E tests (requires the application to be running on port 8000)
cd e2e && npx playwright test

# Run a specific module's tests
cd e2e && npx playwright test tests/hero-section.spec.ts
```

## Modules

| Module | Layer | Description |
|--------|-------|-------------|
| Hero Section | L2 | Full-width carousel with background images, headlines, and CTA buttons. Auto-slides every 5 seconds with manual navigation. |
| Product and Service Section | L2 | 3-column responsive grid of product/service cards with images, titles, descriptions, and CTA links. |
| Features Section | L2 | 3-column responsive grid of feature cards with FontAwesome icons, titles, and descriptions. |
| Testimonials Section | L2 | Single-slide carousel with star ratings, customer reviews, and navigation controls. |
| Team Section | L2 | 3-column responsive grid of team member cards with circular profile photos, names, roles, and LinkedIn links. |
| Contact Section | L2 | Two-column layout: contact info (phone, email, address) on the left, htmx-powered contact form on the right. |
| Blog | L2 | Paginated blog directory (10 per page) with horizontal card layout. SEO-friendly slug URLs for detail pages with hero image overlay and prose content. |

## Routes

| Method | URI | Description |
|--------|-----|-------------|
| GET | `/` | Home page (all sections) |
| GET | `/blog` | Blog directory (paginated) |
| GET | `/blog/{slug}` | Blog detail page |
| POST | `/contact` | Contact form submission (htmx) |
| GET | `/images/{table}/{id}/{column}` | BLOB image serving endpoint |
