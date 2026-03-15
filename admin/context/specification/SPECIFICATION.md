# Root Specification: Simple CMS Admin Portal

[Back to PRD.md](../PRD.md)

---

## 1. Project Overview

| Field | Value |
|-------|-------|
| Project Code | SCMS |
| Application | Admin Portal |
| Artifact ID | admin-portal |
| Group ID | com.simplecms |
| Base Package | `com.simplecms.adminportal` |
| Java Version | 21 |
| Spring Boot | 3.5.7 |
| Database | PostgreSQL 14 (localhost:5432/cms_db) |
| Architecture | Spring Modulith monolith |

### 1.1 Technology Stack

| Category | Technology | Version |
|----------|-----------|---------|
| Runtime | Java (Eclipse Temurin) | 21 |
| Framework | Spring Boot | 3.5.7 |
| Modularity | Spring Modulith | 1.3.x (BOM aligned with Boot 3.5.7) |
| Template Engine | JTE (Java Template Engine) | 3.1.x |
| CSS Framework | Tailwind CSS | 4.x |
| Interactivity | htmx | 2.0.x |
| Reactivity | Alpine.js | 3.14.x |
| Database | PostgreSQL | 14 |
| Migration | Flyway | (managed by Spring Boot starter) |
| ORM | Hibernate / Spring Data JPA | (managed by Spring Boot starter) |
| Scheduling | Quartz (JDBC Job Store) | (managed by Spring Boot starter) |
| Mapping | MapStruct | 1.6.x |
| Build | Maven | 3.9+ |
| Frontend Build | Vite | 6.x |
| Icons | FontAwesome | 6.x (CDN) |
| Rich Text | TinyMCE | 7.x (CDN) |
| Image Processing | Scalr (imgscalr) | 4.2 |
| HTML Sanitizer | OWASP Java HTML Sanitizer | 20240325.1 |

### 1.2 Roles and Module Access

| Module | USER | EDITOR | ADMIN |
|--------|------|--------|-------|
| Home (Dashboard) | Read own | Read own | Read own |
| Profile | Read/Edit own | Read/Edit own | Read/Edit own |
| Account (Password) | Change own | Change own | Change own |
| Notifications | Read own | Read own | Read own |
| User Management | -- | -- | Full CRUD |
| Hero Section | -- | Full CRUD | -- |
| Products & Services | -- | Full CRUD | -- |
| Features | -- | Full CRUD | -- |
| Testimonials | -- | Full CRUD | -- |
| Team | -- | Full CRUD | -- |
| Contact | -- | Full CRUD | -- |
| Blog Categories | -- | Full CRUD | -- |
| Blog Posts | -- | Full CRUD | -- |

### 1.3 Sidebar Navigation per Role

#### USER Role

| Group | Label | Path | Icon |
|-------|-------|------|------|
| -- | Home | `/user/home` | House |

#### ADMIN Role

| Group | Label | Path | Icon |
|-------|-------|------|------|
| -- | Home | `/admin/home` | House |
| System | Users | `/admin/users` | Users |

#### EDITOR Role

| Group | Label | Path | Icon |
|-------|-------|------|------|
| -- | Home | `/editor/home` | House |
| Marketing | Hero Section | `/editor/hero_section` | Image |
| Marketing | Products & Services | `/editor/product_and_service` | Box |
| Marketing | Features | `/editor/features_section` | Sparkles |
| Marketing | Testimonials | `/editor/testimonials` | Chat |
| Marketing | Team | `/editor/team_section` | Users |
| Marketing | Contact | `/editor/contact_section` | Mail |
| Blog | Blog Categories | `/editor/blog_categories` | Tag |
| Blog | Blog Posts | `/editor/blog` | Newspaper |

### 1.4 Module Index

| # | Module | Prefix | SPEC Link |
|---|--------|--------|-----------|
| 1 | Authentication and Authorization | AAA | [authentication-and-authorization/SPEC.md](./authentication-and-authorization/SPEC.md) |
| 2 | User | USR | [user/SPEC.md](./user/SPEC.md) |
| 3 | Hero Section | HRS | [hero-section/SPEC.md](./hero-section/SPEC.md) |
| 4 | Product and Service Section | PAS | [product-and-service-section/SPEC.md](./product-and-service-section/SPEC.md) |
| 5 | Features Section | FTS | [features-section/SPEC.md](./features-section/SPEC.md) |
| 6 | Testimonials Section | TST | [testimonials-section/SPEC.md](./testimonials-section/SPEC.md) |
| 7 | Team Section | TMS | [team-section/SPEC.md](./team-section/SPEC.md) |
| 8 | Contact Section | CTS | [contact-section/SPEC.md](./contact-section/SPEC.md) |
| 9 | Blog | BLG | [blog/SPEC.md](./blog/SPEC.md) |

---

