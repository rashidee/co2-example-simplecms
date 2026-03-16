# Context
- This document contain list of bugs reported by users or testers about this application
- All previously reported bugs have been resolved and merged into the PRD.md as NFRs or constraints.

---

# System Module

## Authentication and Authorization

---

## User

---

# Business Module

## Hero Section

[v1.0.5]
- [BUG-001] The carousel keep refreshing the whole page instead of just scrolling the carousel items. This causes a poor user experience and can lead to increased load times.
  - Steps to Reproduce:
    1. Navigate to the landing page.
    2. Observe the carousel section.
    3. Click on the navigation arrows or wait for the auto-scroll to trigger.
    4. Notice that the entire page refreshes instead of just scrolling the carousel items.
  - Expected Behavior: The carousel should scroll smoothly to the next item without refreshing the entire page.

[v1.0.6]
- [BUG-002] The carousel still keep refreshing every 5 seconds and the carousel rendering everytime I switch item  is not smooth and it looks like the whole page is refreshing instead of just the carousel section. This issue can lead to a poor user experience and can cause increased load times.
  - Steps to Reproduce:
    1. Navigate to the landing page.
    2. Observe the carousel section.
    3. Wait for the auto-scroll to trigger every 5 seconds.
    4. Notice that the entire page refreshes instead of just scrolling the carousel items, and the rendering of the carousel items is not smooth.
  - Suggestion to Fix: Remove the time interval. Use a state management solution to manage the current active carousel item and update the carousel items without refreshing the entire page. Implement smooth transitions between carousel items to enhance the user experience.
  - Expected Behavior: The carousel should scroll smoothly to the next item without refreshing the entire page, and the rendering of the carousel items should be smooth without any flickering or delays.


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
