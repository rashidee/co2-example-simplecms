@param com.simplecms.adminportal.user.internal.AccountView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Account Settings</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasSuccess())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                    ${view.successMessage()}
                </div>
            @endif
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <div class="mb-6">
                <label class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Email</label>
                <input type="text" value="${view.email()}" disabled
                       class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] bg-gray-50 dark:bg-slate-700 text-[#646970] dark:text-slate-400">
            </div>

            <h2 class="text-lg font-semibold text-[#1e1e1e] dark:text-slate-100 mb-4">Change Password</h2>
            <form method="post" action="/account" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="newPassword" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" required
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="confirmPassword" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <button type="submit"
                        class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                    Change Password
                </button>
            </form>
        </div>
    </div>
    `
)
