<?php

namespace Modules\ContactSection\Providers;

use Illuminate\Support\Facades\Route;
use Illuminate\Support\ServiceProvider;

class ContactSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'ContactSection';
    protected string $nameLower = 'contactsection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
        $this->loadRoutesFrom(module_path($this->name, 'routes/web.php'));
    }

    public function register(): void {}
}
