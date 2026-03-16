<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactMessage extends Model
{
    use HasUuids;

    protected $table = 'cts_contact_message';

    protected $fillable = [
        'sender_name', 'sender_email', 'message_content', 'submitted_at',
        'created_by', 'updated_by',
    ];

    protected $casts = [
        'submitted_at' => 'datetime',
    ];
}
