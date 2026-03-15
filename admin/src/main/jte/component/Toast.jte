<div x-data class="fixed top-20 right-6 z-50 flex flex-col gap-2 w-80">
    <template x-for="msg in $store.toast.messages" :key="msg.id">
        <div x-transition
             :class="{
                 'bg-success/10 border-success/30 text-success': msg.type === 'success',
                 'bg-danger/10 border-danger/30 text-danger': msg.type === 'error',
                 'bg-warning/10 border-warning/30 text-warning': msg.type === 'warning',
                 'bg-primary/10 border-primary/30 text-primary': msg.type === 'info'
             }"
             class="p-3 border rounded-card text-sm shadow-lg flex items-center justify-between">
            <span x-text="msg.text"></span>
            <button @click="$store.toast.remove(msg.id)" class="ml-2 opacity-70 hover:opacity-100">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>
    </template>
</div>
