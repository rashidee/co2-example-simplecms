<?php

namespace Modules\ContactSection\Services;

use Modules\ContactSection\Models\ContactMessage;

class ContactMessageService
{
    public function store(array $data): ContactMessage
    {
        return ContactMessage::create([
            'sender_name' => $data['sender_name'],
            'sender_email' => $data['sender_email'],
            'message_content' => $data['message_content'],
            'submitted_at' => now(),
        ]);
    }
}
