# Implementation - Hero Section (v1.0.4 Upgrade)

**Module**: Hero Section
**Layer**: L2
**Status**: IN PROGRESS
**Started**: 2026-03-16

---

## v1.0.4 Changes Required

1. Remove READY status from enum ✅
2. Auto-determine status from dates (computeFromDates) ✅
3. Remove status parameter from create/update service methods ✅
4. Store images as BLOB in database ✅
5. Add date validation (effective < expiration) ✅
6. Add image serving endpoints (/hero-section/{id}/image, /thumbnail) ✅
7. Update create/edit templates (remove status dropdown) ✅
8. Update list/card-grid templates (use thumbnailUrl()) ✅

## Implementation Checklist

- [x] 1. Create Flyway V4 migration (BLOB columns, status removal for other modules)
- [x] 2. Update HeroSectionStatus enum (remove READY, add computeFromDates)
- [x] 3. Update HeroSectionEntity (add imageData/thumbnailData BLOB fields)
- [x] 4. Update HeroSectionDTO (add hasImageData, thumbnailUrl(), imageUrl())
- [x] 5. Update HeroSectionMapper (add hasImageData mapping)
- [x] 6. Update HeroSectionService interface (remove status param, add getImageData/getThumbnailData)
- [x] 7. Update HeroSectionServiceImpl (BLOB storage, auto-status, date validation)
- [x] 8. Update HeroSectionPageController (remove status param, add image endpoints)
- [x] 9. Update HeroSectionFormView (remove statuses array)
- [x] 10. Update HeroSectionCreatePage.jte (remove status dropdown)
- [x] 11. Update HeroSectionEditPage.jte (remove status dropdown, use thumbnailUrl)
- [x] 12. Update HeroSectionListPage.jte (use thumbnailUrl, remove READY badge)
- [x] 13. Update HeroSectionCardGrid.jte (use thumbnailUrl, remove READY badge)
- [ ] 14. Compilation verification — BLOCKED: Other module entities still reference dropped status columns
- [ ] 15. Runtime verification with Playwright

## Blocking Issue

The V4 migration drops status columns from fts_feature, tst_testimonial, tms_team_member,
pas_product_service tables. Their entity classes still reference the status field. Need to
update all 4 entity classes + their DTOs, services, controllers, and templates before the
app will start.

## Files Modified

- `admin/src/main/resources/db/migration/V4__v1_0_4_blob_images_remove_status.sql`
- `admin/src/main/java/com/simplecms/adminportal/herosection/HeroSectionStatus.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/HeroSectionDTO.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/HeroSectionService.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/internal/HeroSectionEntity.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/internal/HeroSectionServiceImpl.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/internal/HeroSectionPageController.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/internal/HeroSectionMapper.java`
- `admin/src/main/java/com/simplecms/adminportal/herosection/internal/HeroSectionFormView.java`
- `admin/src/main/jte/herosection/HeroSectionCreatePage.jte`
- `admin/src/main/jte/herosection/HeroSectionEditPage.jte`
- `admin/src/main/jte/herosection/HeroSectionListPage.jte`
- `admin/src/main/jte/herosection/fragments/HeroSectionCardGrid.jte`
