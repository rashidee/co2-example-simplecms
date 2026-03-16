<?php

namespace Modules\TeamSection\DTOs;

use Spatie\LaravelData\Data;

class TeamMemberData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $name,
        public readonly string $role,
        public readonly string $linkedin_url,
        public readonly int $display_order,
        public readonly ?string $profile_picture_url,
    ) {}
}
