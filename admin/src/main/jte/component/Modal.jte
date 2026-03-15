@import gg.jte.Content

@param String id = "modal"
@param String title = ""
@param Content content

!{
    var openEvent = "open-modal-" + id;
    var closeEvent = "close-modal-" + id;
}

<div x-data="{ open: false }"
     x-show="open"
     x-on:keydown.escape.window="open = false"
     x-init="$el.addEventListener('${openEvent}', () => open = true); $el.addEventListener('${closeEvent}', () => open = false)"
     class="fixed inset-0 z-50 flex items-center justify-center"
     style="display: none;">
    <div class="fixed inset-0 bg-black/50" @click="open = false"></div>
    <div class="relative bg-surface dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card shadow-xl w-full max-w-lg mx-4 p-6"
         x-transition>
        <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-semibold text-text-primary dark:text-slate-100">${title}</h3>
            <button @click="open = false" class="text-text-secondary hover:text-text-primary">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>
        ${content}
    </div>
</div>
