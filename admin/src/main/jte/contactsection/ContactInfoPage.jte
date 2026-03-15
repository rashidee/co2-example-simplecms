@param com.simplecms.adminportal.contactsection.internal.ContactInfoView view
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
    content = @`
    <div class="max-w-2xl mx-auto">
        <div class="flex items-center justify-between mb-6">
            <h1 class="text-xl font-bold text-[#1e1e1e]">Contact Information</h1>
            <a href="/contact-section/messages" class="text-sm text-[#2271b1] hover:underline">View Messages</a>
        </div>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasSuccess())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">${view.successMessage()}</div>
            @endif
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">${view.errorMessage()}</div>
            @endif

            <form method="post" action="/contact-section" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="phoneNumber" class="block text-sm font-medium text-[#1e1e1e] mb-1">Phone Number</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" required
                           value="${view.contactInfo() != null ? view.contactInfo().phoneNumber() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="emailAddress" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email</label>
                    <input type="email" id="emailAddress" name="emailAddress" required
                           value="${view.contactInfo() != null ? view.contactInfo().emailAddress() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="physicalAddress" class="block text-sm font-medium text-[#1e1e1e] mb-1">Physical Address</label>
                    <textarea id="physicalAddress" name="physicalAddress" required rows="3" maxlength="500"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.contactInfo() != null ? view.contactInfo().physicalAddress() : ""}</textarea>
                </div>
                <div>
                    <label for="linkedinUrl" class="block text-sm font-medium text-[#1e1e1e] mb-1">LinkedIn URL</label>
                    <input type="url" id="linkedinUrl" name="linkedinUrl"
                           value="${view.contactInfo() != null && view.contactInfo().linkedinUrl() != null ? view.contactInfo().linkedinUrl() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <button type="submit" class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Save</button>
            </form>
        </div>
    </div>
    `
)
