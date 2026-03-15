@import gg.jte.Content

@param Content header
@param Content body

<div class="overflow-x-auto">
    <table class="w-full text-sm text-left">
        <thead class="text-xs text-text-secondary uppercase bg-slate-50 dark:bg-slate-700/50 border-b border-border dark:border-slate-700">
            ${header}
        </thead>
        <tbody class="divide-y divide-border dark:divide-slate-700">
            ${body}
        </tbody>
    </table>
</div>
