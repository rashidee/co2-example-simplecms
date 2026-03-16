<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactInfo extends Model
{
    use HasUuids;

    protected $table = 'cts_contact_info';

    protected $fillable = [
        'phone_number', 'email_address', 'physical_address', 'linkedin_url',
    ];
}
