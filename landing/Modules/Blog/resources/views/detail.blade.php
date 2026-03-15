{{-- USL000024 [v1.0.0] - Blog detail --}}

@extends('layouts.blog')

@section('title', $post->title . ' - Simple CMS Blog')
@section('meta_description', $post->summary)

@section('content')
<article class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <div class="mb-8">
        <a href="/blog" class="text-primary hover:text-primary-dark text-sm transition">&larr; Back to Blog</a>
    </div>

    <img src="{{ asset($post->image_path) }}"
         alt="{{ $post->title }}"
         class="w-full h-64 md:h-96 object-cover rounded-card mb-8">

    <div class="mb-4 flex items-center space-x-4">
        @if($post->category)
        <span class="inline-block bg-primary/10 text-primary text-xs font-semibold px-2 py-1 rounded">{{ $post->category->name }}</span>
        @endif
        <span class="text-xs text-text-secondary">{{ $post->effective_date->format('M d, Y') }}</span>
    </div>

    <h1 class="text-3xl md:text-4xl font-bold mb-6">{{ $post->title }}</h1>

    <div class="prose prose-lg max-w-none text-text-secondary">
        {!! $post->content !!}
    </div>
</article>
@endsection
