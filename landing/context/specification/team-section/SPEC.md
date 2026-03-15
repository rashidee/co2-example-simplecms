# Team Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Team Section
> **nwidart Module:** Modules/TeamSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000015 | As Public I want to be able to see list of team members and their roles, so that I can understand the team behind the products and services offered by the company. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00051 | Section title: "Meet Our Team" | v1.0.0 |
| NFRL00054 | The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00057 | The profile picture will be a circular image with a size of at least 150x150 pixels to ensure that it looks good on all screen sizes. | v1.0.0 |
| NFRL00060 | The name will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the role text. | v1.0.0 |
| NFRL00063 | The role will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes. | v1.0.0 |
| NFRL00066 | The LinkedIn profile link will be displayed as a LinkedIn icon with a size of at least 24px to ensure that it is easy to understand the link at a glance. | v1.0.0 |
| NFRL00069 | Light gray background for the section, and the card will have a shadow effect to make it stand out from the background. | v1.0.0 |

### 1.3 Constraints

_No constraints specific to this module._

---

## 2. Eloquent Model

### 2.1 TeamMember

**File:** `Modules/TeamSection/app/Models/TeamMember.php`

**Table:** `team_members` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\TeamSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class TeamMember extends Model
{
    protected $table = 'team_members';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'display_order' => 'integer',
    ];

    /**
     * Scope: only ACTIVE records.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query->where('status', 'ACTIVE');
    }

    /**
     * Scope: order by display_order ascending.
     */
    public function scopeOrdered(Builder $query): Builder
    {
        return $query->orderBy('display_order', 'asc');
    }
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| profile_picture_path | profile_picture_path | string(500) | Profile photo (400x400, displayed as 150x150 circle) |
| name | name | string(100) | Team member name |
| role | role | string(100) | Job title/position |
| linkedin_url | linkedin_url | string(500) | LinkedIn profile URL |
| display_order | display_order | integer | Display sorting |
| status | status | string (enum) | DRAFT, INACTIVE, ACTIVE |

---

## 3. Service

### 3.1 TeamMemberService

**File:** `Modules/TeamSection/app/Services/TeamMemberService.php`

```php
<?php

namespace Modules\TeamSection\Services;

use Illuminate\Support\Collection;
use Modules\TeamSection\Models\TeamMember;

class TeamMemberService
{
    /**
     * Get all active team members ordered by display_order.
     *
     * @return Collection<int, TeamMember>
     */
    public function getActiveMembers(): Collection
    {
        return TeamMember::query()
            ->active()
            ->ordered()
            ->get();
    }
}
```

---

## 4. Data Transfer Object

### 4.1 TeamMemberData

**File:** `Modules/TeamSection/app/Data/TeamMemberData.php`

```php
<?php

namespace Modules\TeamSection\Data;

use Spatie\LaravelData\Data;

class TeamMemberData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $profile_picture_path,
        public readonly string $name,
        public readonly string $role,
        public readonly string $linkedin_url,
    ) {}
}
```

---

## 5. ViewModel

### 5.1 TeamMemberViewModel

**File:** `Modules/TeamSection/app/ViewModels/TeamMemberViewModel.php`

```php
<?php

namespace Modules\TeamSection\ViewModels;

use Illuminate\Support\Collection;
use Spatie\ViewModels\ViewModel;
use Modules\TeamSection\Data\TeamMemberData;

class TeamMemberViewModel extends ViewModel
{
    public function __construct(
        private readonly Collection $teamMembers,
    ) {}

    /**
     * @return Collection<int, TeamMemberData>
     */
    public function teamMembers(): Collection
    {
        return $this->teamMembers->map(fn ($member) => new TeamMemberData(
            id: $member->id,
            profile_picture_path: $member->profile_picture_path,
            name: $member->name,
            role: $member->role,
            linkedin_url: $member->linkedin_url,
        ));
    }
}
```

---

## 6. Controller

### 6.1 TeamMemberController

**File:** `Modules/TeamSection/app/Http/Controllers/TeamMemberController.php`

```php
<?php

namespace Modules\TeamSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\TeamSection\Services\TeamMemberService;

class TeamMemberController extends Controller
{
    public function __construct(
        private readonly TeamMemberService $teamMemberService,
    ) {}

    /**
     * Get active team members for embedding in the landing page.
     * Called from HomeController, not directly routed.
     */
    public function getActiveMembers()
    {
        return $this->teamMemberService->getActiveMembers();
    }
}
```

---

## 7. Blade Templates

### 7.1 Team Section Component

**File:** `Modules/TeamSection/resources/views/components/team-grid.blade.php`

```blade
{{-- USL000015 [v1.0.0] - Team members listing --}}
{{-- NFRL00051 [v1.0.0] - Section title: "Meet Our Team" --}}
{{-- NFRL00054 [v1.0.0] - 3-col desktop, 1-col mobile --}}
{{-- NFRL00057 [v1.0.0] - Circular 150x150 profile picture --}}
{{-- NFRL00060 [v1.0.0] - Name bold 18px --}}
{{-- NFRL00063 [v1.0.0] - Role 16px --}}
{{-- NFRL00066 [v1.0.0] - LinkedIn icon 24px --}}
{{-- NFRL00069 [v1.0.0] - Light gray bg, shadow --}}

<section id="team" class="bg-page-bg py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Meet Our Team</h2>

        @if($teamMembers->isNotEmpty())
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($teamMembers as $member)
            <div class="bg-surface border border-border rounded-card shadow p-8 text-center hover:shadow-lg transition">
                {{-- Circular profile picture (NFRL00057) --}}
                <img src="{{ asset($member->profile_picture_path) }}"
                     alt="{{ $member->name }}"
                     class="w-[150px] h-[150px] rounded-full mx-auto mb-4 object-cover">

                {{-- Name (NFRL00060) --}}
                <h3 class="text-lg font-bold">{{ $member->name }}</h3>

                {{-- Role (NFRL00063) --}}
                <p class="text-text-secondary text-base mb-3">{{ $member->role }}</p>

                {{-- LinkedIn link (NFRL00066) --}}
                <a href="{{ $member->linkedin_url }}"
                   target="_blank"
                   rel="noopener"
                   aria-label="{{ $member->name }} on LinkedIn">
                    <svg class="w-6 h-6 mx-auto text-text-secondary hover:text-primary transition"
                         fill="currentColor" viewBox="0 0 24 24">
                        <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
                    </svg>
                </a>
            </div>
            @endforeach
        </div>
        @endif
    </div>
</section>
```

---

## 8. Module Registration

### 8.1 module.json

**File:** `Modules/TeamSection/module.json`

```json
{
    "name": "TeamSection",
    "alias": "team-section",
    "description": "Team member profiles grid for the landing page",
    "keywords": ["team", "members", "profiles", "grid", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\TeamSection\\Providers\\TeamSectionServiceProvider"
    ],
    "files": []
}
```

### 8.2 Routes

**File:** `Modules/TeamSection/routes/web.php`

```php
<?php

// No dedicated routes for the Team Section module.
// The section is rendered as part of the home page via HomeController.
```
