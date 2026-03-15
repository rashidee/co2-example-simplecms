{{-- USL000003 [v1.0.0] - Hero content carousel --}}
{{-- NFRL00003 [v1.0.0] - Auto-slide every 5s, navigation arrows --}}
{{-- NFRL00006 [v1.0.0] - Image 1600x500 --}}
{{-- CONSL0006 [v1.0.0] - No header/footer in hero section --}}

@if($heroes->isNotEmpty())
<section id="hero" x-data="{
    current: 0,
    total: {{ $heroes->count() }},
    next() { this.current = (this.current + 1) % this.total },
    prev() { this.current = (this.current - 1 + this.total) % this.total },
    autoSlide: null,
    startAuto() { this.autoSlide = setInterval(() => this.next(), 5000) },
    stopAuto() { clearInterval(this.autoSlide) }
}" x-init="startAuto()" class="relative w-full overflow-hidden">

    {{-- Slides --}}
    @foreach($heroes as $index => $hero)
    <div x-show="current === {{ $index }}"
         x-transition:enter="transition ease-out duration-500"
         x-transition:enter-start="opacity-0"
         x-transition:enter-end="opacity-100"
         x-transition:leave="transition ease-in duration-300"
         x-transition:leave-start="opacity-100"
         x-transition:leave-end="opacity-0"
         class="relative w-full" style="min-height:500px;">
        <img src="{{ asset($hero->image_path) }}"
             alt="{{ $hero->headline }}"
             class="w-full h-[500px] object-cover">
        <div class="absolute inset-0 bg-black/40 flex flex-col items-center justify-center text-center px-4">
            <h1 class="text-white text-3xl md:text-5xl font-bold mb-4">{{ $hero->headline }}</h1>
            <p class="text-white/90 text-lg md:text-xl mb-6 max-w-2xl">{{ $hero->subheadline }}</p>
            <a href="{{ $hero->cta_url }}"
               target="_blank"
               rel="noopener"
               class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
                {{ $hero->cta_text }}
            </a>
        </div>
    </div>
    @endforeach

    {{-- Left arrow --}}
    <button @click="prev(); stopAuto(); startAuto()"
            class="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Previous slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
        </svg>
    </button>

    {{-- Right arrow --}}
    <button @click="next(); stopAuto(); startAuto()"
            class="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Next slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
        </svg>
    </button>

    {{-- Dot indicators --}}
    <div class="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
        @foreach($heroes as $index => $hero)
        <button @click="current = {{ $index }}; stopAuto(); startAuto()"
                :class="current === {{ $index }} ? 'bg-white' : 'bg-white/50'"
                class="w-3 h-3 rounded-full transition"
                aria-label="Go to slide {{ $index + 1 }}"></button>
        @endforeach
    </div>
</section>
@endif
