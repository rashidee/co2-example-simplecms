<?php

namespace Modules\TeamSection\Providers;

use Illuminate\Support\ServiceProvider;

class TeamSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'TeamSection';
    protected string $nameLower = 'teamsection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
    }

    public function register(): void {}
}
