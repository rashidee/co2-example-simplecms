{{-- USL000006 [v1.0.0] - Product and service listing --}}

@if($products->isNotEmpty())
<section id="products" class="py-16 bg-page-bg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Our Products &amp; Services</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($products as $product)
            <div class="bg-surface border border-border rounded-card shadow overflow-hidden">
                <img src="{{ asset($product->image_path) }}"
                     alt="{{ $product->title }}"
                     class="w-full h-48 object-cover">
                <div class="p-6">
                    <h3 class="text-xl font-bold mb-2">{{ $product->title }}</h3>
                    <p class="text-text-secondary text-sm mb-4">{{ $product->description }}</p>
                    @if($product->cta_url)
                    <a href="{{ $product->cta_url }}"
                       target="_blank"
                       rel="noopener"
                       class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-2 px-6 rounded-btn transition text-sm">
                        {{ $product->cta_text ?? 'Learn More' }}
                    </a>
                    @endif
                </div>
            </div>
            @endforeach
        </div>
    </div>
</section>
@endif
