<?php

namespace Modules\TeamSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class TeamMember extends Model
{
    use HasUuids;

    protected $table = 'tms_team_member';

    protected $fillable = [
        'profile_picture_path', 'image_data', 'name', 'role',
        'linkedin_url', 'display_order',
    ];

    protected $casts = [
        'display_order' => 'integer',
    ];
}
