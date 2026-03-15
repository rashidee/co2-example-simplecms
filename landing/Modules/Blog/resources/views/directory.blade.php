{{-- USL000021 [v1.0.0] - Blog directory --}}

@extends('layouts.blog')

@section('title', 'Blog - Simple CMS')
@section('meta_description', 'Read the latest articles from Simple CMS')

@section('content')
<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <h1 class="text-3xl font-bold mb-8">Blog</h1>

    <div id="blog-list">
        @include('blog::partials.blog-list', ['posts' => $posts])
    </div>
</div>
@endsection
