<?php

namespace Modules\ProductAndServiceSection\Exceptions;

use App\Exceptions\WebApplicationException;

class ProductServiceException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Product/Service not found: {$id}", 404);
    }
}
