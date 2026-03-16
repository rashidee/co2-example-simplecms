<?php

namespace Modules\ContactSection\Contracts;

use Modules\ContactSection\DTOs\ContactInfoData;
use Modules\ContactSection\DTOs\ContactMessageData;

interface ContactSectionServiceInterface
{
    public function getContactInfo(): ?ContactInfoData;

    public function submitMessage(array $data): ContactMessageData;
}
