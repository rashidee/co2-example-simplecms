# Context
- This document contain user stories, non-functional requirements, constraints and references by module about this application
- The application name can be inferred from the root folder name where this file is located.
- This is a version controlled document, the version is indicated in the square bracket after each section title, and it will be updated whenever there is any change in the content of that section. The versioning format is [v{major}.{minor}.{patch}] where:
  - Major version will be updated when there is a significant change in the content that may affect the overall understanding of the module or the system.
  - Minor version will be updated when there is a minor change in the content that may affect some details but not the overall understanding of the module or the system.
  - Patch version will be updated when there is a small change in the content that does not affect the overall understanding of the module or the system, such as fixing typos or formatting issues.
- For user any item which is no longer valid or applicable, it will be marked with strikethrough and indicated in the new version.

---

# Standards
- Technical standards which should be referenced all the time in the development of the system.

## UI/UX
- The general UI design is meant for landing page.
- Color and font
  - Primary color: #2271b1 (blue) — used for primary buttons, active states, links, and focused elements.
  - Secondary color: #135e96 (dark blue) — used for hover states on primary elements.
  - Sidebar background: #1d2327 (dark charcoal) — used for the admin sidebar navigation background.
  - Sidebar text: #f0f0f1 (light gray) — used for sidebar menu text and icons.
  - Page background: #f0f0f1 (light gray) — used for the main content area background.
  - Surface/card background: #ffffff (white) — used for cards, panels, form containers, and content blocks.
  - Text primary: #1e1e1e (near black) — used for headings and body text.
  - Text secondary: #646970 (medium gray) — used for helper text, labels, and secondary information.
  - Border color: #c3c4c7 (gray) — used for input borders, dividers, and card outlines.
  - Success color: #00a32a (green) — used for success messages, active status badges, and confirmations.
  - Warning color: #dba617 (amber) — used for warning messages and draft/pending status badges.
  - Error color: #d63638 (red) — used for error messages, validation errors, and destructive action buttons.
  - Border radius: 4px for buttons and inputs, 8px for cards and panels.
  - Font family: System font stack (-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif).
- Basic  layout structure:
  - Top navigation bar with logo and menu items
    - Menu Items: Home, Product and Service, Features, Testimonials, Contact, Team, Blog
  - Hero section with a large background image and a call-to-action button
  - Product and service section with a grid layout to showcase the products and services offered
  - Features section with a list of features and benefits of the products and services
  - Testimonials section with customer reviews and ratings
  - Team section with a list of team members and their roles
  - Contact section with a contact form and contact information
- Blog menu will open a new page with the blog directory.
- Use modern and clean design with a consistent color scheme and typography throughout the landing page.

## Coding
- Blog article URL should be SEO friendly, which means it should be in the format of `/{blog-slug}` where `blog-slug` is a lowercase string with words separated by hyphens, and it should be unique for each blog article.

---

# System Module

## Authentication and Authorization

### User Story

[v1.0.0]

### Non Functional Requirement

[v1.0.0]

### Constraint

[v1.0.0]
- [CONSL0003] This is a landing page with public user access, so there is no authentication and authorization required for accessing the content of the landing page.

### Reference

[v1.0.0]

---

## User

### User Story

[v1.0.0]

### Non Functional Requirement

[v1.0.0]

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

# Business Module

## Hero Section

### User Story
[v1.0.0]
- [USL000003] As Public I want to be able to see list of hero content, so that I can understand the main message and value proposition of the landing page.
  - The hero content will be a carousel with  multiple slides and each slide will contain:
    - A large background image
    - A headline text
    - A subheadline text
    - A call-to-action button with a link to the product and service section (open new tab).

### Non Functional Requirement
[v1.0.0]
- [NFRL00003] The carousel will slide every 5 seconds and it will have navigation arrows to allow users to manually slide the carousel.
- [NFRL00006] The image size will be large image with a resolution of at least 1600x500 pixels to ensure that it looks good on all screen sizes.

### Constraint
[v1.0.0]
- [CONSL0006] No header and footer in the hero section, only the content mentioned in the user story.

### Reference
[v1.0.0]

---

## Product and Service Section

### User Story

[v1.0.0]
- [USL000006] As Public I want to be able to see list of products and services, so that I can understand what products and services are offered by the company.
  - Each product and service will be displayed in a card layout with:
    - An image
    - A title
    - A short description
    - A link to the product and service detail page.

### Non Functional Requirement

[v1.0.0]
- [NFRL00009] Section title: "Our Products and Services"
- [NFRL00012] The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices.
- [NFRL00015] The image size will be medium image with a resolution of at least 400x400 pixels to ensure that it looks good on all screen sizes.
- [NFRL00018] White background for the section, and the card will have a shadow effect to make it stand out from the background.

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

## Features Section

### User Story

[v1.0.0]
- [USL000009] As Public I want to be able to see list of features and benefits, so that I can understand the key features and benefits of the products and services offered by the company.
  - Each feature will be displayed in a list layout with:
    - An icon
    - A title
    - A short description

### Non Functional Requirement

