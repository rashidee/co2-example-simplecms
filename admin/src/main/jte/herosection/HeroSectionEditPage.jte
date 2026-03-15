@param com.simplecms.adminportal.herosection.internal.HeroSectionFormView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Edit Hero Section</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/hero-section/${view.heroSection().id().toString()}/edit" enctype="multipart/form-data" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="headline" class="block text-sm font-medium text-[#1e1e1e] mb-1">Headline</label>
                    <input type="text" id="headline" name="headline" required maxlength="100"
                           value="${view.heroSection().headline()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970]">Max 100 characters</p>
                </div>
                <div>
                    <label for="subheadline" class="block text-sm font-medium text-[#1e1e1e] mb-1">Subheadline</label>
                    <textarea id="subheadline" name="subheadline" required maxlength="200" rows="3"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.heroSection().subheadline()}</textarea>
                    <p class="mt-1 text-xs text-[#646970]">Max 200 characters</p>
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Current Image</label>
                    <div class="mb-2 border border-[#c3c4c7] rounded-[4px] overflow-hidden">
                        <img src="${view.heroSection().thumbnailPath()}" alt="${view.heroSection().headline()}"
                             class="w-full h-[125px] object-cover">
                    </div>
                    <label for="image" class="block text-sm font-medium text-[#1e1e1e] mb-1">Replace Image</label>
                    <input type="file" id="image" name="image" accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970]">Required dimensions: 1600x500 pixels. Leave empty to keep current image.</p>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="ctaUrl" class="block text-sm font-medium text-[#1e1e1e] mb-1">CTA URL</label>
                        <input type="url" id="ctaUrl" name="ctaUrl" required
                               value="${view.heroSection().ctaUrl()}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="ctaText" class="block text-sm font-medium text-[#1e1e1e] mb-1">CTA Text</label>
                        <input type="text" id="ctaText" name="ctaText" required maxlength="50"
                               value="${view.heroSection().ctaText()}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970]">Max 50 characters</p>
                    </div>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="effectiveDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Effective Date</label>
                        <input type="date" id="effectiveDate" name="effectiveDate" required
                               value="${view.heroSection().effectiveDate().toString()}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="expirationDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Expiration Date</label>
                        <input type="date" id="expirationDate" name="expirationDate"
                               value="${view.heroSection().expirationDate() != null ? view.heroSection().expirationDate().toString() : ""}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970]">Optional</p>
                    </div>
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var s : view.statuses())
                            @if(s == view.heroSection().status())
                                <option value="${s.name()}" selected>${s.name()}</option>
                            @else
                                <option value="${s.name()}">${s.name()}</option>
                            @endif
                        @endfor
                    </select>
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Update Hero Section
                    </button>
                    <a href="/hero-section"
                       class="px-4 py-2.5 bg-white hover:bg-gray-50 text-[#1e1e1e] text-sm font-medium border border-[#c3c4c7] rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
