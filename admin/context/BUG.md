# Context
- This document contain list of bugs reported by users or testers about this application
- All previously reported bugs have been resolved and merged into the PRD.md as NFRs or constraints.

---

# Common

[v1.0.2]
- Menu color is dark and hard to read since the sidebar background is also dark
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Observe the color of the System > User menu in the sidebar
      3. Observe that the color is dark and hard to read against the dark background
  - Expected result: The System > User menu should have a lighter color that contrasts well with the dark background for better readability
- Dark mode toggle is not working
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Click on the dark mode toggle button in the header
      3. Observe that the theme does not change and there is an error in the console
  - Expected result: The dark mode toggle should switch between light and dark themes without any errors
- User avatar dropdown menu is not working
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Click on the user avatar in the header
      3. Observe that the dropdown menu does not appear and there is an error in the console
  - Expected result: The user avatar dropdown menu should appear with options for profile and logout without any errors

# System Module

## Authentication and Authorization

[v1.0.1]
- [BUG-001] 403 error after successful login
  - Priority: High
  - Steps to reproduce:
    1. Go to login page
    2. Enter valid email and password
    3. Click login button
    4. Observe 403 error page
  - Expected result: User should be redirected to the dashboard page after successful login

---

## User

[v1.0.2]
- System > User menu is not working when logging in as admin
  - Priority: Medium
  - Steps to reproduce:
    1. Login as admin user
    2. Click on System > User menu
    3. Observe that the page is not loading and there is an error in the console
  - Expected result: The user management page should load successfully without any errors

---

# Business Module

## Hero Section

---

## Product and Service Section

---

## Features Section

---

## Testimonials Section

---

## Contact Section

---

## Team Section

---

## Blog

---
