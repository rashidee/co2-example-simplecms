-- BUG-011: Allow nullable expiration_date for hero sections, product/service, and blog posts
ALTER TABLE hrs_hero_section ALTER COLUMN expiration_date DROP NOT NULL;
