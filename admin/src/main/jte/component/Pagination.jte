@param int currentPage = 0
@param int totalPages = 0
@param String baseUrl = ""

@if(totalPages > 1)
<nav class="flex items-center justify-center gap-1 mt-6">
    @if(currentPage > 0)
        <a href="${baseUrl}?page=${currentPage - 1}"
           class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
            Previous
        </a>
    @endif

    @for(int i = 0; i < totalPages; i++)
        @if(i == currentPage)
            <span class="px-3 py-2 text-sm bg-primary text-white rounded-btn">${i + 1}</span>
        @else
            <a href="${baseUrl}?page=${i}"
               class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
                ${i + 1}
            </a>
        @endif
    @endfor

    @if(currentPage < totalPages - 1)
        <a href="${baseUrl}?page=${currentPage + 1}"
           class="px-3 py-2 text-sm border border-border rounded-btn hover:bg-slate-50 dark:border-slate-600 dark:hover:bg-slate-700">
            Next
        </a>
    @endif
</nav>
@endif
