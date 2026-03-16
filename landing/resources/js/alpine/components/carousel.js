export function carousel(slides = [], autoSlideMs = 5000) {
    return {
        current: 0,
        slides,
        autoSlide: null,
        next() { this.current = (this.current + 1) % this.slides.length; },
        prev() { this.current = (this.current - 1 + this.slides.length) % this.slides.length; },
        startAuto() { this.autoSlide = setInterval(() => this.next(), autoSlideMs); },
        stopAuto() { clearInterval(this.autoSlide); },
        init() { if (this.slides.length > 1) this.startAuto(); },
    };
}
