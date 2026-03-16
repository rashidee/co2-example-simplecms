@props(['name', 'type' => 'text', 'value' => ''])

<input type="{{ $type }}" name="{{ $name }}" id="{{ $name }}" value="{{ old($name, $value) }}"
       {{ $attributes->merge(['class' => 'w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary']) }}>
