<?php

namespace Modules\ContactSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\Services\ContactSectionService;

class ContactSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(ContactSectionServiceInterface::class, ContactSectionService::class);
    }

    public function boot(): void
    {
        $this->loadRoutesFrom(__DIR__ . '/../routes/web.php');
    }
}
