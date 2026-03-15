@param String userInitials = ""
@param String userName = ""
@param String userRole = ""
@param String cspNonce = ""

!{var roleLower = userRole.toLowerCase();}

<header class="bg-white dark:bg-slate-800 border-b border-border dark:border-slate-700 h-16 flex items-center justify-between px-6 fixed top-0 left-0 right-0 z-30 shadow-sm">
    <div class="flex items-center gap-3">
        <div class="w-8 h-8 rounded-btn bg-primary flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zm10 0a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zm10 0a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"/>
            </svg>
        </div>
        <span class="font-semibold text-lg text-slate-900 dark:text-slate-100">Admin Portal</span>
    </div>

    <div class="flex items-center gap-2">
        <!-- Dark Mode Toggle -->
        <button @click="$store.theme.toggle()"
                class="p-2 text-text-secondary hover:text-text-primary hover:bg-slate-100 dark:hover:bg-slate-700 rounded-btn transition-colors duration-200"
                :title="$store.theme.dark ? 'Switch to light mode' : 'Switch to dark mode'">
            <svg x-show="!$store.theme.dark" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
            </svg>
            <svg x-show="$store.theme.dark" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" style="display:none">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
            </svg>
        </button>

        <div class="w-px h-8 bg-border dark:bg-slate-600 mx-1"></div>

        <!-- User Avatar + Dropdown -->
        <div class="relative" x-data="{ open: false }">
            <button @click="open = !open" @keydown.escape.window="open = false"
                    class="flex items-center gap-2 cursor-pointer p-1 rounded-btn hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors">
                <div class="w-8 h-8 rounded-full bg-primary flex items-center justify-center font-semibold text-sm text-white">
                    ${userInitials}
                </div>
                <svg class="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                </svg>
            </button>
            <div x-show="open" @click.outside="open = false" x-transition
                 class="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-slate-800 border border-border dark:border-slate-700 rounded-card shadow-xl py-1 z-50"
                 style="display: none;">
                <a href="/${roleLower}/profile"
                   class="flex items-center gap-2 px-4 py-2 text-sm text-text-primary dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                    </svg>
                    Profile
                </a>
                <a href="/${roleLower}/account"
                   class="flex items-center gap-2 px-4 py-2 text-sm text-text-primary dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.066 2.573c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.573 1.066c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.066-2.573c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                    Account
                </a>
                <div class="border-t border-slate-100 dark:border-slate-700 my-1"></div>
                <form method="post" action="/logout">
                    <button type="submit"
                            class="flex w-full items-center gap-2 px-4 py-2 text-sm text-danger hover:bg-red-50 dark:hover:bg-red-900/20">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                        </svg>
                        Logout
                    </button>
                </form>
            </div>
        </div>
    </div>
</header>
