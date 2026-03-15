@param com.simplecms.adminportal.user.internal.UserFormView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Create New User</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/users/create" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="email" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Email</label>
                    <input type="email" id="email" name="email" required
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="firstName" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">First Name</label>
                    <input type="text" id="firstName" name="firstName" required
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="lastName" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Last Name</label>
                    <input type="text" id="lastName" name="lastName" required
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="role" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Role</label>
                    <select id="role" name="role" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var role : view.roles())
                            <option value="${role.name()}">${role.name()}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var status : view.statuses())
                            <option value="${status.name()}">${status.name()}</option>
                        @endfor
                    </select>
                </div>
                <p class="text-xs text-[#646970] dark:text-slate-400">Default password: "password" (user will be prompted to change on first login)</p>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Create User
                    </button>
                    <a href="/users"
                       class="px-4 py-2.5 bg-white dark:bg-slate-800 hover:bg-gray-50 text-[#1e1e1e] dark:text-slate-100 text-sm font-medium border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
