<?php

namespace Modules\FeaturesSection\Exceptions;

use App\Exceptions\WebApplicationException;

class FeatureException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Feature not found: {$id}", 404);
    }
}
