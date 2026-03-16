export function carousel(slides = []) {
    return {
        current: 0,
        slides,
        next() { this.current = (this.current + 1) % this.slides.length; },
        prev() { this.current = (this.current - 1 + this.slides.length) % this.slides.length; },
    };
}
