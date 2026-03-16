<x-layouts.landing title="Our Blog - Simple CMS">
    <section class="py-16 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-3xl md:text-4xl font-bold text-text-primary mb-12 text-center">Our Blog</h1>

            <div class="space-y-8">
                @forelse($posts as $post)
                    <a href="{{ route('blog.show', $post->slug) }}"
                       class="block bg-white border border-border rounded-card overflow-hidden hover:shadow-lg transition md:flex">
                        {{-- Thumbnail --}}
                        <div class="md:w-72 md:flex-shrink-0">
                            @if($post->thumbnail_url || $post->image_url)
                                <img src="{{ $post->thumbnail_url ?? $post->image_url }}"
                                     alt="{{ $post->title }}"
                                     class="w-full h-48 md:h-full object-cover">
                            @else
                                <div class="w-full h-48 md:h-full bg-gray-200 flex items-center justify-center">
                                    <span class="text-gray-400 text-sm">No image</span>
                                </div>
                            @endif
                        </div>

                        {{-- Text content --}}
                        <div class="p-6 flex flex-col justify-center">
                            @if($post->category_name)
                                <span class="text-xs font-semibold text-primary uppercase tracking-wide mb-2">{{ $post->category_name }}</span>
                            @endif
                            <h2 class="text-xl font-bold text-text-primary mb-2">{{ $post->title }}</h2>
                            <p class="text-text-secondary text-sm mb-3">{{ $post->summary }}</p>
                            <span class="text-xs text-text-secondary">{{ \Carbon\Carbon::parse($post->effective_date)->format('M d, Y') }}</span>
                        </div>
                    </a>
                @empty
                    <p class="text-center text-text-secondary">No blog posts available at this time.</p>
                @endforelse
            </div>

            <x-pagination :paginator="$posts" />
        </div>
    </section>
</x-layouts.landing>
