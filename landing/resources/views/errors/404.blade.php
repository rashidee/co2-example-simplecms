@extends('layouts.landing')

@section('title', 'Page Not Found - Simple CMS')

@section('content')
<div class="min-h-[60vh] flex flex-col items-center justify-center text-center px-4">
    <h1 class="text-6xl font-bold text-primary mb-4">404</h1>
    <p class="text-xl text-text-secondary mb-8">The page you are looking for could not be found.</p>
    <a href="/" class="bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
        Back to Home
    </a>
</div>
@endsection
