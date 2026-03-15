@param com.simplecms.adminportal.authentication.internal.LoginView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""

@template.layout.AuthLayout(vite = vite, cspNonce = cspNonce, pageTitle = "Login", content = @`
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
        <h2 class="text-lg font-semibold text-[#1e1e1e] mb-5">Log In</h2>

        @if(view.hasError())
            <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                ${view.errorMessage()}
            </div>
        @endif

        @if(view.hasLogoutMessage())
            <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                ${view.logoutMessage()}
            </div>
        @endif

        <form method="post" action="/login" class="space-y-4">
            <div>
                <label for="email" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email</label>
                <input type="email" id="email" name="email" required
                       placeholder="you@example.com"
                       value="${view.email()}"
                       class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-[#1e1e1e] mb-1">Password</label>
                <input type="password" id="password" name="password" required
                       placeholder="Enter your password"
                       class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
            </div>

            <button type="submit"
                    class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                Log In
            </button>
        </form>

        <div class="mt-4 text-center">
            <a href="/forgot-password" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                Forgot your password?
            </a>
        </div>
    </div>
`)
