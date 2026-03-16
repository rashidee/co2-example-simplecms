@props(['label', 'name', 'required' => false])

<div>
    <label for="{{ $name }}" class="block text-sm font-medium mb-1">
        {{ $label }}@if($required) <span class="text-error">*</span>@endif
    </label>
    {{ $slot }}
    @error($name)
        <p class="text-xs text-error mt-1">{{ $message }}</p>
    @enderror
</div>
