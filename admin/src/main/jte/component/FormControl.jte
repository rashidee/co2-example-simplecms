@param String id = ""
@param String label = ""
@param String type = "text"
@param String name = ""
@param String value = ""
@param String placeholder = ""
@param String error = ""
@param boolean required = false
@param int maxlength = 0

!{
    var borderClass = !error.isEmpty() ? "border-danger" : "border-border";
}

<div>
    @if(!label.isEmpty())
        <label for="${id}" class="block text-sm font-medium text-text-primary dark:text-slate-200 mb-1">
            ${label}
            @if(required)
                <span class="text-danger">*</span>
            @endif
        </label>
    @endif
    @if(required && maxlength > 0)
        <input type="${type}" id="${id}" name="${name}" value="${value}"
               placeholder="${placeholder}" required maxlength="${maxlength}"
               class="w-full px-3 py-2.5 text-sm border ${borderClass} rounded-btn focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200">
    @elseif(required)
        <input type="${type}" id="${id}" name="${name}" value="${value}"
               placeholder="${placeholder}" required
               class="w-full px-3 py-2.5 text-sm border ${borderClass} rounded-btn focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200">
    @elseif(maxlength > 0)
        <input type="${type}" id="${id}" name="${name}" value="${value}"
               placeholder="${placeholder}" maxlength="${maxlength}"
               class="w-full px-3 py-2.5 text-sm border ${borderClass} rounded-btn focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200">
    @else
        <input type="${type}" id="${id}" name="${name}" value="${value}"
               placeholder="${placeholder}"
               class="w-full px-3 py-2.5 text-sm border ${borderClass} rounded-btn focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors dark:bg-slate-700 dark:border-slate-600 dark:text-slate-200">
    @endif
    @if(!error.isEmpty())
        <p class="mt-1 text-xs text-danger">${error}</p>
    @endif
</div>
