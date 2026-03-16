# Simple CMS — CO2 Workflow Example

This repository demonstrates the [Compound Context (CO2)](https://github.com/rashidee/co2-skills) AI coding agent workflow by building a simple content management system from structured requirements through to working code.

## What is CO2?

Compound Context (CO2) is an opinionated approach to software development using AI coding agents. It emphasizes providing agents with comprehensive, structured context — user stories, data models, mockups, specifications, and test plans — to minimize hallucination and produce higher-quality outputs.

For full details on the workflow and available skills, see the [co2-skills](https://github.com/rashidee/co2-skills) repository.

## About This Project

**Simple CMS** allows small business owners to create and manage marketing pages and blog content without technical skills.

The marketing page includes sections for: Hero, Products & Services, Features, Testimonials, Contact, and Team. The blog supports creating/editing/deleting posts, categories, tags, comments, and search.

### Applications

| Application | Stack | Description |
|---|---|---|
| **Admin Portal** (`admin/`) | Spring Boot 3, JTE, Tailwind CSS, Alpine.js, HTMX | Back-office web app for managing marketing page and blog content |
| **Landing Page** (`landing/`) | Laravel 12, Eloquent, Blade, HTMX | Public-facing page displaying marketing and blog content |

Both applications share a PostgreSQL 14 database (`cms_db`).

## Repository Structure

```
├── CLAUDE.md              # Project-level context for the AI agent
├── SECRET.md              # Paths and credentials (git-ignored)
├── admin/                 # Admin Portal application
│   ├── context/           # CO2 context artifacts
│   │   ├── PRD.md         #   User stories, NFRs, constraints
│   │   ├── BUG.md         #   Bug reports
│   │   ├── model/         #   Data models (ERD, schemas)
│   │   ├── mockup/        #   HTML mockup screens
│   │   ├── specification/ #   Technical specifications
│   │   ├── test/          #   Test plans and specs
│   │   ├── develop/       #   Development tracking
│   │   └── bug/           #   Bug fix tracking
│   └── src/               # Spring Boot source code
├── landing/               # Landing Page application
│   ├── context/           # CO2 context artifacts (same structure)
│   ├── app/               # Laravel source code
│   ├── resources/         # Blade views, assets
│   └── e2e/               # Playwright E2E tests
```

## CO2 Workflow Demonstrated

This project was built end-to-end using the CO2 workflow:

1. **Requirements** — User stories, NFRs, and constraints written in `PRD.md` per application
2. **Data Modeling** — Module models generated from user stories (`modelgen-relational`)
3. **Mockups** — HTML mockup screens generated for UI review (`mockgen-tailwind`)
4. **Specifications** — Technical specifications generated per tech stack (`specgen-spring-jpa-jtehtmx`, `specgen-laravel-eloquent-bladehtmx`)
5. **Test Specifications** — Playwright E2E test plans generated from artifacts (`testgen-functional`)
6. **Development** — Module-by-module implementation orchestrated by the agent (`conductor-feature-develop`)
7. **Bug Fixing** — Bugs reported in `BUG.md` and systematically fixed (`conductor-defect`)
8. **Version Upgrades** — Iterative version upgrades combining bug fixes and new features (`conductor-upgrade-version`)

## Getting Started

### Prerequisites

- Java 21+ (for Admin Portal)
- PHP 8.2+ / Composer (for Landing Page)
- Node.js 18+ (for Tailwind CSS builds and Playwright tests)
- PostgreSQL 14

### Running the Applications

Refer to the individual application READMEs:
- [Admin Portal](admin/README.md)
- [Landing Page](landing/README.md)

## License

This project is provided as an example for the CO2 workflow. See the [co2-skills](https://github.com/rashidee/co2-skills) repository for more information.
