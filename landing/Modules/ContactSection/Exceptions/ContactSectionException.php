<?php

namespace Modules\ContactSection\Exceptions;

use App\Exceptions\WebApplicationException;

class ContactSectionException extends WebApplicationException
{
    public static function noContactInfo(): self
    {
        return new self('No contact information configured', 404);
    }

    public static function submissionFailed(): self
    {
        return new self('Failed to submit contact message', 500, 'Your message could not be sent. Please try again later.');
    }
}
