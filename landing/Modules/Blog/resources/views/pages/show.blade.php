<x-layouts.landing title="{{ $post->title }} - Simple CMS">
    {{-- Hero image with title overlay --}}
    <section class="relative bg-gray-900">
        @if($post->image_url)
            <img src="{{ $post->image_url }}"
                 alt="{{ $post->title }}"
                 class="w-full h-64 md:h-96 object-cover opacity-50">
        @else
            <div class="w-full h-64 md:h-96 bg-gray-700"></div>
        @endif
        <div class="absolute inset-0 flex items-center justify-center">
            <div class="text-center px-4">
                <h1 class="text-3xl md:text-5xl font-bold text-white mb-4">{{ $post->title }}</h1>
                <p class="text-gray-300 text-sm">
                    Published {{ \Carbon\Carbon::parse($post->effective_date)->format('M d, Y') }}
                    @if($post->category_name)
                        &middot; {{ $post->category_name }}
                    @endif
                </p>
            </div>
        </div>
    </section>

    {{-- Content --}}
    <section class="py-16 bg-white">
        <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
            <a href="{{ route('blog.index') }}" class="inline-flex items-center text-primary hover:underline text-sm mb-8">
                &larr; Back to Blog
            </a>

            <article class="prose prose-lg max-w-none">
                {!! $post->content !!}
            </article>

            <div class="mt-12 pt-8 border-t border-border">
                <a href="{{ route('blog.index') }}" class="inline-flex items-center text-primary hover:underline text-sm">
                    &larr; Back to Blog
                </a>
            </div>
        </div>
    </section>
</x-layouts.landing>
