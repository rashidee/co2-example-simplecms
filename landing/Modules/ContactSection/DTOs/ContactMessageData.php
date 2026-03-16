<?php

namespace Modules\ContactSection\DTOs;

use Spatie\LaravelData\Data;

class ContactMessageData extends Data
{
    public function __construct(
        public readonly ?string $id,
        public readonly string $sender_name,
        public readonly string $sender_email,
        public readonly string $message_content,
        public readonly ?string $submitted_at,
    ) {}
}
