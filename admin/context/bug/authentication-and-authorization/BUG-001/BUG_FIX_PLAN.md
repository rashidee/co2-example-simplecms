# Fix Plan — BUG-001

**Bug**: 403 error after successful login
**Module**: Authentication and Authorization
**Root Cause**: The login form (and all other POST forms) are missing the CSRF token hidden input. Spring Security's CSRF protection is enabled by default and rejects POST requests without a valid CSRF token, returning 403 Forbidden. Additionally, after login, the `defaultSuccessUrl("/")` redirects to `/` which has no controller handler.
**Impact Assessment**: Low — the fix adds CSRF support globally and a home redirect controller, which are additive changes that don't break existing functionality.

---

## Fix Checklist

- [x] 1. Add `_csrf` `@ModelAttribute` in WebMvcConfig to expose CsrfToken to all JTE templates
- [x] 2. Add CSRF hidden input to LoginPage.jte
- [x] 3. Add CSRF hidden input to all other POST forms (ForgotPasswordPage.jte, ResetPasswordPage.jte, Header.jte logout, and all 32 CRUD forms)
- [x] 4. Create HomeController to handle GET `/` and redirect based on user role
- [x] 5. Verify fix with BUG_TEST_SPEC.md
- [x] 6. Update artifacts (specifications)

---

## Files Modified

| File | Change Description |
|------|-------------------|
| `admin/src/main/java/com/simplecms/adminportal/config/WebMvcConfig.java` | Added `@ModelAttribute("_csrf")` method to expose CsrfToken |
| `admin/src/main/java/com/simplecms/adminportal/config/HomeController.java` | New — redirects `/` to role-appropriate page (ADMIN→/users, EDITOR→/hero-section, default→/profile) |
| `admin/src/main/jte/authentication/LoginPage.jte` | Added CSRF param and hidden input |
| `admin/src/main/jte/authentication/ForgotPasswordPage.jte` | Added CSRF param and hidden input |
| `admin/src/main/jte/authentication/ResetPasswordPage.jte` | Added CSRF param and hidden input |
| `admin/src/main/jte/fragment/Header.jte` | Added CSRF param and hidden input to logout form |
| 32 additional JTE form templates | Added CSRF param and hidden input to all POST forms |

---

## Fix Log

### Step 1: Add CSRF ModelAttribute to WebMvcConfig
2026-03-16 - Completed
- Added `import org.springframework.security.web.csrf.CsrfToken`
- Added `@ModelAttribute("_csrf")` method that retrieves CsrfToken from request attribute
- Result: CSRF token now available to all JTE templates as `_csrf` parameter

### Step 2: Add CSRF hidden input to all forms
2026-03-16 - Completed
- Added `@param org.springframework.security.web.csrf.CsrfToken _csrf = null` to 36 JTE template files
- Added `@if(_csrf != null) <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"> @endif` inside every POST form
- Files covered: auth pages, header logout, user CRUD, hero section, product/service, features, testimonials, team, blog, contact section, and all card grid fragments

### Step 3: Create HomeController
2026-03-16 - Completed
- Created `HomeController.java` in config package
- Redirects `/` based on user role: ADMIN→/users, EDITOR→/hero-section, default→/profile
- Result: Authenticated users are redirected to their role-appropriate landing page

### Step 4: Verify
2026-03-16 - Completed
- Reproduced bug: login POST returned 403 Forbidden (Whitelabel Error Page)
- Applied fix, rebuilt, and restarted application
- Verified: login with admin@simplecms.com/password now successfully redirects to /users page
- Screenshot saved: screenshot_fixed.png shows authenticated Users page with sidebar and header
