{{-- USL000015 [v1.0.0] - Team member profiles --}}

@if($teamMembers->isNotEmpty())
<section id="team" class="py-16 bg-surface">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Meet Our Team</h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8">
            @foreach($teamMembers as $member)
            <div class="text-center p-6">
                <img src="{{ asset($member->profile_picture_path) }}"
                     alt="{{ $member->name }}"
                     class="w-32 h-32 rounded-full object-cover mx-auto mb-4">
                <h3 class="text-lg font-bold">{{ $member->name }}</h3>
                <p class="text-text-secondary text-sm mb-2">{{ $member->role }}</p>
                @if($member->linkedin_url)
                <a href="{{ $member->linkedin_url }}"
                   target="_blank"
                   rel="noopener"
                   class="text-primary hover:text-primary-dark text-sm transition">
                    <svg class="w-5 h-5 inline" fill="currentColor" viewBox="0 0 24 24"><path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/></svg>
                    LinkedIn
                </a>
                @endif
            </div>
            @endforeach
        </div>
    </div>
</section>
@endif