## 2. Maven Configuration

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.7</version>
        <relativePath/>
    </parent>

    <groupId>com.simplecms</groupId>
    <artifactId>admin-portal</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>Simple CMS Admin Portal</name>
    <description>Admin portal for the Simple CMS content management system</description>

    <properties>
        <java.version>21</java.version>
        <spring-modulith.version>1.3.5</spring-modulith.version>
        <jte.version>3.1.16</jte.version>
        <mapstruct.version>1.6.3</mapstruct.version>
        <imgscalr.version>4.2</imgscalr.version>
        <owasp-sanitizer.version>20240325.1</owasp-sanitizer.version>
        <frontend-maven-plugin.version>1.15.1</frontend-maven-plugin.version>
        <node.version>v20.18.0</node.version>
        <npm.version>10.8.2</npm.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.modulith</groupId>
                <artifactId>spring-modulith-bom</artifactId>
                <version>${spring-modulith.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-database-postgresql</artifactId>
        </dependency>

        <!-- Spring Modulith -->
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-starter-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-events-api</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-starter-jpa</artifactId>
        </dependency>

        <!-- JTE Template Engine -->
        <dependency>
            <groupId>gg.jte</groupId>
            <artifactId>jte-spring-boot-starter-3</artifactId>
            <version>${jte.version}</version>
        </dependency>
        <dependency>
            <groupId>gg.jte</groupId>
            <artifactId>jte</artifactId>
            <version>${jte.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>

        <!-- Image Processing -->
        <dependency>
            <groupId>org.imgscalr</groupId>
            <artifactId>imgscalr-lib</artifactId>
            <version>${imgscalr.version}</version>
        </dependency>

        <!-- HTML Sanitizer -->
        <dependency>
            <groupId>com.googlecode.owasp-java-html-sanitizer</groupId>
            <artifactId>owasp-java-html-sanitizer</artifactId>
            <version>${owasp-sanitizer.version}</version>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>0.2.0</version>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <arg>-Amapstruct.defaultComponentModel=spring</arg>
                    </compilerArgs>
                </configuration>
            </plugin>

            <!-- JTE Precompilation -->
            <plugin>
                <groupId>gg.jte</groupId>
                <artifactId>jte-maven-plugin</artifactId>
                <version>${jte.version}</version>
                <configuration>
                    <sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
                    <contentType>Html</contentType>
                </configuration>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- Frontend Build (Vite + Tailwind) -->
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>${frontend-maven-plugin.version}</version>
                <configuration>
                    <workingDirectory>${project.basedir}/src/main/frontend</workingDirectory>
                    <nodeVersion>${node.version}</nodeVersion>
                    <npmVersion>${npm.version}</npmVersion>
                </configuration>
                <executions>
                    <execution>
                        <id>install-node-and-npm</id>
                        <goals><goal>install-node-and-npm</goal></goals>
                    </execution>
                    <execution>
                        <id>npm-install</id>
                        <goals><goal>npm</goal></goals>
                        <configuration>
                            <arguments>ci</arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>npm-build</id>
                        <goals><goal>npm</goal></goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 3. Application Configuration

### 3.1 application.yml

**File:** `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: admin-portal

  # ----- DataSource -----
  datasource:
    url: jdbc:postgresql://localhost:5432/cms_db
    username: cms_user
    password: cms_password
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000

  # ----- JPA -----
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
    properties:
      hibernate:
        format_sql: true
        jdbc:
          time_zone: UTC

  # ----- Flyway -----
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: false

  # ----- Security -----
  security:
    user:
      name: disabled
      password: disabled

  # ----- Quartz -----
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: always
    properties:
      org:
        quartz:
          scheduler:
            instanceName: AdminPortalScheduler
            instanceId: AUTO
          jobStore:
            driverDelegateClass: org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
            useProperties: false
            tablePrefix: QRTZ_
            isClustered: false
          threadPool:
            threadCount: 5

  # ----- Servlet -----
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

# ----- JTE -----
gg:
  jte:
    development-mode: false
    template-location: src/main/jte
    template-suffix: .jte

# ----- App Custom Properties -----
app:
  theme:
    primary-color: "#2271b1"
    secondary-color: "#135e96"
    sidebar-bg: "#1d2327"
  upload:
    base-path: ./uploads
    hero-image-dir: hero
    product-service-image-dir: product-service
    team-image-dir: team
    blog-image-dir: blog
  scheduling:
    content-expiration-cron: "0 */5 * * * ?"
    email-send-cron: "0 */2 * * * ?"

# ----- Logging -----
logging:
  level:
    root: INFO
    com.simplecms.adminportal: DEBUG
    org.springframework.security: INFO
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.flywaydb: INFO
```

### 3.2 application-dev.yml

**File:** `src/main/resources/application-dev.yml`

```yaml
gg:
  jte:
    development-mode: true

spring:
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true

logging:
  level:
    com.simplecms.adminportal: DEBUG
    org.hibernate.SQL: DEBUG
```

### 3.3 application-prod.yml

**File:** `src/main/resources/application-prod.yml`

```yaml
gg:
  jte:
    development-mode: false

spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    hikari:
      maximum-pool-size: 20

logging:
  level:
    root: WARN
    com.simplecms.adminportal: INFO
    org.hibernate.SQL: WARN
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
```

---

## 4. Build and Tooling

### 4.1 Vite Configuration

**File:** `src/main/frontend/vite.config.js`

```javascript
import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  build: {
    outDir: '../resources/static',
    emptyOutDir: false,
    manifest: true,
    rollupOptions: {
      input: {
        app: resolve(__dirname, 'src/main.js'),
        styles: resolve(__dirname, 'src/main.css'),
      },
      output: {
        entryFileNames: 'js/[name]-[hash].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: (assetInfo) => {
          if (assetInfo.name && assetInfo.name.endsWith('.css')) {
            return 'css/[name]-[hash][extname]';
          }
          return 'assets/[name]-[hash][extname]';
        },
      },
    },
  },
  server: {
    origin: 'http://localhost:5173',
  },
});
```

### 4.2 Tailwind CSS Configuration

**File:** `src/main/frontend/tailwind.config.js`

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    '../jte/**/*.jte',
    './src/**/*.js',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: '#2271b1',
        secondary: '#135e96',
        accent: '#2271b1',
        success: '#00a32a',
        warning: '#dba617',
        danger: '#d63638',
        sidebar: '#1d2327',
        'sidebar-text': '#f0f0f1',
        'page-bg': '#f0f0f1',
        surface: '#ffffff',
        'text-primary': '#1e1e1e',
        'text-secondary': '#646970',
        border: '#c3c4c7',
      },
      fontFamily: {
        body: [
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          'Oxygen-Sans',
          'Ubuntu',
          'Cantarell',
          '"Helvetica Neue"',
          'sans-serif',
        ],
      },
      borderRadius: {
        btn: '4px',
        card: '8px',
      },
    },
  },
  plugins: [],
};
```

### 4.3 PostCSS Configuration

**File:** `src/main/frontend/postcss.config.js`

```javascript
export default {
  plugins: {
    '@tailwindcss/postcss': {},
  },
};
```

### 4.4 Frontend package.json

**File:** `src/main/frontend/package.json`

```json
{
  "name": "admin-portal-frontend",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "devDependencies": {
    "@tailwindcss/postcss": "^4.0.0",
    "autoprefixer": "^10.4.20",
    "postcss": "^8.5.0",
    "tailwindcss": "^4.0.0",
    "vite": "^6.0.0"
  },
  "dependencies": {
    "alpinejs": "^3.14.1",
    "htmx.org": "^2.0.4"
  }
}
```

### 4.5 Frontend Entry Points

**File:** `src/main/frontend/src/main.css`

```css
@import "tailwindcss";
```

**File:** `src/main/frontend/src/main.js`

```javascript
import Alpine from 'alpinejs';
import htmx from 'htmx.org';

window.Alpine = Alpine;
window.htmx = htmx;

// Alpine stores
Alpine.store('toast', {
  messages: [],
  add(type, text, duration = 5000) {
    const id = Date.now();
    this.messages.push({ id, type, text });
    setTimeout(() => this.remove(id), duration);
  },
  remove(id) {
    this.messages = this.messages.filter(m => m.id !== id);
  },
});

Alpine.store('theme', {
  dark: localStorage.getItem('darkMode') === 'true',
  toggle() {
    this.dark = !this.dark;
    localStorage.setItem('darkMode', this.dark);
    document.documentElement.classList.toggle('dark', this.dark);
  },
  init() {
    document.documentElement.classList.toggle('dark', this.dark);
  },
});

Alpine.store('sidebar', {
  collapsed: false,
  toggle() {
    this.collapsed = !this.collapsed;
  },
});

Alpine.start();

// htmx extensions
document.body.addEventListener('htmx:afterRequest', (event) => {
  const xhr = event.detail.xhr;
  if (xhr && xhr.status >= 400) {
    Alpine.store('toast').add('error', 'An error occurred. Please try again.');
  }
});

document.body.addEventListener('htmx:responseError', () => {
  Alpine.store('toast').add('error', 'Server error. Please try again later.');
});
```

### 4.6 ViteManifestService

**File:** `src/main/java/com/simplecms/adminportal/shared/vite/ViteManifestService.java`

```java
package com.simplecms.adminportal.shared.vite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Reads the Vite manifest.json at startup to resolve hashed asset file names.
 * In development mode (Vite dev server), returns un-hashed paths.
 */
