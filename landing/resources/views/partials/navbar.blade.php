<nav class="fixed top-0 left-0 right-0 bg-white shadow z-50" x-data="{ mobileOpen: false }">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <!-- Logo -->
            <a href="/" class="text-xl font-bold text-primary">Simple CMS</a>

            <!-- Desktop menu -->
            <div class="hidden md:flex items-center space-x-6 text-sm font-medium text-text-secondary">
                <a href="/#hero" class="hover:text-primary transition">Home</a>
                <a href="/#products" class="hover:text-primary transition">Products & Services</a>
                <a href="/#features" class="hover:text-primary transition">Features</a>
                <a href="/#testimonials" class="hover:text-primary transition">Testimonials</a>
                <a href="/#team" class="hover:text-primary transition">Team</a>
                <a href="/#contact" class="hover:text-primary transition">Contact</a>
                <a href="/blog" class="hover:text-primary transition {{ request()->is('blog*') ? 'text-primary font-bold' : '' }}">Blog</a>
                <span class="border-l border-border h-4"></span>
                <a href="{{ config('app.admin_url', 'https://admin.' . request()->getHost()) }}" target="_blank" rel="noopener"
                   class="hover:text-primary transition">Admin</a>
            </div>

            <!-- Mobile hamburger -->
            <button class="md:hidden p-2 rounded text-text-secondary hover:text-primary"
                    @click="mobileOpen = !mobileOpen" aria-label="Toggle menu">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path x-show="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16"/>
                    <path x-show="mobileOpen" stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>

        <!-- Mobile menu -->
        <div x-show="mobileOpen" x-transition class="md:hidden pb-4 space-y-2 text-sm font-medium text-text-secondary">
            <a href="/#hero" class="block py-2 hover:text-primary" @click="mobileOpen = false">Home</a>
            <a href="/#products" class="block py-2 hover:text-primary" @click="mobileOpen = false">Products & Services</a>
            <a href="/#features" class="block py-2 hover:text-primary" @click="mobileOpen = false">Features</a>
            <a href="/#testimonials" class="block py-2 hover:text-primary" @click="mobileOpen = false">Testimonials</a>
            <a href="/#team" class="block py-2 hover:text-primary" @click="mobileOpen = false">Team</a>
            <a href="/#contact" class="block py-2 hover:text-primary" @click="mobileOpen = false">Contact</a>
            <a href="/blog" class="block py-2 hover:text-primary">Blog</a>
            <a href="{{ config('app.admin_url', 'https://admin.' . request()->getHost()) }}" target="_blank" rel="noopener"
               class="block py-2 hover:text-primary">Admin</a>
        </div>
    </div>
</nav>
