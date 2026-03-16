<?php

namespace Modules\TeamSection\Exceptions;

use App\Exceptions\WebApplicationException;

class TeamMemberException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Team member not found: {$id}", 404);
    }
}