@Service
public class ViteManifestService {

    private static final Logger log = LoggerFactory.getLogger(ViteManifestService.class);

    private final ObjectMapper objectMapper;
    private final boolean developmentMode;
    private Map<String, ViteManifestEntry> manifest = Collections.emptyMap();

    public ViteManifestService(ObjectMapper objectMapper,
                               @Value("${gg.jte.development-mode:false}") boolean developmentMode) {
        this.objectMapper = objectMapper;
        this.developmentMode = developmentMode;
    }

    @PostConstruct
    void loadManifest() {
        if (developmentMode) {
            log.info("Vite development mode active — manifest not loaded");
            return;
        }
        try {
            ClassPathResource resource = new ClassPathResource("static/.vite/manifest.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    manifest = objectMapper.readValue(is,
                            new TypeReference<Map<String, ViteManifestEntry>>() {});
                    log.info("Loaded Vite manifest with {} entries", manifest.size());
                }
            } else {
                log.warn("Vite manifest.json not found — assets may not resolve correctly");
            }
        } catch (IOException e) {
            log.error("Failed to load Vite manifest.json", e);
        }
    }

    /**
     * Resolves a source entry name (e.g. "src/main.js") to its hashed output path.
     *
     * @param entryName the Vite entry name as defined in vite.config.js
     * @return the resolved path prefixed with "/" for use in templates
     */
    public String resolve(String entryName) {
        if (developmentMode) {
            return "http://localhost:5173/" + entryName;
        }
        ViteManifestEntry entry = manifest.get(entryName);
        if (entry == null) {
            log.warn("Vite manifest entry not found for: {}", entryName);
            return "/" + entryName;
        }
        return "/" + entry.file();
    }

    /**
     * Returns the CSS file(s) for a given entry, if any.
     */
    public String[] resolveCss(String entryName) {
        if (developmentMode) {
            return new String[0];
        }
        ViteManifestEntry entry = manifest.get(entryName);
        if (entry == null || entry.css() == null) {
            return new String[0];
        }
        return entry.css().stream()
                .map(css -> "/" + css)
                .toArray(String[]::new);
    }

    public record ViteManifestEntry(
        String file,
        String src,
        boolean isEntry,
        java.util.List<String> css
    ) {}
}
```

---

## 5. .gitignore

**File:** `.gitignore`

```gitignore
# --- Java / Maven ---
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# --- IDE ---
.idea/
*.iws
*.iml
*.ipr
.vscode/
.settings/
.classpath
.project
*.swp
*.swo
*~
.DS_Store

# --- Spring Boot ---
*.log
logs/

# --- JTE ---
jte-classes/

# --- Node / Frontend ---
src/main/frontend/node_modules/
src/main/frontend/node/
src/main/frontend/dist/

# --- Vite build output (generated into static) ---
src/main/resources/static/js/
src/main/resources/static/css/
src/main/resources/static/assets/
src/main/resources/static/.vite/

# --- Uploads ---
uploads/

# --- Environment ---
.env
*.env.local

# --- Playwright MCP ---
.playwright-mcp/
```

---

## 6. Package Structure

```
src/main/java/com/simplecms/adminportal/
  +-- AdminPortalApplication.java
  |
  +-- shared/                                    (cross-cutting concerns)
  |     +-- config/
  |     |     +-- SecurityConfig.java
  |     |     +-- QuartzConfig.java
  |     |     +-- WebMvcConfig.java
  |     +-- entity/
  |     |     +-- BaseEntity.java
  |     +-- exception/
  |     |     +-- WebApplicationException.java
  |     |     +-- GlobalExceptionHandler.java
  |     +-- security/
  |     |     +-- CustomUserDetailsService.java
  |     |     +-- CspNonceFilter.java
  |     +-- vite/
  |     |     +-- ViteManifestService.java
  |     +-- scheduling/
  |           +-- ContentExpirationJob.java
  |           +-- EmailSendJob.java
  |
  +-- authentication/                            (AAA module — public API)
  |     +-- AuthService.java
  |     +-- PasswordResetTokenDTO.java
  |     +-- internal/
  |           +-- PasswordResetTokenEntity.java
  |           +-- PasswordResetTokenRepository.java
  |           +-- AuthServiceImpl.java
  |           +-- AuthMapper.java
  |           +-- LoginPageController.java
  |           +-- ForgotPasswordPageController.java
  |           +-- ResetPasswordPageController.java
  |           +-- LoginView.java
  |           +-- ForgotPasswordView.java
  |           +-- ResetPasswordView.java
  |
  +-- user/                                      (USR module — public API)
  |     +-- UserService.java
  |     +-- UserDTO.java
  |     +-- CreateUserRequest.java
  |     +-- UpdateUserRequest.java
  |     +-- UpdateProfileRequest.java
  |     +-- ChangePasswordRequest.java
  |     +-- UserRole.java
  |     +-- UserStatus.java
  |     +-- internal/
  |           +-- UserEntity.java
  |           +-- UserRepository.java
  |           +-- UserServiceImpl.java
  |           +-- UserMapper.java
  |           +-- DataSeeder.java
  |           +-- ProfilePageController.java
  |           +-- AccountPageController.java
  |           +-- UserManagementPageController.java
  |           +-- UserFragmentController.java
  |           +-- ProfileView.java
  |           +-- AccountView.java
  |           +-- UserListView.java
  |           +-- UserCreateView.java
  |           +-- UserEditView.java
  |
  +-- herosection/                               (HRS module — public API)
  |     +-- HeroSectionService.java
  |     +-- HeroSectionDTO.java
  |     +-- HeroSectionStatus.java
  |     +-- internal/
  |           +-- HeroSectionEntity.java
  |           +-- HeroSectionRepository.java
  |           +-- HeroSectionServiceImpl.java
  |           +-- HeroSectionMapper.java
  |           +-- HeroSectionPageController.java
  |           +-- HeroSectionFragmentController.java
  |           +-- HeroSectionListView.java
  |           +-- HeroSectionCreateView.java
  |           +-- HeroSectionEditView.java
  |
  +-- productservice/                            (PAS module — public API)
  |     +-- ProductServiceService.java
  |     +-- ProductServiceDTO.java
  |     +-- ProductServiceStatus.java
  |     +-- internal/
  |           +-- ProductServiceEntity.java
  |           +-- ProductServiceRepository.java
  |           +-- ProductServiceServiceImpl.java
  |           +-- ProductServiceMapper.java
  |           +-- ProductServicePageController.java
  |           +-- ProductServiceFragmentController.java
  |           +-- ProductServiceListView.java
  |           +-- ProductServiceCreateView.java
  |           +-- ProductServiceEditView.java
  |
  +-- feature/                                   (FTS module — public API)
  |     +-- FeatureService.java
  |     +-- FeatureDTO.java
  |     +-- FeatureStatus.java
  |     +-- internal/
  |           +-- FeatureEntity.java
  |           +-- FeatureRepository.java
  |           +-- FeatureServiceImpl.java
  |           +-- FeatureMapper.java
  |           +-- FeaturePageController.java
  |           +-- FeatureFragmentController.java
  |           +-- FeatureListView.java
  |           +-- FeatureCreateView.java
  |           +-- FeatureEditView.java
  |
  +-- testimonial/                               (TST module — public API)
  |     +-- TestimonialService.java
  |     +-- TestimonialDTO.java
  |     +-- TestimonialStatus.java
  |     +-- internal/
  |           +-- TestimonialEntity.java
  |           +-- TestimonialRepository.java
  |           +-- TestimonialServiceImpl.java
  |           +-- TestimonialMapper.java
  |           +-- TestimonialPageController.java
  |           +-- TestimonialFragmentController.java
  |           +-- TestimonialListView.java
  |           +-- TestimonialCreateView.java
  |           +-- TestimonialEditView.java
  |
  +-- team/                                      (TMS module — public API)
  |     +-- TeamService.java
  |     +-- TeamMemberDTO.java
  |     +-- TeamMemberStatus.java
  |     +-- internal/
  |           +-- TeamMemberEntity.java
  |           +-- TeamMemberRepository.java
  |           +-- TeamServiceImpl.java
  |           +-- TeamMapper.java
  |           +-- TeamPageController.java
  |           +-- TeamFragmentController.java
  |           +-- TeamListView.java
  |           +-- TeamCreateView.java
  |           +-- TeamEditView.java
  |
  +-- contact/                                   (CTS module — public API)
  |     +-- ContactService.java
  |     +-- ContactInfoDTO.java
  |     +-- ContactMessageDTO.java
  |     +-- ContactResponseDTO.java
  |     +-- internal/
  |           +-- ContactInfoEntity.java
  |           +-- ContactMessageEntity.java
  |           +-- ContactResponseEntity.java
  |           +-- ContactInfoRepository.java
  |           +-- ContactMessageRepository.java
  |           +-- ContactResponseRepository.java
  |           +-- ContactServiceImpl.java
  |           +-- ContactMapper.java
  |           +-- ContactPageController.java
  |           +-- ContactFragmentController.java
  |           +-- ContactInfoView.java
  |           +-- ContactMessageListView.java
  |           +-- ContactMessageDetailView.java
  |
  +-- blog/                                      (BLG module — public API)
        +-- BlogService.java
        +-- BlogCategoryDTO.java
        +-- BlogPostDTO.java
        +-- BlogPostStatus.java
        +-- internal/
              +-- BlogCategoryEntity.java
              +-- BlogPostEntity.java
              +-- BlogCategoryRepository.java
              +-- BlogPostRepository.java
              +-- BlogServiceImpl.java
              +-- BlogMapper.java
              +-- BlogCategoryPageController.java
              +-- BlogPostPageController.java
              +-- BlogCategoryFragmentController.java
              +-- BlogPostFragmentController.java
              +-- BlogCategoryListView.java
              +-- BlogCategoryCreateView.java
              +-- BlogCategoryEditView.java
              +-- BlogPostListView.java
              +-- BlogPostCreateView.java
              +-- BlogPostEditView.java
