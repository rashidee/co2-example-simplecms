<?php

namespace Modules\TeamSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\TeamSection\Services\TeamMemberService;

class TeamSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(TeamMemberServiceInterface::class, TeamMemberService::class);
    }

    public function boot(): void {}
}
