{{-- USL000009 [v1.0.0] - Features listing --}}

@if($features->isNotEmpty())
<section id="features" class="py-16 bg-surface">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Our Features</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($features as $feature)
            <div class="text-center p-6">
                <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-primary/10 text-primary mb-4">
                    <i class="{{ $feature->icon }} text-2xl"></i>
                </div>
                <h3 class="text-xl font-bold mb-2">{{ $feature->title }}</h3>
                <p class="text-text-secondary text-sm">{{ $feature->description }}</p>
            </div>
            @endforeach
        </div>
    </div>
</section>
@endif
