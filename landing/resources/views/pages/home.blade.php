<x-layouts.landing title="Simple CMS - Home">

    <!-- Hero Section — USL000003, NFRL00003, NFRL00006, CONSL0006 -->
    @if($heroSlides->isNotEmpty())
    <section id="hero" x-data="{
        current: 0,
        slides: @js($heroSlides->map(fn($s) => [
            'img' => $s->image_url,
            'headline' => $s->headline,
            'sub' => $s->subheadline,
            'cta' => $s->cta_text,
            'url' => $s->cta_url,
        ])->values()),
        next() { this.current = (this.current + 1) % this.slides.length },
        prev() { this.current = (this.current - 1 + this.slides.length) % this.slides.length },
        autoSlide: null,
        startAuto() { this.autoSlide = setInterval(() => this.next(), 5000) },
        stopAuto() { clearInterval(this.autoSlide) }
    }" x-init="startAuto()" class="relative w-full overflow-hidden">
        {{-- Slides --}}
        <template x-for="(slide, idx) in slides" :key="idx">
            <div x-show="current === idx"
                 x-transition:enter="transition ease-out duration-500"
                 x-transition:enter-start="opacity-0"
                 x-transition:enter-end="opacity-100"
                 x-transition:leave="transition ease-in duration-300"
                 x-transition:leave-start="opacity-100"
                 x-transition:leave-end="opacity-0"
                 class="relative w-full" style="min-height:500px;">
                <img :src="slide.img" :alt="slide.headline" class="w-full h-[500px] object-cover">
                <div class="absolute inset-0 bg-black/40 flex flex-col items-center justify-center text-center px-4">
                    <h1 class="text-white text-3xl md:text-5xl font-bold mb-4" x-text="slide.headline"></h1>
                    <p class="text-white/90 text-lg md:text-xl mb-6 max-w-2xl" x-text="slide.sub"></p>
                    <a :href="slide.url" target="_blank" rel="noopener"
                       class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition"
                       x-text="slide.cta"></a>
                </div>
            </div>
        </template>

        {{-- Navigation arrows --}}
        <button @click="prev(); stopAuto(); startAuto()"
                class="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
                aria-label="Previous slide">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
            </svg>
        </button>
        <button @click="next(); stopAuto(); startAuto()"
                class="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
                aria-label="Next slide">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
            </svg>
        </button>

        {{-- Dot indicators --}}
        <div class="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
            <template x-for="(slide, idx) in slides" :key="'dot-'+idx">
                <button @click="current = idx; stopAuto(); startAuto()"
                        :class="current === idx ? 'bg-white' : 'bg-white/50'"
                        class="w-3 h-3 rounded-full transition"
                        :aria-label="'Go to slide '+(idx+1)"></button>
            </template>
        </div>
    </section>
    @else
    <section id="hero" class="bg-page-bg py-20">
        <p class="text-center text-text-secondary">No hero content available.</p>
    </section>
    @endif

    <!-- Products & Services Section — USL000006, NFRL00009, NFRL00012, NFRL00015, NFRL00018 -->
    <section id="products" class="bg-white py-16 md:py-24">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Our Products and Services</h2>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                @foreach($products as $product)
                <x-card>
                    @if($product->image_url)
                        <img src="{{ $product->image_url }}" alt="{{ $product->title }}" class="w-full h-48 object-cover">
                    @endif
                    <div class="p-6">
                        <h3 class="text-lg font-bold mb-2">{{ $product->title }}</h3>
                        <p class="text-text-secondary text-sm mb-4">{{ $product->description }}</p>
                        @if($product->cta_url)
                            <a href="{{ $product->cta_url }}" target="_blank" rel="noopener"
                               class="text-primary font-semibold text-sm hover:text-primary-dark transition">
                                {{ $product->cta_text ?? 'Learn More' }} &rarr;
                            </a>
                        @endif
                    </div>
                </x-card>
                @endforeach
            </div>
        </div>
    </section>

    <!-- Features Section — USL000009, NFRL00021-NFRL00030 -->
    <section id="features" class="bg-page-bg py-16 md:py-24">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Key Features and Benefits</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                @foreach($features as $feature)
                <div class="bg-surface border border-border rounded-card shadow p-8 text-center hover:shadow-lg transition">
                    <div class="inline-flex items-center justify-center mb-4">
                        <i class="{{ $feature->icon }} text-primary" style="font-size: 48px;"></i>
                    </div>
                    <h3 class="text-lg font-bold mb-2">{{ $feature->title }}</h3>
                    <p class="text-text-secondary text-sm">{{ $feature->description }}</p>
                </div>
                @endforeach
            </div>
        </div>
    </section>

    {{-- Testimonials Section — USL000012, NFRL00033-NFRL00048 --}}
    <section id="testimonials" class="bg-white py-16 md:py-24" x-data="{
        current: 0,
        testimonials: @js($testimonials->map(fn($t) => [
            'name' => $t->customer_name,
            'text' => $t->customer_review,
            'stars' => $t->customer_rating,
        ])->values()),
        next() { this.current = (this.current + 1) % this.testimonials.length },
        prev() { this.current = (this.current - 1 + this.testimonials.length) % this.testimonials.length }
    }">
        <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h2 class="text-3xl md:text-4xl font-bold mb-12">What Our Customers Say</h2>

            <div class="relative">
                <template x-for="(t, idx) in testimonials" :key="idx">
                    <div x-show="current === idx"
                         x-transition:enter="transition ease-out duration-500"
                         x-transition:enter-start="opacity-0 translate-y-4"
                         x-transition:enter-end="opacity-100 translate-y-0"
                         x-transition:leave="transition ease-in duration-300"
                         x-transition:leave-start="opacity-100"
                         x-transition:leave-end="opacity-0"
                         class="px-4 md:px-16">
                        {{-- Star rating (NFRL00045 — 24px stars) --}}
                        <div class="flex justify-center mb-4 space-x-1">
                            <template x-for="s in 5" :key="'star-'+idx+'-'+s">
                                <svg class="w-6 h-6" :class="s <= t.stars ? 'text-warning' : 'text-gray-300'" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                </svg>
                            </template>
                        </div>
                        {{-- Review text (NFRL00042 — 16px) --}}
                        <p class="text-base md:text-lg text-text-secondary italic mb-6 leading-relaxed" x-text="'&quot;' + t.text + '&quot;'"></p>
                        {{-- Customer name (NFRL00039 — bold 18px) --}}
                        <p class="text-lg font-bold text-text-primary" x-text="t.name"></p>
                    </div>
                </template>

                {{-- Navigation arrows --}}
                <button @click="prev()" class="absolute left-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow" aria-label="Previous testimonial">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
                </button>
                <button @click="next()" class="absolute right-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow" aria-label="Next testimonial">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
                </button>
            </div>

            {{-- Dot indicators --}}
            <div class="flex justify-center mt-8 space-x-2">
                <template x-for="(t, idx) in testimonials" :key="'tdot-'+idx">
                    <button @click="current = idx" :class="current === idx ? 'bg-primary' : 'bg-gray-300'" class="w-3 h-3 rounded-full transition" :aria-label="'Go to testimonial '+(idx+1)"></button>
                </template>
            </div>
        </div>
    </section>

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

    {{-- Contact Section — USL000018, NFRL00072-NFRL00084 --}}
    <section id="contact" class="bg-white py-16 md:py-24">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Contact Us</h2>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
                {{-- Left: Contact info --}}
                @if($contactInfo)
                <div class="space-y-8">
                    {{-- Phone (NFRL00075) --}}
                    <div class="flex items-start space-x-4">
                        <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 002.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 01-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 00-1.091-.852H4.5A2.25 2.25 0 002.25 4.5v2.25z"/>
                        </svg>
                        <div>
                            <h3 class="font-bold text-lg mb-1">Phone</h3>
                            <p class="text-text-secondary">{{ $contactInfo->phone_number }}</p>
                        </div>
                    </div>

                    {{-- Email --}}
                    <div class="flex items-start space-x-4">
                        <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.32 8.91a2.25 2.25 0 01-1.07-1.916V6.75"/>
                        </svg>
                        <div>
                            <h3 class="font-bold text-lg mb-1">Email</h3>
                            <p class="text-text-secondary">{{ $contactInfo->email_address }}</p>
                        </div>
                    </div>

                    {{-- Address --}}
                    <div class="flex items-start space-x-4">
                        <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"/>
                            <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"/>
                        </svg>
                        <div>
                            <h3 class="font-bold text-lg mb-1">Address</h3>
                            <p class="text-text-secondary">{!! nl2br(e($contactInfo->physical_address)) !!}</p>
                        </div>
                    </div>
                </div>
                @endif

                {{-- Right: Contact form --}}
                <div>
                    <form hx-post="{{ route('contact.store') }}"
                          hx-swap="none"
                          @htmx:after-request.camel="if(event.detail.successful) $el.reset()"
                          class="space-y-6">
                        @csrf

                        <x-form-control label="Name" name="sender_name" :required="true">
                            <x-input name="sender_name" placeholder="Your full name" required />
                        </x-form-control>

                        <x-form-control label="Email" name="sender_email" :required="true">
                            <x-input name="sender_email" type="email" placeholder="you@example.com" required />
                        </x-form-control>

                        <x-form-control label="Message" name="message_content" :required="true">
                            <x-textarea name="message_content" rows="5" maxlength="500"
                                        placeholder="How can we help you? (max 500 characters)" required />
                        </x-form-control>

                        {{-- CAPTCHA placeholder (NFRL00084) --}}
                        <div class="bg-page-bg border border-border rounded-btn p-4 text-center text-text-secondary text-sm">
                            CAPTCHA verification will be integrated here
                        </div>

                        <x-button type="submit" tone="primary" class="w-full">Send Message</x-button>
                    </form>
                </div>
            </div>
        </div>
    </section>

</x-layouts.landing>
