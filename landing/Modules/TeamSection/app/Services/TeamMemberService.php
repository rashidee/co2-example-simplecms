<?php

namespace Modules\TeamSection\Services;

use Illuminate\Support\Collection;
use Modules\TeamSection\Models\TeamMember;

class TeamMemberService
{
    public function getActiveMembers(): Collection
    {
        return TeamMember::query()
            ->active()
            ->orderBy('display_order')
            ->get();
    }
}
