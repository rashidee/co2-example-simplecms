@props(['paginator'])

@if($paginator->hasPages())
<nav class="mt-12 flex justify-center items-center space-x-2" aria-label="Pagination">
    {{-- Previous --}}
    @if($paginator->onFirstPage())
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">&laquo; Previous</span>
    @else
        <a href="{{ $paginator->previousPageUrl() }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">&laquo; Previous</a>
    @endif

    {{-- Page Numbers --}}
    @foreach($paginator->getUrlRange(1, $paginator->lastPage()) as $page => $url)
        @if($page == $paginator->currentPage())
            <span class="px-3 py-2 text-sm font-bold text-white bg-primary rounded-btn">{{ $page }}</span>
        @else
            <a href="{{ $url }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary border border-border rounded-btn transition">{{ $page }}</a>
        @endif
    @endforeach

    {{-- Next --}}
    @if($paginator->hasMorePages())
        <a href="{{ $paginator->nextPageUrl() }}" class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">Next &raquo;</a>
    @else
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">Next &raquo;</span>
    @endif
</nav>
@endif
