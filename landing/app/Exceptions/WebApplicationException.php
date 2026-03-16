<?php

namespace App\Exceptions;

use RuntimeException;

class WebApplicationException extends RuntimeException
{
    public function __construct(
        string $message,
        int $code = 500,
        public readonly ?string $userMessage = null,
    ) {
        parent::__construct($message, $code);
    }
}
