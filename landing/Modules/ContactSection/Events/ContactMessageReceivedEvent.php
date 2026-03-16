<?php

namespace Modules\ContactSection\Events;

use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ContactMessageReceivedEvent
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly string $messageId,
        public readonly string $senderName,
        public readonly string $senderEmail,
        public readonly \DateTimeImmutable $occurredAt,
    ) {}
}
