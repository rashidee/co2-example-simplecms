@props(['rating' => 0, 'size' => 'w-6 h-6'])

<div class="flex space-x-1">
    @for ($i = 1; $i <= 5; $i++)
        <svg class="{{ $size }} {{ $i <= $rating ? 'text-warning' : 'text-gray-300' }}"
             fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
        </svg>
    @endfor
</div>
