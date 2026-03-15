# Simple CMS Landing Page

A public-facing website that displays marketing page content and blog posts managed through the Simple CMS Admin Portal. Built with Laravel 12, Blade templates, Tailwind CSS, Alpine.js, and htmx, using a modular architecture powered by nwidart/laravel-modules. The application is read-only (no authentication required) with the exception of a contact form submission feature.

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| PHP | 8.3+ | Runtime |
| Laravel | 12.x | Backend framework |
| Blade | (bundled) | Server-side template engine |
| Tailwind CSS | 4.x | Utility-first CSS framework |
| Alpine.js | 3.x | Lightweight client-side reactivity |
| htmx | 2.x | Partial page updates and AJAX interactions |
| Vite | 6.x | Frontend asset bundling |
| PostgreSQL | 17.x | Relational database (shared with Admin Portal) |
| nwidart/laravel-modules | latest | Modular architecture for feature isolation |
| spatie/laravel-data | latest | Data transfer objects (DTOs) |
| spatie/laravel-view-models | latest | View model encapsulation |

## Architecture

The application follows a **nwidart/laravel-modules** modular architecture. Each marketing section and the blog are isolated into self-contained modules under the `Modules/` directory. Shared layouts, partials, and frontend assets live in the standard Laravel `resources/` directory.

### Layer Conventions (per module)

| Layer | Responsibility |
|-------|---------------|
| Model (Eloquent) | Database access, read-only queries against the shared `cms_db` |
| Service | Business logic, data retrieval and transformation |
| Data (spatie/laravel-data) | DTOs for passing structured data from services to views |
| ViewModel (spatie/laravel-view-models) | View-specific logic encapsulation |
| Controller | HTTP request handling, view rendering |
| Blade Views / Components | Server-rendered HTML templates |

### Key Design Decisions

- **No database migrations** — all tables are created and managed by the Admin Portal. This application only reads from the shared `cms_db`.
- **No authentication** — the site is fully public (anonymous visitors only).
- **Contact form** is the only write operation (stores messages to the database).

## Folder Structure

```
landing/
  app/
    Http/                              # Application HTTP kernel
    Models/                            # (empty - models live in modules)
    Providers/                         # Service providers
    View/Components/                   # Shared Blade components
  bootstrap/                           # Framework bootstrap
  config/                              # Application configuration
  database/                            # (empty - no migrations)
  Modules/                             # nwidart/laravel-modules root
    HeroSection/
      app/
        Http/Controllers/              # Hero section controller
        Models/                        # HeroSection Eloquent model
        Services/                      # Data retrieval service
        Data/                          # DTO classes
        ViewModels/                    # View model classes
      resources/views/components/      # Blade components
      routes/web.php                   # Module routes
    ProductAndServiceSection/          # Same structure as above
    FeaturesSection/
    TestimonialsSection/
    TeamSection/
    ContactSection/
      app/
        Http/
          Controllers/                 # Contact controller (read + form submit)
          Requests/                    # StoreContactMessageRequest validation
        Models/                        # ContactInfo, ContactMessage models
        Services/
        Data/
        ViewModels/
      resources/views/components/
      routes/web.php
    Blog/
      app/
        Http/Controllers/              # Blog listing and detail
        Models/                        # BlogCategory, BlogPost models
        Services/
        Data/
        ViewModels/
      resources/views/
        directory.blade.php            # Blog listing page
        detail.blade.php               # Blog post detail page
      routes/web.php
  public/                              # Public web root
  resources/
    css/app.css                        # Tailwind CSS entry with design tokens
    js/app.js                          # Alpine.js + htmx initialization
    views/
      layouts/
        landing.blade.php              # Marketing page layout
        blog.blade.php                 # Blog layout
      partials/
        navbar.blade.php               # Navigation bar
        footer.blade.php               # Footer
      pages/
        home.blade.php                 # Home page (assembles all marketing sections)
      errors/
        404.blade.php                  # Not found page
        500.blade.php                  # Server error page
  routes/
    web.php                            # Application-level routes
  e2e/                                 # Playwright E2E tests
    playwright.config.ts
    tests/
    package.json
  composer.json                        # PHP dependencies
  package.json                         # JS dependencies
  vite.config.js                       # Vite build configuration
  modules_statuses.json                # nwidart module activation status
  artisan                              # Laravel CLI
```

## Prerequisites

- **PHP 8.3+** with extensions: pdo_pgsql, mbstring, openssl, tokenizer, xml, ctype, json
- **Composer** (latest)
- **Node.js 20.x** and **npm**
- **PostgreSQL 17.x** with the `cms_db` database (created and managed by the Admin Portal)

## Getting Started

### Installation

```bash
cd landing
composer install
npm install
```

### Configuration

1. Copy the environment file:

```bash
cp .env.example .env
```

2. Generate the application key:

```bash
php artisan key:generate
```

3. Configure the database connection in `.env`:

```dotenv
DB_CONNECTION=pgsql
DB_HOST=localhost
DB_PORT=5432
DB_DATABASE=cms_db
DB_USERNAME=cms_user
DB_PASSWORD=cms_password
```

The Landing Page shares the same `cms_db` database as the Admin Portal. Ensure the Admin Portal has been set up and its migrations have run before starting the Landing Page.

### Building Frontend Assets

For development (with hot reload):

```bash
npm run dev
```

For production:

```bash
npm run build
```

### Running the Application

```bash
php artisan serve
```

The application will be accessible at **http://localhost:8000**.

### Running Tests

**Playwright E2E tests** (requires the application to be running):

```bash
cd e2e
npm install
npx playwright test
```

**PHPUnit tests:**

```bash
php artisan test
```

## Modules

| # | Module | Description |
|---|--------|-------------|
| 1 | Hero Section | Displays the hero banner with title, description, and call-to-action button |
| 2 | Product & Service Section | Showcases products and services with photos and descriptions |
| 3 | Features Section | Highlights key features with icons and descriptions |
| 4 | Testimonials Section | Displays customer testimonials with photos |
| 5 | Team Section | Shows team member profiles with photos |
| 6 | Contact Section | Displays contact information, map, and a contact form for visitor submissions |
| 7 | Blog | Blog post listing with category filtering, search, and individual post detail pages with comments |
