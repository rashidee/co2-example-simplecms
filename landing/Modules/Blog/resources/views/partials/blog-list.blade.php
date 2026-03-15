<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
    @foreach($posts as $post)
    <div class="bg-surface border border-border rounded-card shadow overflow-hidden">
        <img src="{{ asset($post->image_path) }}"
             alt="{{ $post->title }}"
             class="w-full h-48 object-cover">
        <div class="p-6">
            @if($post->category)
            <span class="inline-block bg-primary/10 text-primary text-xs font-semibold px-2 py-1 rounded mb-2">{{ $post->category->name }}</span>
            @endif
            <h2 class="text-lg font-bold mb-2">
                <a href="/{{ $post->slug }}" class="hover:text-primary transition">{{ $post->title }}</a>
            </h2>
            <p class="text-text-secondary text-sm mb-3">{{ $post->summary }}</p>
            <p class="text-xs text-text-secondary">{{ $post->effective_date->format('M d, Y') }}</p>
        </div>
    </div>
    @endforeach
</div>

@if($posts->hasPages())
<div class="mt-8 flex justify-center">
    {{ $posts->links() }}
</div>
@endif
