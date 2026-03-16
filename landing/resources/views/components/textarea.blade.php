@props(['name', 'rows' => 5, 'value' => ''])

<textarea name="{{ $name }}" id="{{ $name }}" rows="{{ $rows }}"
          {{ $attributes->merge(['class' => 'w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary resize-none']) }}>{{ old($name, $value) }}</textarea>