[v1.0.0]
- [NFRL00021] Section title: "Key Features and Benefits"
- [NFRL00024] The features will  be displayed in a grid layout with 3 columns on desktop and 1 column on mobile devices.
- [NFRL00027] The icons size will be a font icon with a size of at least 48px to ensure that it looks good on all screen sizes.
- [NFRL00030] Light gray background for the section, and the feature item will have a shadow effect to make it stand out from the background.

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

## Testimonials Section

### User Story

[v1.0.0]
- [USL000012] As Public I want to be able to see list of customer reviews and ratings, so that I can understand the customer satisfaction and feedback about the products and services offered by the company.
  - Each testimonial will be displayed in a card layout with:
    - A customer name
    - A customer review
    - A customer rating (1-5 stars)

### Non Functional Requirement

[v1.0.0]
- [NFRL00033] Section title: "What Our Customers Say"
- [NFRL00036] The testimonials will be displayed in carousel layout with 1 testimonial per slide on desktop and mobile devices.
- [NFRL00039] The customer name will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the review text.
- [NFRL00042] The customer review will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes.
- [NFRL00045] The customer rating will be displayed as a star icon with a size of at least 24px to ensure that it is easy to understand the rating at a glance.
- [NFRL00048] White background for the section, and the card will have a shadow effect to make it stand out from the background.

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

## Team Section

### User Story

[v1.0.0]
- [USL000015] As Public I want to be able to see list of team members and their roles, so that I can understand the team behind the products and services offered by the company.
  - Each team member will be displayed in a card layout with:
    - A profile picture
    - A name
    - A role
    - LinkedIn profile link

### Non Functional Requirement

[v1.0.0]
- [NFRL00051] Section title: "Meet Our Team"
- [NFRL00054] The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices.
- [NFRL00057] The profile picture will be a circular image with a size of at least 150x150 pixels to ensure that it looks good on all screen sizes.
- [NFRL00060] The name will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the role text.
- [NFRL00063] The role will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes.
- [NFRL00066] The LinkedIn profile link will be displayed as a LinkedIn icon with a size of at least 24px to ensure that it is easy to understand the link at a glance.
- [NFRL00069] Light gray background for the section, and the card will have a shadow effect to make it stand out from the background.

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

## Contact Section

### User Story

[v1.0.0]
- [USL000018] As Public I want to be able to see contact information and a contact form, so that I can easily get in touch with the company for any inquiries or support.
  - The contact information will include:
    - A phone number
    - An email address
    - A physical address
  - The contact form will include:
    - A name field
    - An email field
    - A message field
    - A submit button

### Non Functional Requirement

[v1.0.0]
- [NFRL00072] Section title: "Contact Us"
- [NFRL00075] The contact information will be displayed in a list layout with:
  - The phone number will be displayed with a phone icon and a size of at least 16px to ensure that it is easy to read on all screen sizes.
  - The email address will be displayed with an email icon and a size of at least 16px to ensure that it is easy to read on all screen sizes.
  - The physical address will be displayed with a location icon and a size of at least 16px to ensure that it is easy to read on all screen sizes.
- [NFRL00078] The contact form will be displayed in a vertical layout with:
  - The name field will be a text input with a size of at least 16px to ensure that it is easy to read on all screen sizes.
  - The email field will be a text input with a size of at least 16px to ensure that it is easy to read on all screen sizes.
  - The message field will be a textarea input with a size of at least 16px to ensure that it is easy to read on all screen sizes.
  - The message field will have a maximum character limit of 500 characters to ensure that the message is concise and easy to read.
  - The submit button will be a button with a size of at least 16px to ensure that it is easy to click on all screen sizes.
- [NFRL00081] White background for the section, and the contact information and contact form will have a shadow effect to make it stand out from the background.
- [NFRL00084] The contact form will be protected with a CAPTCHA to prevent spam submissions.

### Constraint

[v1.0.0]

### Reference

[v1.0.0]

---

## Blog

### User Story

[v1.0.0]
- [USL000021] As Public I want to be able to see a list of blog articles, so that I can stay updated with the latest news and insights about the company and its products and services.
  - Each blog article will be displayed in a card layout with:
    - An image
    - A title
    - A short description
    - A link to the blog article detail page.
- [USL000024] As Public I want to be able to view the blog article detail page, so that I can read the full content of the blog article and understand the insights and information shared by the company.
  - The blog article detail page will include:
    - An image (1600x500 pixels)
    - A title (overlap on top of the image)
    - The full content of the blog article
    - A link to go back to the blog directory page.

### Non Functional Requirement

[v1.0.0]
- [NFRL00087] Section title: "Our Blog"
- [NFRL00090] The blog list will be a vertical card layout with the blog thumbnail image on the left and the title and short description on the right on desktop, and a vertical card layout with the thumbnail image on top and the title and short description below on mobile devices.
- [NFRL00093] The image size will be medium image with a resolution of at least 800x600 pixels to ensure that it looks good on all screen sizes.
- [NFRL00096] The title will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the description text.
- [NFRL00099] The short description will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes.
- [NFRL00102] The blog article detail page will have a white background, and the content will be displayed in a single column layout with a maximum width of 800px to ensure that it is easy to read on all screen sizes.

### Constraint
- [CONSL0009] The list will be paginated with 10 articles per page, and there will be a pagination navigation at the bottom of the blog directory page to allow users to navigate through the pages of blog articles.

[v1.0.0]

### Reference

[v1.0.0]

---

