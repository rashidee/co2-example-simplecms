@param String type = "info"
@param String message = ""
@param boolean dismissible = true

!{
    var bgClass = switch (type) {
        case "success" -> "bg-success/10 border-success/30 text-success";
        case "warning" -> "bg-warning/10 border-warning/30 text-warning";
        case "error"   -> "bg-danger/10 border-danger/30 text-danger";
        default        -> "bg-primary/10 border-primary/30 text-primary";
    };
}

<div class="p-3 border rounded-btn text-sm ${bgClass}" x-data="{ show: true }" x-show="show" x-transition>
    <div class="flex items-center justify-between">
        <span>${message}</span>
        @if(dismissible)
            <button @click="show = false" class="ml-2 opacity-70 hover:opacity-100">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        @endif
    </div>
</div>