```

### JTE Template Structure

```
src/main/jte/
  +-- layout/
  |     +-- MainLayout.jte
  |     +-- AuthLayout.jte
  |     +-- ErrorLayout.jte
  |
  +-- fragment/
  |     +-- Header.jte
  |     +-- Sidebar.jte
  |     +-- Footer.jte
  |
  +-- component/
  |     +-- Alert.jte
  |     +-- Badge.jte
  |     +-- Button.jte
  |     +-- Card.jte
  |     +-- FormControl.jte
  |     +-- Modal.jte
  |     +-- Pagination.jte
  |     +-- Table.jte
  |     +-- Toast.jte
  |
  +-- authentication/
  |     +-- LoginPage.jte
  |     +-- ForgotPasswordPage.jte
  |     +-- ResetPasswordPage.jte
  |
  +-- user/
  |     +-- ProfilePage.jte
  |     +-- ProfileEditFragment.jte
  |     +-- AccountPage.jte
  |     +-- UserListPage.jte
  |     +-- UserListFragment.jte
  |     +-- UserCreatePage.jte
  |     +-- UserEditPage.jte
  |
  +-- herosection/
  |     +-- HeroSectionListPage.jte
  |     +-- HeroSectionListFragment.jte
  |     +-- HeroSectionCreatePage.jte
  |     +-- HeroSectionEditPage.jte
  |
  +-- productservice/
  |     +-- ProductServiceListPage.jte
  |     +-- ProductServiceListFragment.jte
  |     +-- ProductServiceCreatePage.jte
  |     +-- ProductServiceEditPage.jte
  |
  +-- feature/
  |     +-- FeatureListPage.jte
  |     +-- FeatureListFragment.jte
  |     +-- FeatureCreatePage.jte
  |     +-- FeatureEditPage.jte
  |
  +-- testimonial/
  |     +-- TestimonialListPage.jte
  |     +-- TestimonialListFragment.jte
  |     +-- TestimonialCreatePage.jte
  |     +-- TestimonialEditPage.jte
  |
  +-- team/
  |     +-- TeamListPage.jte
  |     +-- TeamListFragment.jte
  |     +-- TeamCreatePage.jte
  |     +-- TeamEditPage.jte
  |
  +-- contact/
  |     +-- ContactInfoPage.jte
  |     +-- ContactMessageListPage.jte
  |     +-- ContactMessageListFragment.jte
  |     +-- ContactMessageDetailPage.jte
  |
  +-- blog/
  |     +-- BlogCategoryListPage.jte
  |     +-- BlogCategoryListFragment.jte
  |     +-- BlogCategoryCreatePage.jte
  |     +-- BlogCategoryEditPage.jte
  |     +-- BlogPostListPage.jte
  |     +-- BlogPostListFragment.jte
  |     +-- BlogPostCreatePage.jte
  |     +-- BlogPostEditPage.jte
  |
  +-- error/
        +-- 403.jte
        +-- 404.jte
        +-- 500.jte
```

---

## 7. Security Configuration

**File:** `src/main/java/com/simplecms/adminportal/shared/config/SecurityConfig.java`

```java
package com.simplecms.adminportal.shared.config;

import com.simplecms.adminportal.shared.security.CspNonceFilter;
import com.simplecms.adminportal.shared.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",
                    "/forgot-password",
                    "/reset-password",
                    "/css/**",
                    "/js/**",
                    "/assets/**",
                    "/favicon.ico",
                    "/error"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/editor/**").hasRole("EDITOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .addFilterBefore(new CspNonceFilter(),
                org.springframework.security.web.csrf.CsrfFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
```

### 7.1 CustomUserDetailsService

**File:** `src/main/java/com/simplecms/adminportal/shared/security/CustomUserDetailsService.java`

```java
package com.simplecms.adminportal.shared.security;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import com.simplecms.adminportal.user.UserStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Loads user details from the User module for Spring Security authentication.
 *
 * Traces: USA000003
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));

        boolean enabled = user.status() == UserStatus.ACTIVE;

        return new User(
                user.email(),
                userService.getEncodedPassword(user.id()),
                enabled,
                true,  // accountNonExpired
                true,  // credentialsNonExpired
                true,  // accountNonLocked
                List.of(new SimpleGrantedAuthority("ROLE_" + user.role().name()))
        );
    }
}
```

### 7.2 CspNonceFilter

**File:** `src/main/java/com/simplecms/adminportal/shared/security/CspNonceFilter.java`

```java
package com.simplecms.adminportal.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Generates a CSP nonce per request and adds it as a request attribute
 * and Content-Security-Policy header.
 */
