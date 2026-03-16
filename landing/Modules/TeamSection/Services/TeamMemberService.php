<?php

namespace Modules\TeamSection\Services;

use Illuminate\Support\Collection;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\TeamSection\DTOs\TeamMemberData;
use Modules\TeamSection\Models\TeamMember;

class TeamMemberService implements TeamMemberServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed
        return TeamMember::orderBy('display_order')
            ->get()
            ->map(fn (TeamMember $m) => new TeamMemberData(
                id: $m->id,
                name: $m->name,
                role: $m->role,
                linkedin_url: $m->linkedin_url,
                display_order: $m->display_order,
                profile_picture_url: $m->image_data
                    ? route('image.show', ['table' => 'team-member', 'id' => $m->id, 'column' => 'image_data'])
                    : null,
            ));
    }
}
