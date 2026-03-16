-- ============================================================
-- Simple CMS Admin Portal — v1.0.4 Migration
-- 1. Add BLOB image columns to all image-bearing tables
-- 2. Remove status column from modules that no longer use it
-- 3. Remove READY status from hero section
-- ============================================================

-- ===================== HRS Module ============================
-- Add BLOB columns for image storage
ALTER TABLE hrs_hero_section ADD COLUMN image_data BYTEA;
ALTER TABLE hrs_hero_section ADD COLUMN thumbnail_data BYTEA;
-- Make path columns nullable (images now stored as BLOB)
ALTER TABLE hrs_hero_section ALTER COLUMN image_path DROP NOT NULL;
-- Update any READY status to DRAFT (READY is being removed)
UPDATE hrs_hero_section SET status = 'DRAFT' WHERE status = 'READY';

-- ===================== PAS Module ============================
-- Add BLOB columns for image storage
ALTER TABLE pas_product_service ADD COLUMN image_data BYTEA;
ALTER TABLE pas_product_service ADD COLUMN thumbnail_data BYTEA;
-- Make path columns nullable
ALTER TABLE pas_product_service ALTER COLUMN image_path DROP NOT NULL;
-- Drop status column (no longer used in v1.0.4)
DROP INDEX IF EXISTS idx_pas_product_service_status;
ALTER TABLE pas_product_service DROP COLUMN status;

-- ===================== FTS Module ============================
-- Drop status column (no longer used in v1.0.4)
DROP INDEX IF EXISTS idx_fts_feature_status;
ALTER TABLE fts_feature DROP COLUMN status;

-- ===================== TST Module ============================
-- Drop status column (no longer used in v1.0.4)
DROP INDEX IF EXISTS idx_tst_testimonial_status;
ALTER TABLE tst_testimonial DROP COLUMN status;

-- ===================== TMS Module ============================
-- Add BLOB columns for image storage
ALTER TABLE tms_team_member ADD COLUMN profile_picture_data BYTEA;
ALTER TABLE tms_team_member ADD COLUMN thumbnail_data BYTEA;
-- Make path columns nullable
ALTER TABLE tms_team_member ALTER COLUMN profile_picture_path DROP NOT NULL;
-- Drop status column (no longer used in v1.0.4)
DROP INDEX IF EXISTS idx_tms_team_member_status;
ALTER TABLE tms_team_member DROP COLUMN status;

-- ===================== BLG Module ============================
-- Add BLOB columns for image storage
ALTER TABLE blg_blog_post ADD COLUMN image_data BYTEA;
ALTER TABLE blg_blog_post ADD COLUMN thumbnail_data BYTEA;
-- Make path columns nullable
ALTER TABLE blg_blog_post ALTER COLUMN image_path DROP NOT NULL;
