<?php

namespace Modules\TeamSection\Contracts;

use Illuminate\Support\Collection;

interface TeamMemberServiceInterface
{
    /**
     * Get all visible team members.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending.
     *
     * @return Collection<TeamMemberData>
     */
    public function getAllVisible(): Collection;
}
