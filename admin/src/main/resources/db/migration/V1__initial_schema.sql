-- ============================================================
-- Simple CMS Admin Portal — Initial Schema
-- Database: PostgreSQL 14
-- ============================================================

-- ===================== USR Module ============================

CREATE TABLE usr_user (
    id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
    email                   VARCHAR(255)    NOT NULL,
    password                VARCHAR(255)    NOT NULL,
    first_name              VARCHAR(100)    NOT NULL,
    last_name               VARCHAR(100)    NOT NULL,
    role                    VARCHAR(20)     NOT NULL DEFAULT 'USER',
    status                  VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    force_password_change   BOOLEAN         NOT NULL DEFAULT TRUE,
    last_login_at           TIMESTAMP,
    version                 BIGINT          NOT NULL DEFAULT 0,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(255)    NOT NULL,
    updated_at              TIMESTAMP,
    updated_by              VARCHAR(255),
    CONSTRAINT pk_usr_user PRIMARY KEY (id),
    CONSTRAINT uq_usr_user_email UNIQUE (email)
);

CREATE INDEX idx_usr_user_email ON usr_user(email);
CREATE INDEX idx_usr_user_status ON usr_user(status);
CREATE INDEX idx_usr_user_role ON usr_user(role);

-- ===================== AAA Module ============================

CREATE TABLE aaa_password_reset_token (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL,
    token           VARCHAR(255)    NOT NULL,
    expires_at      TIMESTAMP       NOT NULL,
    used            BOOLEAN         NOT NULL DEFAULT FALSE,
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_aaa_password_reset_token PRIMARY KEY (id),
    CONSTRAINT uq_aaa_password_reset_token_token UNIQUE (token),
    CONSTRAINT fk_aaa_password_reset_token_user FOREIGN KEY (user_id)
        REFERENCES usr_user(id) ON DELETE CASCADE
);

CREATE INDEX idx_aaa_password_reset_token_user_id ON aaa_password_reset_token(user_id);
CREATE INDEX idx_aaa_password_reset_token_token ON aaa_password_reset_token(token);

-- ===================== HRS Module ============================

CREATE TABLE hrs_hero_section (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    image_path          VARCHAR(500)    NOT NULL,
    thumbnail_path      VARCHAR(500),
    headline            VARCHAR(100)    NOT NULL,
    subheadline         VARCHAR(200)    NOT NULL,
    cta_url             VARCHAR(500)    NOT NULL,
    cta_text            VARCHAR(50)     NOT NULL,
    effective_date      TIMESTAMP       NOT NULL,
    expiration_date     TIMESTAMP       NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_hrs_hero_section PRIMARY KEY (id)
);

CREATE INDEX idx_hrs_hero_section_status ON hrs_hero_section(status);
CREATE INDEX idx_hrs_hero_section_effective_date ON hrs_hero_section(effective_date DESC);

-- ===================== PAS Module ============================

CREATE TABLE pas_product_service (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    image_path          VARCHAR(500)    NOT NULL,
    thumbnail_path      VARCHAR(500),
    title               VARCHAR(100)    NOT NULL,
    description         VARCHAR(500)    NOT NULL,
    cta_url             VARCHAR(500),
    cta_text            VARCHAR(50),
    display_order       INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_pas_product_service PRIMARY KEY (id)
);

CREATE INDEX idx_pas_product_service_status ON pas_product_service(status);
CREATE INDEX idx_pas_product_service_order ON pas_product_service(display_order ASC, created_at ASC);

-- ===================== FTS Module ============================

CREATE TABLE fts_feature (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    icon                VARCHAR(100)    NOT NULL,
    title               VARCHAR(100)    NOT NULL,
    description         VARCHAR(500)    NOT NULL,
    display_order       INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_fts_feature PRIMARY KEY (id)
);

CREATE INDEX idx_fts_feature_status ON fts_feature(status);
CREATE INDEX idx_fts_feature_order ON fts_feature(display_order ASC, created_at ASC);

-- ===================== TST Module ============================

CREATE TABLE tst_testimonial (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    customer_name       VARCHAR(100)    NOT NULL,
    customer_review     VARCHAR(1000)   NOT NULL,
    customer_rating     INTEGER         NOT NULL,
    display_order       INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_tst_testimonial PRIMARY KEY (id),
    CONSTRAINT chk_tst_testimonial_rating CHECK (customer_rating >= 1 AND customer_rating <= 5)
);

