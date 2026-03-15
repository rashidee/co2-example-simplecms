# Context
- This document is for coding agent to understand the project as a whole, including the project detail, goals and terminology.
- This document will be used as reference in any of the tasks related to this project.

# Project Detail
- Project Code: SCMS
- Project Name: Simple CMS
- Project Description: 
  - A simple content management system which allows users to create marketing page and blog content
  - The marketing page will have sections such as:
    - Hero section with title, description and call-to-action button
    - Product and Service section with list of products and services and photo
    - Features section with list of features and icons
    - Testimonials section with customer testimonials and photos
    - Contact section with contact form and map
    - Team section with team member profiles and photos
  - The blog content will have features such as:
    - Create, edit and delete blog posts
    - Categorize blog posts with tags and categories
    - Comment on blog posts
    - Search blog posts by keywords and tags

# Goals
- To enable small business owners to easily create and manage their marketing pages and blog content without needing technical skills.

# Terminology
- CMS: Content Management System

# Supporting 3rd Party Applications
- Below are the list of 3rd party applications which will be used as part of this project:

## CMS Database
- A PostgresSQL database version 14.
- Database name:
  - cms_db

# Custom Applications
- Below are the list of applications which will be custom developed as part of this project.:

## Admin Portal
- A custom developed web application to manage the content of the marketing page and blog.
- Technology stack: Spring Boot, JTE, Tailwind CCS, Alpine.js, HTMX
- Depends on:
  - CMS Database for storing the content data and user data.

## Landing Page
- A landing page to display the marketing content and blog content to the public.
- Technology stack: Laravel, Eloquent, Blade, HTMX
- Depends on:
  - CMS Database for fetching the content data and user data.

# Modules

## System Module

### Authentication and Authorization
- Information about the authentication and authorization of users in the system.

### User
- Information about the users of the system.

## Business Module

### Hero Section
- Information about the hero section of the marketing page.

### Product and Service Section
- Information about the product and service section of the marketing page.

### Features Section
- Information about the features section of the marketing page.

### Testimonials Section
- Information about the testimonials section of the marketing page.

### Contact Section
- Information about the contact section of the marketing page.

### Team Section
- Information about the team section of the marketing page.

### Blog
- Information about the blog content and features.

# Folder structure
- The project folder structure is as follows:
  - <application_folder>: Each application has its own root-level folder containing both context and source code.
    - context: The context folder for the application.
      - model: The module data model for the application. Output by the "modelgen-*" skill.
      - mockup: The HTML mockups for the UI design. Output by the "mockgen-tailwind" skill.
      - specification: The detailed specification for the application. Output by the "specgen-*" skill.
      - test: The test specification for the application. Output by the "testgen-*" skill.
      - develop: The planning and tracking output during the development phase. Output by the "conductor-feature".
      - bug: The planning and tracking output during the bug fixing phase. Output by the "conductor-defect".
      - PRD.md: The user stories, NFRs and Constraints (by modules) input by user. Used by all the skills above as reference.
      - BUG.md: The bug reported by user. Used by "conductor-defect" skill as reference.
    - (source code files): The source code for the application. Output by the "conductor-feature" skill.

# Path and Credentials
**Refer to [SECRET.md](SECRET.md) for the path and credentials for the 3rd party applications.**

# Rules

## Playwright Screenshots
- When taking screenshots using Playwright MCP `browser_take_screenshot`, ALWAYS save to the `.playwright-mcp/` folder by prefixing the filename (e.g., `.playwright-mcp/my-screenshot.png`).
- NEVER use bare relative filenames (e.g., `screenshot.png`) — they will be saved to the project root and pollute the repository.