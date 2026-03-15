<?php

namespace Modules\ContactSection\Services;

use Modules\ContactSection\Models\ContactInfo;

class ContactInfoService
{
    public function getContactInfo(): ?ContactInfo
    {
        return ContactInfo::query()->first();
    }
}