public class CspNonceFilter extends OncePerRequestFilter {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int NONCE_LENGTH = 16;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        byte[] nonceBytes = new byte[NONCE_LENGTH];
        RANDOM.nextBytes(nonceBytes);
        String nonce = Base64.getEncoder().encodeToString(nonceBytes);

        request.setAttribute("cspNonce", nonce);

        String csp = String.format(
            "default-src 'self'; " +
            "script-src 'self' 'nonce-%s' https://cdn.jsdelivr.net https://cdn.tiny.cloud; " +
            "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://cdn.tiny.cloud; " +
            "font-src 'self' https://cdnjs.cloudflare.com; " +
            "img-src 'self' data: blob:; " +
            "connect-src 'self' http://localhost:5173 ws://localhost:5173;",
            nonce
        );
        response.setHeader("Content-Security-Policy", csp);

        filterChain.doFilter(request, response);
    }
}
```

---

## 8. Layouts

### 8.1 MainLayout.jte

**File:** `src/main/jte/layout/MainLayout.jte`

```html
@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Admin Portal"
@param String currentPath = ""
@param String userRole = ""
@param String userInitials = ""
@param String userName = ""
@param ViteManifestService vite
@param String cspNonce = ""

<!DOCTYPE html>
<html lang="en" x-data :class="{ 'dark': $store.theme.dark }">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Simple CMS</title>
    <link rel="stylesheet" href="${vite.resolve("src/main.css")}">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="bg-page-bg dark:bg-slate-900 text-text-primary dark:text-slate-200 min-h-screen flex flex-col font-body">

    @template.fragment.Header(
        userInitials = userInitials,
        userName = userName,
        userRole = userRole,
        cspNonce = cspNonce
    )

    <div class="flex pt-16 min-h-[calc(100vh-4rem)]">
        @template.fragment.Sidebar(
            currentPath = currentPath,
            userRole = userRole
        )

        <main class="flex-1 ml-64 flex flex-col min-w-0">
            <div id="content-spinner" class="hidden fixed inset-0 bg-white/50 dark:bg-slate-900/50 z-50 items-center justify-center">
                <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
            <div id="content-area" class="flex-1 flex flex-col transition-opacity duration-200">
                ${content}
            </div>
            @template.fragment.Footer()
        </main>
    </div>

    <script src="${vite.resolve("src/main.js")}" nonce="${cspNonce}"></script>
</body>
</html>
```

### 8.2 AuthLayout.jte

**File:** `src/main/jte/layout/AuthLayout.jte`

```html
@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Admin Portal"
@param ViteManifestService vite
@param String cspNonce = ""

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Simple CMS</title>
    <link rel="stylesheet" href="${vite.resolve("src/main.css")}">
</head>
<body class="bg-page-bg min-h-screen flex items-center justify-center font-body">

    <div class="w-full max-w-md px-6">
        <div class="text-center mb-8">
            <h1 class="text-2xl font-bold text-text-primary">Simple CMS</h1>
            <p class="text-text-secondary text-sm mt-1">Admin Portal</p>
        </div>

        ${content}
    </div>

    <script src="${vite.resolve("src/main.js")}" nonce="${cspNonce}"></script>
</body>
</html>
```

### 8.3 ErrorLayout.jte

**File:** `src/main/jte/layout/ErrorLayout.jte`

```html
@import gg.jte.Content
@import com.simplecms.adminportal.shared.vite.ViteManifestService

@param Content content
@param String pageTitle = "Error"
@param ViteManifestService vite
@param String cspNonce = ""

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Simple CMS</title>
    <link rel="stylesheet" href="${vite.resolve("src/main.css")}">
</head>
<body class="bg-page-bg min-h-screen flex items-center justify-center font-body">

    <div class="text-center px-6">
        ${content}
    </div>

</body>
</html>
```

---

## 9. Fragments

### 9.1 Header.jte

**File:** `src/main/jte/fragment/Header.jte`

```html
@param String userInitials = ""
@param String userName = ""
@param String userRole = ""
@param String cspNonce = ""

<header class="bg-white dark:bg-slate-800 border-b border-border dark:border-slate-700 h-16 flex items-center justify-between px-6 fixed top-0 left-0 right-0 z-30 shadow-sm">
    <div class="flex items-center gap-3">
        <div class="w-8 h-8 rounded-btn bg-primary flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zm10 0a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zm10 0a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"/>
            </svg>
        </div>
        <span class="font-semibold text-lg text-slate-900 dark:text-slate-100">Admin Portal</span>
    </div>

    <div class="flex items-center gap-2">
        <!-- Dark Mode Toggle -->
        <button @click="$store.theme.toggle()"
                class="p-2 text-text-secondary hover:text-text-primary hover:bg-slate-100 dark:hover:bg-slate-700 rounded-btn transition-colors duration-200"
                :title="$store.theme.dark ? 'Switch to light mode' : 'Switch to dark mode'">
            <svg x-show="!$store.theme.dark" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
            </svg>
            <svg x-show="$store.theme.dark" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" style="display:none">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
            </svg>
        </button>

        <div class="w-px h-8 bg-border dark:bg-slate-600 mx-1"></div>

        <!-- User Avatar + Dropdown -->
        <div class="relative" x-data="{ open: false }">
            <button @click="open = !open" @keydown.escape.window="open = false"
                    class="flex items-center gap-2 cursor-pointer p-1 rounded-btn hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors">
                <div class="w-8 h-8 rounded-full bg-primary flex items-center justify-center font-semibold text-sm text-white">
                    ${userInitials}
                </div>
                <svg class="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                </svg>
            </button>
            <div x-show="open" @click.outside="open = false" x-transition
                 class="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card shadow-xl py-1 z-50"
                 style="display: none;">
                <a href="/${userRole}/profile"
                   class="flex items-center gap-2 px-4 py-2 text-sm text-text-primary dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                    </svg>
                    Profile
                </a>
                <a href="/${userRole}/account"
                   class="flex items-center gap-2 px-4 py-2 text-sm text-text-primary dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.066 2.573c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.573 1.066c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.066-2.573c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                    Account
                </a>
                <div class="border-t border-slate-100 dark:border-slate-700 my-1"></div>
                <form method="post" action="/logout">
                    <button type="submit"
                            class="flex w-full items-center gap-2 px-4 py-2 text-sm text-danger hover:bg-red-50 dark:hover:bg-red-900/20">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                        </svg>
                        Logout
                    </button>
                </form>
            </div>
        </div>
    </div>
</header>
```

### 9.2 Sidebar.jte

**File:** `src/main/jte/fragment/Sidebar.jte`

```html
@param String currentPath = ""
@param String userRole = ""

!{var roleLower = userRole.toLowerCase();}

