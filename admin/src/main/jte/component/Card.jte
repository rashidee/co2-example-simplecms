@import gg.jte.Content

@param Content content
@param String title = ""
@param String padding = "p-6"

<div class="bg-surface dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card ${padding}">
    @if(!title.isEmpty())
        <h3 class="text-lg font-semibold text-text-primary dark:text-slate-100 mb-4">${title}</h3>
    @endif
    ${content}
</div>
