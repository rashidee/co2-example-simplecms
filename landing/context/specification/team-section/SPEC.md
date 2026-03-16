# Team Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000015 | As Public I want to be able to see list of team members and their roles, so that I can understand the team behind the products and services offered by the company. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00051 | Section title: "Meet Our Team" | v1.0.0 |
| NFRL00054 | The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00057 | The profile picture will be a circular image with a size of at least 150x150 pixels. | v1.0.0 |
| NFRL00060 | The name will be displayed in a bold font with a size of at least 18px. | v1.0.0 |
| NFRL00063 | The role will be displayed in a regular font with a size of at least 16px. | v1.0.0 |
| NFRL00066 | The LinkedIn profile link will be displayed as a LinkedIn icon with a size of at least 24px. | v1.0.0 |
| NFRL00069 | Light gray background for the section, and the card will have a shadow effect. | v1.0.0 |

### Constraints
_None for this module._

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Table | `tms_team_member` |
| Mockup Screen | `mockup/pages/home.html` (Team Section) |
| Admin Model | `admin/context/model/team-section/model.md` |

## 2. Service Contract

```php
namespace Modules\TeamSection\Contracts;

use Illuminate\Support\Collection;

interface TeamMemberServiceInterface
{
    /**
     * Get all visible team members.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending.
     *
     * @return Collection<TeamMemberData>
     */
    public function getAllVisible(): Collection;
}
```

## 3. DTO

```php
namespace Modules\TeamSection\DTOs;

use Spatie\LaravelData\Data;

class TeamMemberData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $name,
        public readonly string $role,
        public readonly string $linkedin_url,
        public readonly int $display_order,
        public readonly ?string $profile_picture_url,
    ) {}
}
```

## 4. Exception

```php
namespace Modules\TeamSection\Exceptions;

use App\Exceptions\WebApplicationException;

class TeamMemberException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Team member not found: {$id}", 404);
    }
}
```

## 5. Eloquent Model

```php
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
```

## 6. Migration

**Not applicable.** Admin Portal owns the schema.

## 7. Service Implementation

```php
namespace Modules\TeamSection\Services;

use Illuminate\Support\Collection;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\TeamSection\DTOs\TeamMemberData;
use Modules\TeamSection\Models\TeamMember;

class TeamMemberService implements TeamMemberServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed
        return TeamMember::orderBy('display_order')
            ->get()
            ->map(fn (TeamMember $m) => new TeamMemberData(
                id: $m->id,
                name: $m->name,
                role: $m->role,
                linkedin_url: $m->linkedin_url,
                display_order: $m->display_order,
                profile_picture_url: $m->image_data
                    ? route('image.show', ['table' => 'team-member', 'id' => $m->id, 'column' => 'image_data'])
                    : null,
            ));
    }
}
```

## 8. Blade Template (Section in Home Page)

```blade
{{-- Team Section — USL000015, NFRL00051-NFRL00069 --}}
<section id="team" class="bg-page-bg py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Meet Our Team</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($teamMembers as $member)
            <div class="bg-surface border border-border rounded-card shadow p-8 text-center hover:shadow-lg transition">
                {{-- Circular profile picture (NFRL00057 — 150x150) --}}
                @if($member->profile_picture_url)
                    <img src="{{ $member->profile_picture_url }}" alt="{{ $member->name }}"
                         class="w-[150px] h-[150px] rounded-full mx-auto mb-4 object-cover">
                @else
                    <div class="w-[150px] h-[150px] rounded-full mx-auto mb-4 bg-page-bg flex items-center justify-center text-text-secondary text-2xl font-bold">
                        {{ collect(explode(' ', $member->name))->map(fn($w) => strtoupper(substr($w, 0, 1)))->join('') }}
                    </div>
                @endif

                {{-- Name (NFRL00060 — bold 18px) --}}
                <h3 class="text-lg font-bold">{{ $member->name }}</h3>

                {{-- Role (NFRL00063 — 16px) --}}
                <p class="text-text-secondary text-base mb-3">{{ $member->role }}</p>

                {{-- LinkedIn (NFRL00066 — 24px icon) --}}
                <a href="{{ $member->linkedin_url }}" target="_blank" rel="noopener"
                   aria-label="{{ $member->name }} on LinkedIn">
                    <svg class="w-6 h-6 mx-auto text-text-secondary hover:text-primary transition" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
                    </svg>
                </a>
            </div>
            @endforeach
        </div>
    </div>
</section>
```

## 9. Module Service Provider

```php
namespace Modules\TeamSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\TeamSection\Services\TeamMemberService;

class TeamSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(TeamMemberServiceInterface::class, TeamMemberService::class);
    }

    public function boot(): void {}
}
```