<aside class="w-64 bg-sidebar fixed top-16 bottom-0 left-0 overflow-y-auto z-20">
    <nav class="py-4" x-data="{ active: '${currentPath}' }">

        <%-- Home — all roles --%>
        <a href="/${roleLower}/home"
           :class="active.startsWith('/${roleLower}/home')
               ? 'bg-white/10 text-white border-l-4 border-white font-medium'
               : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
           class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
            <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>
            </svg>
            <span>Home</span>
        </a>

        @if(userRole.equals("ADMIN"))
            <%-- ADMIN: System group --%>
            <div class="px-4 pt-4 pb-1">
                <span class="text-xs font-semibold text-sidebar-text/40 uppercase tracking-wider">System</span>
            </div>
            <a href="/admin/users"
               :class="active.startsWith('/admin/users')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/>
                </svg>
                <span>Users</span>
            </a>
        @endif

        @if(userRole.equals("EDITOR"))
            <%-- EDITOR: Marketing group --%>
            <div class="px-4 pt-4 pb-1">
                <span class="text-xs font-semibold text-sidebar-text/40 uppercase tracking-wider">Marketing</span>
            </div>

            <a href="/editor/hero_section"
               :class="active.startsWith('/editor/hero_section')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                <span>Hero Section</span>
            </a>

            <a href="/editor/product_and_service"
               :class="active.startsWith('/editor/product_and_service')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
                </svg>
                <span>Products & Services</span>
            </a>

            <a href="/editor/features_section"
               :class="active.startsWith('/editor/features_section')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"/>
                </svg>
                <span>Features</span>
            </a>

            <a href="/editor/testimonials"
               :class="active.startsWith('/editor/testimonials')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
                </svg>
                <span>Testimonials</span>
            </a>

            <a href="/editor/team_section"
               :class="active.startsWith('/editor/team_section')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/>
                </svg>
                <span>Team</span>
            </a>

            <a href="/editor/contact_section"
               :class="active.startsWith('/editor/contact_section')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                </svg>
                <span>Contact</span>
            </a>

            <%-- EDITOR: Blog group --%>
            <div class="px-4 pt-4 pb-1">
                <span class="text-xs font-semibold text-sidebar-text/40 uppercase tracking-wider">Blog</span>
            </div>

            <a href="/editor/blog_categories"
               :class="active.startsWith('/editor/blog_categories')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"/>
                </svg>
                <span>Blog Categories</span>
            </a>

            <a href="/editor/blog"
               :class="active.startsWith('/editor/blog') && !active.startsWith('/editor/blog_categories')
                   ? 'bg-white/10 text-white border-l-4 border-white font-medium'
                   : 'text-sidebar-text/80 hover:bg-white/5 hover:text-white'"
               class="flex items-center gap-3 px-4 py-2.5 text-sm transition-colors duration-200 cursor-pointer">
                <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"/>
                </svg>
                <span>Blog Posts</span>
            </a>
        @endif
    </nav>
</aside>
```

### 9.3 Footer.jte

**File:** `src/main/jte/fragment/Footer.jte`

```html
<footer class="border-t border-border dark:border-slate-700 bg-white dark:bg-slate-800 px-6 py-3 flex items-center justify-between text-xs text-text-secondary">
    <span>&copy; 2026 Simple CMS &mdash; Admin Portal. All rights reserved.</span>
    <span>v1.0.0</span>
</footer>
```

---

## 10. UI Components

All components are pure Tailwind CSS JTE templates. They accept parameters and render consistent markup.

### 10.1 Alert.jte

**File:** `src/main/jte/component/Alert.jte`

```html
@param String type = "info"
@param String message = ""
@param boolean dismissible = true

!{
    var bgClass = switch (type) {
        case "success" -> "bg-success/10 border-success/30 text-success";
        case "warning" -> "bg-warning/10 border-warning/30 text-warning";
        case "error"   -> "bg-danger/10 border-danger/30 text-danger";
        default        -> "bg-primary/10 border-primary/30 text-primary";
    };
}

<div class="p-3 border rounded-btn text-sm ${bgClass}" x-data="{ show: true }" x-show="show" x-transition>
    <div class="flex items-center justify-between">
        <span>${message}</span>
        @if(dismissible)
            <button @click="show = false" class="ml-2 opacity-70 hover:opacity-100">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        @endif
    </div>
</div>
```

### 10.2 Badge.jte

**File:** `src/main/jte/component/Badge.jte`

```html
@param String type = "default"
@param String text = ""

!{
    var classes = switch (type) {
        case "success" -> "bg-success/10 text-success";
        case "warning" -> "bg-warning/10 text-warning";
        case "error"   -> "bg-danger/10 text-danger";
        case "info"    -> "bg-primary/10 text-primary";
        default        -> "bg-slate-100 text-text-secondary dark:bg-slate-700 dark:text-slate-300";
    };
}

<span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${classes}">
    ${text}
</span>
```

### 10.3 Button.jte

**File:** `src/main/jte/component/Button.jte`

```html
@param String type = "button"
@param String variant = "primary"
@param String size = "md"
@param String text = ""
@param String href = ""
@param boolean disabled = false

!{
    var variantClass = switch (variant) {
        case "secondary" -> "bg-white border border-border text-text-primary hover:bg-slate-50 dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200 dark:hover:bg-slate-600";
        case "danger"    -> "bg-danger hover:bg-red-700 text-white";
        case "ghost"     -> "bg-transparent text-primary hover:bg-primary/10";
        default          -> "bg-primary hover:bg-secondary text-white";
    };
    var sizeClass = switch (size) {
        case "sm" -> "px-3 py-1.5 text-xs";
        case "lg" -> "px-6 py-3 text-base";
        default   -> "px-4 py-2.5 text-sm";
    };
    var baseClass = "inline-flex items-center justify-center font-medium rounded-btn transition-colors duration-200 " + variantClass + " " + sizeClass;
}

@if(!href.isEmpty())
    <a href="${href}" class="${baseClass}">${text}</a>
@else
    <button type="${type}" class="${baseClass}" ${ disabled ? "disabled" : "" }>
        ${text}
    </button>
@endif
```

### 10.4 Card.jte

**File:** `src/main/jte/component/Card.jte`

```html
@import gg.jte.Content

@param Content content
@param String title = ""
@param String padding = "p-6"

<div class="bg-surface dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card ${padding}">
    @if(!title.isEmpty())
        <h3 class="text-lg font-semibold text-text-primary dark:text-slate-100 mb-4">${title}</h3>
    @endif
    ${content}
</div>
```

### 10.5 FormControl.jte

**File:** `src/main/jte/component/FormControl.jte`

```html
@param String id = ""
@param String label = ""
@param String type = "text"
@param String name = ""
@param String value = ""
@param String placeholder = ""
@param String error = ""
@param boolean required = false
@param int maxlength = 0

