# Context
- This document contain list of bugs reported by users or testers about this application
- All previously reported bugs have been resolved and merged into the PRD.md as NFRs or constraints.

---

# Common

[v1.0.2]
- [BUG-002] Menu color is dark and hard to read since the sidebar background is also dark
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Observe the color of the System > User menu in the sidebar
      3. Observe that the color is dark and hard to read against the dark background
  - Expected result: The System > User menu should have a lighter color that contrasts well with the dark background for better readability
- [BUG-003] Dark mode toggle is not working
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Click on the dark mode toggle button in the header
      3. Observe that the theme does not change and there is an error in the console
  - Expected result: The dark mode toggle should switch between light and dark themes without any errors
- [BUG-004] User avatar dropdown menu is not working
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Click on the user avatar in the header
      3. Observe that the dropdown menu does not appear and there is an error in the console
  - Expected result: The user avatar dropdown menu should appear with options for profile and logout without any errors
- [BUG-005] The footer is not positioned at the bottom of the page and overlaps with the content when the page height is small
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Resize the browser window to a smaller height
      3. Observe that the footer overlaps with the main content and is not fixed at the bottom of the page
  - Expected result: The footer should be fixed at the bottom of the page and should not overlap with the main content regardless of the page height
- [BUG-006] Logging in as editor causing 500 error
  - Priority: High
  - Steps to reproduce:
      1. Login as editor user
      2. Observe that there is a 500 error and the editor user cannot access the system
  - Expected result: The editor user should be able to log in successfully and access the system

[v1.0.3]
- [BUG-010] All the cards in all the pages does not support dark mode and the text is hard to read when dark mode is enabled
  - Priority: Low
  - Steps to reproduce:
      1. Login as admin user
      2. Enable dark mode by clicking on the dark mode toggle button in the header
      3. Observe that all the cards in all the pages have a light background and the text is hard to read against the light background when dark mode is enabled
  - Expected result: All the cards in all the pages should have a dark background with light text when dark mode is enabled for better readability

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

[v1.0.2]
- [BUG-007] Logout URL not working and causing 500 error
  - Priority: Medium
  - Steps to reproduce:
    1. Login as admin user
    2. Click on the user avatar in the header
    3. Click on the Logout option in the dropdown menu
    4. Observe that there is a 500 error and the user is not logged out successfully
  - Expected result: The logout URL should log the user out successfully and redirect to the login page without any errors

---

## User

[v1.0.2]
- [BUG-008] System > User menu is not working when logging in as admin
  - Priority: Medium
  - Steps to reproduce:
    1. Login as admin user
    2. Click on System > User menu
    3. Observe that the page is not loading and there is an error in the console
  - Expected result: The user management page should load successfully without any errors
- [BUG-009] In the create/edit user form, the role selection of `USER` should be removed
  - Priority: Low
  - Steps to reproduce:
    1. Login as admin user
    2. Go to System > User menu
    3. Click on Create User or Edit an existing user
    4. Observe that the role selection includes `USER` option
  - Expected result: The role selection should only include `ADMIN` or `EDITOR` option since there is no functionality for `USER` role in the system and it can cause confusion for the admin users

---

# Business Module

## Hero Section

[v1.0.3]
- [BUG-011] Error 500 when creating new hero section content.
  - Priority: Medium
  - Steps to reproduce:
    1. Login as editor user
    2. Click on Hero Section menu
    3. Click on Create New Hero Section button
    4. Fill in the form with valid data and submit
    5. Observe that there is a 500 error and the hero section content is not created successfully
  - Expected result: The new hero section content should be created successfully without any errors

---

## Product and Service Section

[v1.0.3]
- [BUG-012] Error 500 when creating new product/service content.
  - Priority: Medium
  - Steps to reproduce:
    1. Login as editor user
    2. Click on Product and Service Section menu
    3. Click on Create New Product/Service button
    4. Fill in the form with valid data and submit
    5. Observe that there is a 500 error and the product/service content is not created successfully
  - Expected result: The new product/service content should be created successfully without any errors

---

## Features Section

[v1.0.3]
- [BUG-013] Icon selection is not showing the icon itself in the new features content form.
  - Priority: Low
  - Steps to reproduce:
    1. Login as editor user
    2. Click on Features Section menu
    3. Click on Create New Feature button
    4. Observe that the icon selection dropdown does not show the icons and only shows the icon names, which makes it hard for the user to select the desired icon
  - Expected result: The icon selection dropdown should show the icons themselves along with the icon names for better user experience when selecting an icon for the feature content

---

## Testimonials Section

---

## Contact Section

---

## Team Section

[v1.0.3]
- [BUG-014] Error 500 when creating new team member content.
  - Priority: Medium
  - Steps to reproduce:
    1. Login as editor user
    2. Click on Team Section menu
    3. Click on Create New Team Member button
    4. Fill in the form with valid data and submit
    5. Observe that there is a 500 error and the team member content is not created successfully
  - Expected result: The new team member content should be created successfully without any errors

---

## Blog

[v1.0.3]
- [BUG-015] Menu Blog Posts is not working when logging in as editor
  - Priority: Medium
  - Steps to reproduce:
    1. Login as editor user
    2. Click on Blog > Posts menu
    3. Observe that the page is not loading and there is an error in the console
  - Expected result: The blog post management page should load successfully without any errors

---
