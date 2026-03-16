@props(['class' => ''])

<div {{ $attributes->merge(['class' => "bg-surface border border-border rounded-card shadow hover:shadow-lg transition overflow-hidden $class"]) }}>
    {{ $slot }}
</div>
