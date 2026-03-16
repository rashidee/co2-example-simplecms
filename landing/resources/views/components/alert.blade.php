@props(['type' => 'info', 'message'])

@php
$colors = match($type) {
    'success' => 'bg-green-50 text-success border-success/30',
    'error' => 'bg-red-50 text-error border-error/30',
    'warning' => 'bg-yellow-50 text-warning border-warning/30',
    default => 'bg-blue-50 text-primary border-primary/30',
};
@endphp

<div class="p-4 rounded-card border {{ $colors }}" role="alert">
    <span class="text-sm font-medium">{{ $message }}</span>
</div>
