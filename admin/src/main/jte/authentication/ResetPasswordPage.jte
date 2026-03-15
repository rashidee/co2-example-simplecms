@param com.simplecms.adminportal.authentication.internal.ResetPasswordView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String cspNonce = ""

@template.layout.AuthLayout(vite = vite, cspNonce = cspNonce, pageTitle = "Reset Password", content = @`
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
        <h2 class="text-lg font-semibold text-[#1e1e1e] mb-2">Reset Password</h2>
        <p class="text-sm text-[#646970] mb-5">Enter your new password below.</p>

        @if(view.hasSuccess())
            <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                ${view.successMessage()}
            </div>
            <div class="text-center mt-4">
                <a href="/login" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Go to Login</a>
            </div>
        @endif

        @if(view.hasError())
            <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                ${view.errorMessage()}
            </div>
        @endif

        @if(view.tokenValid())
            <form method="post" action="/reset-password" class="space-y-4">
                <input type="hidden" name="token" value="${view.token()}">

                <div>
                    <label for="newPassword" class="block text-sm font-medium text-[#1e1e1e] mb-1">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" required
                           placeholder="Enter your new password"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                </div>

                <div>
                    <label for="confirmPassword" class="block text-sm font-medium text-[#1e1e1e] mb-1">Confirm New Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           placeholder="Confirm your new password"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                </div>

                <button type="submit"
                        class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                    Reset Password
                </button>
            </form>
        @endif

        @if(!view.tokenValid() && !view.hasSuccess())
            <div class="text-center mt-4">
                <a href="/forgot-password" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                    Request a new reset link
                </a>
            </div>
        @endif
    </div>
`)
