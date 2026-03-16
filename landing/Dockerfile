# Stage 1: Build frontend assets
FROM node:20-alpine AS frontend-builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY vite.config.js ./
COPY resources/ resources/
COPY Modules/ Modules/

RUN npm run build

# Stage 2: PHP application
FROM php:8.2-fpm AS app

# Install system dependencies and PHP extensions
RUN apt-get update && apt-get install -y \
    libpq-dev \
    libzip-dev \
    unzip \
    nginx \
    supervisor \
    && docker-php-ext-install pdo pdo_pgsql zip opcache \
    && rm -rf /var/lib/apt/lists/*

# Install Composer
COPY --from=composer:2 /usr/bin/composer /usr/bin/composer

WORKDIR /var/www/html

# Copy composer files and install dependencies
COPY composer.json composer.lock ./
RUN composer install --no-dev --no-scripts --no-autoloader --prefer-dist

# Copy application source
COPY . .

# Copy built frontend assets from builder stage
COPY --from=frontend-builder /app/public/build public/build

# Generate optimized autoloader and run post-install scripts
RUN composer dump-autoload --optimize \
    && php artisan config:cache \
    && php artisan route:cache \
    && php artisan view:cache

# Set permissions
RUN chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache

# Nginx configuration
COPY <<'NGINX' /etc/nginx/sites-available/default
server {
    listen 8000;
    server_name _;
    root /var/www/html/public;
    index index.php;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        fastcgi_pass 127.0.0.1:9000;
        fastcgi_param SCRIPT_FILENAME $realpath_root$fastcgi_script_name;
        include fastcgi_params;
    }

    location ~ /\.(?!well-known).* {
        deny all;
    }
}
NGINX

# Supervisor configuration to run both nginx and php-fpm
COPY <<'SUPERVISOR' /etc/supervisor/conf.d/app.conf
[supervisord]
nodaemon=true

[program:php-fpm]
command=php-fpm --nodaemonize
autostart=true
autorestart=true

[program:nginx]
command=nginx -g "daemon off;"
autostart=true
autorestart=true
SUPERVISOR

EXPOSE 8000

CMD ["supervisord", "-c", "/etc/supervisor/supervisord.conf"]
