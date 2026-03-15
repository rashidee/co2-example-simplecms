{{-- USL000018 [v1.0.0] - Contact section with form --}}

<section id="contact" class="py-16 bg-page-bg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Contact Us</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
            {{-- Contact Info --}}
            <div>
                <h3 class="text-xl font-bold mb-6">Get in Touch</h3>
                @if($contactInfo)
                <div class="space-y-4">
                    <div class="flex items-start space-x-3">
                        <svg class="w-6 h-6 text-primary mt-0.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                        </svg>
                        <span class="text-text-secondary">{{ $contactInfo->phone_number }}</span>
                    </div>
                    <div class="flex items-start space-x-3">
                        <svg class="w-6 h-6 text-primary mt-0.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                        </svg>
                        <span class="text-text-secondary">{{ $contactInfo->email_address }}</span>
                    </div>
                    <div class="flex items-start space-x-3">
                        <svg class="w-6 h-6 text-primary mt-0.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                        </svg>
                        <span class="text-text-secondary">{{ $contactInfo->physical_address }}</span>
                    </div>
                </div>
                @endif
            </div>

            {{-- Contact Form --}}
            <div>
                <form hx-post="/contact" hx-target="#contact-form-result" hx-swap="innerHTML"
                      class="space-y-4" x-data="{ messageLength: 0 }">
                    @csrf
                    <div>
                        <label for="sender_name" class="block text-sm font-medium mb-1">Name <span class="text-error">*</span></label>
                        <input type="text" id="sender_name" name="sender_name" required maxlength="100"
                               class="w-full border border-border rounded-btn px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent">
                    </div>
                    <div>
                        <label for="sender_email" class="block text-sm font-medium mb-1">Email <span class="text-error">*</span></label>
                        <input type="email" id="sender_email" name="sender_email" required maxlength="255"
                               class="w-full border border-border rounded-btn px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent">
                    </div>
                    <div>
                        <label for="message_content" class="block text-sm font-medium mb-1">Message <span class="text-error">*</span></label>
                        <textarea id="message_content" name="message_content" required maxlength="500" rows="5"
                                  x-on:keyup="messageLength = $el.value.length"
                                  class="w-full border border-border rounded-btn px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"></textarea>
                        <p class="text-xs text-text-secondary mt-1"><span x-text="messageLength">0</span>/500 characters</p>
                    </div>
                    <button type="submit"
                            class="bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
                        Send Message
                    </button>
                    <div id="contact-form-result" class="mt-4"></div>
                </form>
            </div>
        </div>
    </div>
</section>