CREATE INDEX idx_tst_testimonial_status ON tst_testimonial(status);
CREATE INDEX idx_tst_testimonial_order ON tst_testimonial(display_order ASC, created_at ASC);

-- ===================== TMS Module ============================

CREATE TABLE tms_team_member (
    id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
    profile_picture_path    VARCHAR(500)    NOT NULL,
    name                    VARCHAR(100)    NOT NULL,
    role                    VARCHAR(100)    NOT NULL,
    linkedin_url            VARCHAR(500)    NOT NULL,
    display_order           INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version                 BIGINT          NOT NULL DEFAULT 0,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(255)    NOT NULL,
    updated_at              TIMESTAMP,
    updated_by              VARCHAR(255),
    CONSTRAINT pk_tms_team_member PRIMARY KEY (id)
);

CREATE INDEX idx_tms_team_member_status ON tms_team_member(status);
CREATE INDEX idx_tms_team_member_order ON tms_team_member(display_order ASC, created_at ASC);

-- ===================== CTS Module ============================

CREATE TABLE cts_contact_info (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    phone_number        VARCHAR(50)     NOT NULL,
    email_address       VARCHAR(255)    NOT NULL,
    physical_address    VARCHAR(500)    NOT NULL,
    linkedin_url        VARCHAR(500)    NOT NULL,
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_cts_contact_info PRIMARY KEY (id)
);

CREATE TABLE cts_contact_message (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    sender_name         VARCHAR(255)    NOT NULL,
    sender_email        VARCHAR(255)    NOT NULL,
    message_content     TEXT            NOT NULL,
    submitted_at        TIMESTAMP       NOT NULL DEFAULT NOW(),
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_cts_contact_message PRIMARY KEY (id)
);

CREATE INDEX idx_cts_contact_message_submitted ON cts_contact_message(submitted_at DESC);

CREATE TABLE cts_contact_response (
    id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
    contact_message_id      UUID            NOT NULL,
    responder_name          VARCHAR(255)    NOT NULL,
    responder_email         VARCHAR(255)    NOT NULL,
    response_content        TEXT            NOT NULL,
    sent_at                 TIMESTAMP,
    version                 BIGINT          NOT NULL DEFAULT 0,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(255)    NOT NULL,
    updated_at              TIMESTAMP,
    updated_by              VARCHAR(255),
    CONSTRAINT pk_cts_contact_response PRIMARY KEY (id),
    CONSTRAINT fk_cts_contact_response_message FOREIGN KEY (contact_message_id)
        REFERENCES cts_contact_message(id) ON DELETE CASCADE
);

CREATE INDEX idx_cts_contact_response_message ON cts_contact_response(contact_message_id);

-- ===================== BLG Module ============================

CREATE TABLE blg_blog_category (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    name                VARCHAR(100)    NOT NULL,
    description         VARCHAR(500),
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_blg_blog_category PRIMARY KEY (id),
    CONSTRAINT uq_blg_blog_category_name UNIQUE (name)
);

CREATE TABLE blg_blog_post (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    category_id         UUID            NOT NULL,
    author_id           UUID            NOT NULL,
    title               VARCHAR(100)    NOT NULL,
    slug                VARCHAR(255)    NOT NULL,
    summary             VARCHAR(300)    NOT NULL,
    content             TEXT            NOT NULL,
    image_path          VARCHAR(500)    NOT NULL,
    thumbnail_path      VARCHAR(500),
    effective_date      TIMESTAMP       NOT NULL,
    expiration_date     TIMESTAMP       NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255)    NOT NULL,
    updated_at          TIMESTAMP,
    updated_by          VARCHAR(255),
    CONSTRAINT pk_blg_blog_post PRIMARY KEY (id),
    CONSTRAINT uq_blg_blog_post_slug UNIQUE (slug),
    CONSTRAINT fk_blg_blog_post_category FOREIGN KEY (category_id)
        REFERENCES blg_blog_category(id) ON DELETE RESTRICT,
    CONSTRAINT fk_blg_blog_post_author FOREIGN KEY (author_id)
        REFERENCES usr_user(id) ON DELETE NO ACTION
);

CREATE INDEX idx_blg_blog_post_category ON blg_blog_post(category_id);
CREATE INDEX idx_blg_blog_post_author ON blg_blog_post(author_id);
CREATE INDEX idx_blg_blog_post_status ON blg_blog_post(status);
CREATE INDEX idx_blg_blog_post_effective ON blg_blog_post(effective_date DESC);
CREATE INDEX idx_blg_blog_post_slug ON blg_blog_post(slug);
