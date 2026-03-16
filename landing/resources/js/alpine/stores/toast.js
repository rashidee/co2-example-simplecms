export function toastStore() {
    return {
        toasts: [],
        add({ type = 'info', message = '' }) {
            const id = Date.now();
            this.toasts.push({ id, type, message, visible: true });
            setTimeout(() => {
                this.toasts = this.toasts.filter(t => t.id !== id);
            }, 5000);
        },
    };
}
