<?php

namespace Modules\ContactSection\DTOs;

use Spatie\LaravelData\Data;

class ContactInfoData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $phone_number,
        public readonly string $email_address,
        public readonly string $physical_address,
        public readonly string $linkedin_url,
    ) {}
}