<div>
    @if(!label.isEmpty())
        <label for="${id}" class="block text-sm font-medium text-text-primary dark:text-slate-200 mb-1">
            ${label}
            @if(required)
                <span class="text-danger">*</span>
            @endif
        </label>
    @endif
    <input type="${type}" id="${id}" name="${name}" value="${value}"
           placeholder="${placeholder}"
           ${ required ? "required" : "" }
           ${ maxlength > 0 ? "maxlength=\"" + maxlength + "\"" : "" }
           class="w-full px-3 py-2.5 text-sm border ${!error.isEmpty() ? "border-danger" : "border-border"} rounded-btn focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200">
    @if(!error.isEmpty())
        <p class="mt-1 text-xs text-danger">${error}</p>
    @endif
</div>
```

### 10.6 Modal.jte

**File:** `src/main/jte/component/Modal.jte`

```html
@import gg.jte.Content

@param String id = "modal"
@param String title = ""
@param Content content

<div x-data="{ open: false }"
     x-show="open"
     x-on:open-modal-${id}.window="open = true"
     x-on:close-modal-${id}.window="open = false"
     @keydown.escape.window="open = false"
     class="fixed inset-0 z-50 flex items-center justify-center"
     style="display: none;">
    <div class="fixed inset-0 bg-black/50" @click="open = false"></div>
    <div class="relative bg-surface dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card shadow-xl w-full max-w-lg mx-4 p-6"
         x-transition>
        <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-semibold text-text-primary dark:text-slate-100">${title}</h3>
            <button @click="open = false" class="text-text-secondary hover:text-text-primary">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>
        ${content}
    </div>
</div>
```

### 10.7 Pagination.jte

**File:** `src/main/jte/component/Pagination.jte`

```html
@param int currentPage = 0
@param int totalPages = 0
@param String baseUrl = ""

@if(totalPages > 1)
<nav class="flex items-center justify-center gap-1 mt-6">
    @if(currentPage > 0)
        <a href="${baseUrl}?page=${currentPage - 1}"
           class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
            Previous
        </a>
    @endif

    @for(int i = 0; i < totalPages; i++)
        @if(i == currentPage)
            <span class="px-3 py-2 text-sm bg-primary text-white rounded-btn">${i + 1}</span>
        @else
            <a href="${baseUrl}?page=${i}"
               class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
                ${i + 1}
            </a>
        @endif
    @endfor

    @if(currentPage < totalPages - 1)
        <a href="${baseUrl}?page=${currentPage + 1}"
           class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
            Next
        </a>
    @endif
</nav>
@endif
```

### 10.8 Table.jte

**File:** `src/main/jte/component/Table.jte`

```html
@import gg.jte.Content

@param Content header
@param Content body

<div class="overflow-x-auto">
    <table class="w-full text-sm text-left">
        <thead class="text-xs text-text-secondary uppercase bg-slate-50 dark:bg-slate-700/50 border-b border-border dark:border-slate-700">
            ${header}
        </thead>
        <tbody class="divide-y divide-border dark:divide-slate-700">
            ${body}
        </tbody>
    </table>
</div>
```

### 10.9 Toast.jte

**File:** `src/main/jte/component/Toast.jte`

```html
<div x-data class="fixed top-20 right-6 z-50 flex flex-col gap-2 w-80">
    <template x-for="msg in $store.toast.messages" :key="msg.id">
        <div x-transition
             :class="{
                 'bg-success/10 border-success/30 text-success': msg.type === 'success',
                 'bg-danger/10 border-danger/30 text-danger': msg.type === 'error',
                 'bg-warning/10 border-warning/30 text-warning': msg.type === 'warning',
                 'bg-primary/10 border-primary/30 text-primary': msg.type === 'info'
             }"
             class="p-3 border rounded-card text-sm shadow-lg flex items-center justify-between">
            <span x-text="msg.text"></span>
            <button @click="$store.toast.remove(msg.id)" class="ml-2 opacity-70 hover:opacity-100">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>
    </template>
</div>
```

---

## 11. Frontend JS

Covered in Section 4.5. The Alpine stores (`toast`, `theme`, `sidebar`) and htmx event listeners are defined in `src/main/frontend/src/main.js`.

---

## 12. Auth Pages

Auth pages use `AuthLayout`. Full JTE templates are specified in the [Authentication and Authorization SPEC](./authentication-and-authorization/SPEC.md), section 7:

- `LoginPage.jte` -- Section 7.1
- `ForgotPasswordPage.jte` -- Section 7.2
- `ResetPasswordPage.jte` -- Section 7.3

---

## 13. Data Access

### 13.1 BaseEntity

**File:** `src/main/java/com/simplecms/adminportal/shared/entity/BaseEntity.java`

```java
package com.simplecms.adminportal.shared.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base entity providing common fields for all JPA entities:
 * UUID primary key, optimistic locking version, and audit columns.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 13.2 Flyway V1__initial_schema.sql

**File:** `src/main/resources/db/migration/V1__initial_schema.sql`

```sql
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
```

### 13.3 Repository Pattern

All repositories follow the Spring Data JPA pattern:

```java
@Repository
interface ExampleRepository extends JpaRepository<ExampleEntity, UUID> {
    // Derived queries, @Query methods, Specification support
}
```

Pagination is achieved via `Pageable` parameters:

```java
Page<ExampleEntity> findByStatus(String status, Pageable pageable);
```

### 13.4 MapStruct Convention

All mappers follow this pattern:

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ExampleMapper {
    ExampleDTO toDTO(ExampleEntity entity);
    List<ExampleDTO> toDTOList(List<ExampleEntity> entities);
}
```

### 13.5 Pagination Convention

- Default page size: 12 (grid layouts), 20 (table layouts)
- Page parameter: `?page=0` (zero-indexed)
- Sort parameter: `?sort=fieldName,asc`
- Pagination component: `component/Pagination.jte`

---

## 14. Error Handling

### 14.1 WebApplicationException

**File:** `src/main/java/com/simplecms/adminportal/shared/exception/WebApplicationException.java`

```java
package com.simplecms.adminportal.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Application-level exception that carries an HTTP status code
 * and a user-friendly message for rendering in error pages.
 */
public class WebApplicationException extends RuntimeException {

    private final HttpStatus status;

    public WebApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public WebApplicationException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    // Convenience factory methods

    public static WebApplicationException notFound(String message) {
        return new WebApplicationException(HttpStatus.NOT_FOUND, message);
    }

    public static WebApplicationException badRequest(String message) {
        return new WebApplicationException(HttpStatus.BAD_REQUEST, message);
    }

    public static WebApplicationException forbidden(String message) {
        return new WebApplicationException(HttpStatus.FORBIDDEN, message);
    }

