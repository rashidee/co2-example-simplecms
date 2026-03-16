@param com.simplecms.adminportal.feature.internal.FeatureFormView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String currentPath = ""
@param String userRole = ""
@param String userInitials = ""
@param String userName = ""
@param String cspNonce = ""
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout.MainLayout(
    vite = vite,
    currentPath = currentPath,
    userRole = userRole,
    userInitials = userInitials,
    userName = userName,
    cspNonce = cspNonce,
    _csrf = _csrf,
    content = @`
    <div class="max-w-2xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Edit Feature</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/features-section/${view.feature().id().toString()}/edit" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div x-data="{ selectedIcon: '${view.feature().icon()}' }">
                    <label for="icon" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Icon</label>
                    <div class="flex items-center gap-3">
                        <select id="icon" name="icon" required x-model="selectedIcon"
                                class="flex-1 px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            <option value="">Select an icon...</option>
                            <option value="fa-solid fa-bolt">Bolt</option>
                            <option value="fa-solid fa-shield-halved">Shield</option>
                            <option value="fa-solid fa-rocket">Rocket</option>
                            <option value="fa-solid fa-gear">Gear</option>
                            <option value="fa-solid fa-chart-line">Chart Line</option>
                            <option value="fa-solid fa-cloud">Cloud</option>
                            <option value="fa-solid fa-lock">Lock</option>
                            <option value="fa-solid fa-code">Code</option>
                            <option value="fa-solid fa-database">Database</option>
                            <option value="fa-solid fa-globe">Globe</option>
                            <option value="fa-solid fa-heart">Heart</option>
                            <option value="fa-solid fa-star">Star</option>
                            <option value="fa-solid fa-users">Users</option>
                            <option value="fa-solid fa-bell">Bell</option>
                            <option value="fa-solid fa-check-circle">Check Circle</option>
                            <option value="fa-solid fa-cog">Cog</option>
                            <option value="fa-solid fa-lightbulb">Lightbulb</option>
                            <option value="fa-solid fa-puzzle-piece">Puzzle Piece</option>
                            <option value="fa-solid fa-wrench">Wrench</option>
                            <option value="fa-solid fa-mobile-screen">Mobile</option>
                        </select>
                        <div x-show="selectedIcon" class="w-10 h-10 flex items-center justify-center border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] bg-gray-50 dark:bg-slate-700 text-xl text-[#2271b1]">
                            <i :class="selectedIcon"></i>
                        </div>
                    </div>
                </div>
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           value="${view.feature().title()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="description" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Description</label>
                    <textarea id="description" name="description" required maxlength="500" rows="4"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.feature().description()}</textarea>
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required min="0"
                           value="${view.feature().displayOrder()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Update Feature
                    </button>
                    <a href="/features-section"
                       class="px-4 py-2.5 bg-white dark:bg-slate-800 hover:bg-gray-50 text-[#1e1e1e] dark:text-slate-100 text-sm font-medium border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
