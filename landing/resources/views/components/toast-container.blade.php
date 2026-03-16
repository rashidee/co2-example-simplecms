<div x-data="toastStore" class="fixed top-20 right-4 z-50 space-y-2">
    <template x-for="toast in toasts" :key="toast.id">
        <div x-show="toast.visible"
             x-transition:enter="transform ease-out duration-300 transition"
             x-transition:enter-start="translate-x-full opacity-0"
             x-transition:enter-end="translate-x-0 opacity-100"
             x-transition:leave="transition ease-in duration-200"
             x-transition:leave-start="opacity-100"
             x-transition:leave-end="opacity-0"
             :class="{
                'bg-green-50 text-success border-success/30': toast.type === 'success',
                'bg-red-50 text-error border-error/30': toast.type === 'error',
             }"
             class="px-4 py-3 rounded-card border shadow text-sm font-medium max-w-sm"
             x-text="toast.message">
        </div>
    </template>
</div>
