{{-- USL000012 [v1.0.0] - Testimonials carousel --}}

@if($testimonials->isNotEmpty())
<section id="testimonials" class="py-16 bg-page-bg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">What Our Customers Say</h2>

        <div x-data="{
            current: 0,
            total: {{ $testimonials->count() }},
            next() { this.current = (this.current + 1) % this.total },
            prev() { this.current = (this.current - 1 + this.total) % this.total }
        }" class="relative max-w-3xl mx-auto">

            @foreach($testimonials as $index => $testimonial)
            <div x-show="current === {{ $index }}"
                 x-transition:enter="transition ease-out duration-300"
                 x-transition:enter-start="opacity-0"
                 x-transition:enter-end="opacity-100"
                 x-transition:leave="transition ease-in duration-200"
                 x-transition:leave-start="opacity-100"
                 x-transition:leave-end="opacity-0"
                 class="bg-surface border border-border rounded-card shadow p-8 text-center">
                <div class="flex justify-center mb-4">
                    <x-star-rating :rating="$testimonial->customer_rating" />
                </div>
                <p class="text-text-secondary italic mb-6">"{{ $testimonial->customer_review }}"</p>
                <p class="font-bold text-text-primary">{{ $testimonial->customer_name }}</p>
            </div>
            @endforeach

            {{-- Left arrow --}}
            <button @click="prev()"
                    class="absolute left-0 top-1/2 -translate-y-1/2 -translate-x-12 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
                    aria-label="Previous testimonial">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
            </button>

            {{-- Right arrow --}}
            <button @click="next()"
                    class="absolute right-0 top-1/2 -translate-y-1/2 translate-x-12 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
                    aria-label="Next testimonial">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                </svg>
            </button>

            {{-- Dot indicators --}}
            <div class="flex justify-center space-x-2 mt-6">
                @foreach($testimonials as $index => $testimonial)
                <button @click="current = {{ $index }}"
                        :class="current === {{ $index }} ? 'bg-primary' : 'bg-gray-300'"
                        class="w-3 h-3 rounded-full transition"
                        aria-label="Go to testimonial {{ $index + 1 }}"></button>
                @endforeach
            </div>
        </div>
    </div>
</section>
@endif
