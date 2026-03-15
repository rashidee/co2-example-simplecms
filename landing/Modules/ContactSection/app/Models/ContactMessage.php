<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactMessage extends Model
{
    use HasUuids;

    protected $table = 'cts_contact_message';

    protected $keyType = 'string';
    public $incrementing = false;

    const CREATED_AT = 'created_at';
    const UPDATED_AT = null;

    protected $fillable = [
        'sender_name',
        'sender_email',
        'message_content',
        'submitted_at',
    ];

    protected $casts = [
        'id' => 'string',
        'submitted_at' => 'datetime',
        'created_at' => 'datetime',
    ];
}
