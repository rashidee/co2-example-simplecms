<?php

namespace Modules\ContactSection\Services;

use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\DTOs\ContactInfoData;
use Modules\ContactSection\DTOs\ContactMessageData;
use Modules\ContactSection\Events\ContactMessageReceivedEvent;
use Modules\ContactSection\Models\ContactInfo;
use Modules\ContactSection\Models\ContactMessage;

class ContactSectionService implements ContactSectionServiceInterface
{
    public function getContactInfo(): ?ContactInfoData
    {
        $info = ContactInfo::first();
        return $info ? ContactInfoData::from($info) : null;
    }

    public function submitMessage(array $data): ContactMessageData
    {
        $message = ContactMessage::create([
            'sender_name' => $data['sender_name'],
            'sender_email' => $data['sender_email'],
            'message_content' => $data['message_content'],
            'submitted_at' => now(),
            'created_by' => 'public',
            'updated_by' => 'public',
        ]);

        event(new ContactMessageReceivedEvent(
            messageId: $message->id,
            senderName: $message->sender_name,
            senderEmail: $message->sender_email,
            occurredAt: new \DateTimeImmutable(),
        ));

        return ContactMessageData::from($message);
    }
}
