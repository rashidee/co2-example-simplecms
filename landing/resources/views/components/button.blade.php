@props(['type' => 'button', 'tone' => 'primary', 'href' => null])

@php
$classes = match($tone) {
    'primary' => 'bg-primary hover:bg-primary-dark text-white',
    'secondary' => 'bg-white hover:bg-page-bg text-text-primary border border-border',
    default => 'bg-primary hover:bg-primary-dark text-white',
};
$baseClasses = 'inline-block font-semibold py-3 px-6 rounded-btn transition text-sm';
@endphp

@if($href)
    <a href="{{ $href }}" {{ $attributes->merge(['class' => "$baseClasses $classes"]) }}>{{ $slot }}</a>
@else
    <button type="{{ $type }}" {{ $attributes->merge(['class' => "$baseClasses $classes"]) }}>{{ $slot }}</button>
@endif
