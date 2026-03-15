@extends('layouts.landing')

@section('title', 'Server Error - Simple CMS')

@section('content')
<div class="min-h-[60vh] flex flex-col items-center justify-center text-center px-4">
    <h1 class="text-6xl font-bold text-error mb-4">500</h1>
    <p class="text-xl text-text-secondary mb-8">Something went wrong. Please try again later.</p>
    <a href="/" class="bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
        Back to Home
    </a>
</div>
@endsection
