# Simple CMS Admin Portal

A web application for managing marketing page content and blog posts for the Simple CMS platform. Built with Spring Boot 3, JTE templates, Tailwind CSS, Alpine.js, and htmx, following a Spring Modulith architecture with PostgreSQL as the data store.

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java (Eclipse Temurin) | 21 | Runtime |
| Spring Boot | 3.5.7 | Application framework |
| Spring Modulith | 1.3.x | Module boundary enforcement and event-driven communication |
| Spring Data JPA / Hibernate | (managed by Boot) | ORM and data access |
| Spring Security | (managed by Boot) | Form-based authentication and role-based authorization |
| JTE (Java Template Engine) | 3.1.x | Server-side HTML rendering |
| Tailwind CSS | 4.x | Utility-first CSS framework |
| htmx | 2.0.x | Partial page updates and AJAX interactions |
| Alpine.js | 3.14.x | Lightweight client-side reactivity |
| PostgreSQL | 14 | Relational database |
| Flyway | (managed by Boot) | Database schema migrations |
| Quartz (JDBC Job Store) | (managed by Boot) | Scheduled job execution |
| MapStruct | 1.6.x | Java bean mapping |
| imgscalr | 4.2 | Server-side image resizing |
| OWASP Java HTML Sanitizer | 20240325.1 | Rich-text input sanitization |
| Vite | 6.x | Frontend asset bundling |
| FontAwesome | 6.x (CDN) | Icon library |
| TinyMCE | 7.x (CDN) | Rich text editor for blog content |
| Maven | 3.9+ | Build tool |

## Architecture

The application follows a **Spring Modulith monolith** architecture. Each business domain is packaged as a top-level module under `com.simplecms.adminportal`, with an `internal` sub-package for implementation details that are not exposed to other modules. Cross-module communication uses Spring Modulith's event publication mechanism.

### Layer Conventions (per module)

| Layer | Responsibility |
|-------|---------------|
| Entity (JPA) | Domain model, mapped to PostgreSQL tables |
| Repository | Spring Data JPA interfaces for data access |
| Service | Business logic, transaction boundaries |
| Mapper (MapStruct) | DTO ↔ Entity conversion |
| Controller | HTTP request handling, view model preparation |
| JTE Templates | Server-rendered HTML views |

### Roles

| Role | Access |
|------|--------|
| ADMIN | User management, profile, account |
| EDITOR | All marketing sections (Hero, Products & Services, Features, Testimonials, Team, Contact), blog categories and posts, profile, account |
| USER | Home dashboard, profile, account |

## Folder Structure

```
admin/
  pom.xml                                    # Maven build configuration
  src/
    main/
      java/com/simplecms/adminportal/
        AdminPortalApplication.java          # Spring Boot entry point
        config/                              # Global configuration
        shared/                              # Cross-cutting concerns
          config/                            # Shared beans and config
          exception/                         # Global exception handlers
          model/                             # Base entity, audit fields
          scheduling/                        # Quartz job infrastructure
          security/                          # Spring Security config, UserDetails
          vite/                              # Vite manifest integration
        authentication/                      # Login, logout, session management
          internal/
        user/                                # User CRUD (ADMIN), profile, account
          internal/
        herosection/                         # Hero section CRUD (EDITOR)
          internal/
        productservice/                      # Products & Services CRUD (EDITOR)
          internal/
        feature/                             # Features section CRUD (EDITOR)
          internal/
        testimonial/                         # Testimonials CRUD (EDITOR)
          internal/
        teamsection/                         # Team section CRUD (EDITOR)
          internal/
        contactsection/                      # Contact section & messages (EDITOR)
          internal/
        blog/                                # Blog posts & categories (EDITOR)
          internal/
      jte/                                   # JTE template files
        layout/                              # Shared layouts (main, auth, error)
        component/                           # Reusable UI components
        fragment/                            # Shared fragments (sidebar, header)
        authentication/                      # Login/logout views
        user/                                # User management views
        herosection/                         # Hero section views
        productservice/                      # Product & Service views
        feature/                             # Features section views
        testimonial/                         # Testimonials views
        teamsection/                         # Team section views
        contactsection/                      # Contact section views
        blog/                                # Blog views
        error/                               # Error pages
      frontend/                              # Vite + Tailwind + Alpine.js + htmx
        src/
          main.js                            # JS entry (Alpine.js, htmx init)
          main.css                           # Tailwind CSS entry
        vite.config.js
        tailwind.config.js
        package.json
      resources/
        application.yml                      # Default configuration
        application-dev.yml                  # Development overrides
        db/migration/                        # Flyway SQL migrations
  e2e/                                       # Playwright E2E tests
    playwright.config.ts
    tests/
    package.json
```

## Prerequisites

- **Java 21** (Eclipse Temurin recommended)
- **Maven 3.9+**
- **Node.js 20.x** and **npm 10.x** (downloaded automatically by frontend-maven-plugin during build)
- **PostgreSQL 14** with a database named `cms_db`

## Getting Started

### Database Setup

Create the PostgreSQL database and user:

```sql
CREATE DATABASE cms_db;
CREATE USER cms_user WITH PASSWORD 'cms_password';
GRANT ALL PRIVILEGES ON DATABASE cms_db TO cms_user;
```

Flyway will automatically run migrations on application startup.

### Installation

```bash
cd admin
mvn clean install
```

This will:
1. Download Node.js and npm (via frontend-maven-plugin)
2. Install frontend dependencies (`npm ci`)
3. Build frontend assets with Vite (output to `src/main/resources/static/`)
4. Compile Java sources and JTE templates
5. Run tests

### Configuration

The default configuration in `src/main/resources/application.yml` connects to PostgreSQL at `localhost:5432/cms_db` with credentials `cms_user`/`cms_password`.

For development, activate the `dev` profile to enable JTE development mode, Spring DevTools live reload, and verbose SQL logging:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

For production, set these environment variables:

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | JDBC connection string (e.g., `jdbc:postgresql://host:5432/cms_db`) |
| `DATABASE_USERNAME` | Database username |
| `DATABASE_PASSWORD` | Database password |

### Running the Application

```bash
cd admin
mvn spring-boot:run
```

The application will be accessible at **http://localhost:8080**.

### Running Tests

**Playwright E2E tests** (requires the application to be running):

```bash
cd admin/e2e
npm install
npx playwright test
```

**Java unit/integration tests:**

```bash
cd admin
mvn test
```

## Modules

| # | Module | Layer | Description |
|---|--------|-------|-------------|
| 1 | User | L1 (System) | User management (CRUD for ADMIN role), profile and account settings for all roles |
| 2 | Authentication and Authorization | L1 (System) | Form-based login/logout, session management, role-based access control |
| 3 | Hero Section | L2 (Business) | Manage hero banner content — title, description, CTA button, background image |
| 4 | Product and Service Section | L2 (Business) | Manage product/service listings with photos and descriptions |
| 5 | Features Section | L2 (Business) | Manage feature highlights with icons and descriptions |
| 6 | Testimonials Section | L2 (Business) | Manage customer testimonials with photos |
| 7 | Team Section | L2 (Business) | Manage team member profiles with photos |
| 8 | Contact Section | L2 (Business) | Manage contact form configuration, map settings, and view submitted messages |
| 9 | Blog | L2 (Business) | Blog post and category management — create, edit, delete, categorize, and publish posts |