    public static WebApplicationException conflict(String message) {
        return new WebApplicationException(HttpStatus.CONFLICT, message);
    }
}
```

### 14.2 GlobalExceptionHandler

**File:** `src/main/java/com/simplecms/adminportal/shared/exception/GlobalExceptionHandler.java`

```java
package com.simplecms.adminportal.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler that renders error pages for uncaught exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebApplicationException.class)
    public String handleWebApplicationException(WebApplicationException ex, Model model) {
        log.warn("WebApplicationException: {} - {}", ex.getStatus(), ex.getMessage());
        model.addAttribute("status", ex.getStatus().value());
        model.addAttribute("error", ex.getStatus().getReasonPhrase());
        model.addAttribute("message", ex.getMessage());

        return switch (ex.getStatus()) {
            case FORBIDDEN -> "error/403";
            case NOT_FOUND -> "error/404";
            default -> "error/500";
        };
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unhandled exception", ex);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred. Please try again later.");
        return "error/500";
    }
}
```

---

## 15. Theming

Light/dark mode is toggled via an Alpine.js store (`$store.theme`) and persisted in `localStorage`.

- The `<html>` element receives `class="dark"` when dark mode is active.
- Tailwind `dark:` variants are used throughout all templates.
- The theme toggle button is in the `Header.jte` fragment.
- No cookie is needed; `localStorage` is sufficient because theming is purely client-side.

---

## 16. Scheduling

### 16.1 Quartz JDBC Configuration

Quartz is configured with a JDBC Job Store (see Section 3.1 `application.yml`). Spring Boot auto-creates the Quartz tables via `spring.quartz.jdbc.initialize-schema=always`.

### 16.2 ContentExpirationJob

**File:** `src/main/java/com/simplecms/adminportal/shared/scheduling/ContentExpirationJob.java`

```java
package com.simplecms.adminportal.shared.scheduling;

import com.simplecms.adminportal.blog.BlogService;
import com.simplecms.adminportal.herosection.HeroSectionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Quartz job that checks for expired Hero Section and Blog Post content
 * and updates their status to EXPIRED.
 *
 * Runs on cron: ${app.scheduling.content-expiration-cron} (default every 5 minutes)
 *
 * Traces: NFRA00018 (hero expiration), CONSA0030 (blog expiration)
 */
@Component
public class ContentExpirationJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ContentExpirationJob.class);

    private final HeroSectionService heroSectionService;
    private final BlogService blogService;

    public ContentExpirationJob(HeroSectionService heroSectionService,
                                BlogService blogService) {
        this.heroSectionService = heroSectionService;
        this.blogService = blogService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Running content expiration check");
        try {
            int heroExpired = heroSectionService.expireContent();
            int blogExpired = blogService.expireContent();
            log.info("Content expiration complete: {} hero sections, {} blog posts expired",
                    heroExpired, blogExpired);
        } catch (Exception e) {
            log.error("Error during content expiration check", e);
            throw new JobExecutionException(e);
        }
    }
}
```

### 16.3 EmailSendJob

**File:** `src/main/java/com/simplecms/adminportal/shared/scheduling/EmailSendJob.java`

```java
package com.simplecms.adminportal.shared.scheduling;

import com.simplecms.adminportal.contact.ContactService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Quartz job that sends pending contact response emails asynchronously.
 *
 * Runs on cron: ${app.scheduling.email-send-cron} (default every 2 minutes)
 *
 * Traces: USA000093, NFRA00108
 */
@Component
public class EmailSendJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(EmailSendJob.class);

    private final ContactService contactService;

    public EmailSendJob(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Running email send job");
        try {
            int sent = contactService.sendPendingResponses();
            if (sent > 0) {
                log.info("Email send job complete: {} responses sent", sent);
            }
        } catch (Exception e) {
            log.error("Error during email send job", e);
            throw new JobExecutionException(e);
        }
    }
}
```

### 16.4 QuartzConfig

**File:** `src/main/java/com/simplecms/adminportal/shared/config/QuartzConfig.java`

```java
package com.simplecms.adminportal.shared.config;

import com.simplecms.adminportal.shared.scheduling.ContentExpirationJob;
import com.simplecms.adminportal.shared.scheduling.EmailSendJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${app.scheduling.content-expiration-cron:0 */5 * * * ?}")
    private String contentExpirationCron;

    @Value("${app.scheduling.email-send-cron:0 */2 * * * ?}")
    private String emailSendCron;

    @Bean
    public JobDetail contentExpirationJobDetail() {
        return JobBuilder.newJob(ContentExpirationJob.class)
                .withIdentity("contentExpirationJob", "content")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger contentExpirationTrigger(JobDetail contentExpirationJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(contentExpirationJobDetail)
                .withIdentity("contentExpirationTrigger", "content")
                .withSchedule(CronScheduleBuilder.cronSchedule(contentExpirationCron))
                .build();
    }

    @Bean
    public JobDetail emailSendJobDetail() {
        return JobBuilder.newJob(EmailSendJob.class)
                .withIdentity("emailSendJob", "email")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger emailSendTrigger(JobDetail emailSendJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(emailSendJobDetail)
                .withIdentity("emailSendTrigger", "email")
                .withSchedule(CronScheduleBuilder.cronSchedule(emailSendCron))
                .build();
    }
}
```

---

## 17. Events, MapStruct, and Testing Strategy

### 17.1 Spring Modulith Events

Domain events are published via `ApplicationEventPublisher` within service implementations. Spring Modulith ensures events stay within their bounded context unless explicitly published as integration events.

Event pattern:

```java
// In service implementation
private final ApplicationEventPublisher eventPublisher;

// After successful operation
eventPublisher.publishEvent(new HeroSectionCreated(entity.getId(), entity.getHeadline(), entity.getStatus().name()));
```

Event record pattern:

```java
package com.simplecms.adminportal.herosection;

import java.util.UUID;

/**
 * Published when a new hero section is created.
 */
public record HeroSectionCreated(UUID heroSectionId, String headline, String status) {}
```

### 17.2 MapStruct Conventions

- All mappers use `@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)`.
- Mappers are package-private within the `internal` package.
- Entity-to-DTO mapping only; DTOs are immutable records.
- Use `@Getter`/`@Setter` on entities (not `@Data`) to be explicit and avoid equals/hashCode issues with JPA.
- Lombok-MapStruct binding is configured in the Maven compiler plugin.

### 17.3 Testing Strategy

| Layer | Tool | Scope |
|-------|------|-------|
| Module Structure | `ApplicationModules.of(AdminPortalApplication.class).verify()` | Verify Spring Modulith module boundaries |
| Service (Unit) | JUnit 5 + Mockito | Business logic, validation rules |
| Repository (Integration) | `@DataJpaTest` + Testcontainers PostgreSQL | Query correctness, constraint validation |
| Controller (Integration) | `@WebMvcTest` + `@WithMockUser` | HTTP status, model attributes, view names |
| Security | `spring-security-test` | Access control per role |
| End-to-End | `@SpringBootTest` + Testcontainers | Full request lifecycle |

Example modulith verification test:

```java
package com.simplecms.adminportal;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithStructureTest {

    @Test
    void verifyModuleStructure() {
        ApplicationModules.of(AdminPortalApplication.class).verify();
    }
}
```

Example security test:

```java
@WebMvcTest(UserManagementPageController.class)
class UserManagementSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAccessUserManagement() throws Exception {
        mockMvc.perform(get("/admin/users"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessUserManagement() throws Exception {
        mockMvc.perform(get("/admin/users"))
               .andExpect(status().isOk());
    }
}
```

---

## Changelog

| Version | Date | Description |
|---------|------|-------------|
| v1.0.0 | 2026-03-15 | Initial root specification covering all 9 modules |
