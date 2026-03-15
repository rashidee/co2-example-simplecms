@param com.simplecms.adminportal.authentication.internal.ForgotPasswordView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout.AuthLayout(vite = vite, cspNonce = cspNonce, pageTitle = "Forgot Password", content = @`
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
        <h2 class="text-lg font-semibold text-[#1e1e1e] mb-2">Forgot Password</h2>
        <p class="text-sm text-[#646970] mb-5">Enter your email address and we'll send you a link to reset your password.</p>

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

        <form method="post" action="/forgot-password" class="space-y-4">
            @if(_csrf != null)
                <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
            @endif
            <div>
                <label for="email" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email Address</label>
                <input type="email" id="email" name="email" required
                       placeholder="you@example.com"
                       class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
            </div>

            <button type="submit"
                    class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                Send Reset Link
            </button>
        </form>

        <div class="mt-4 text-center">
            <a href="/login" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                Back to Login
            </a>
        </div>
    </div>
`)
