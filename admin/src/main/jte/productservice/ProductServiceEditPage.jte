@param com.simplecms.adminportal.productservice.internal.ProductServiceFormView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Edit Product / Service</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/product-and-service/${view.item().id().toString()}/edit" enctype="multipart/form-data" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           value="${view.item().title()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970]">Max 100 characters</p>
                </div>
                <div>
                    <label for="description" class="block text-sm font-medium text-[#1e1e1e] mb-1">Description</label>
                    <textarea id="description" name="description" required maxlength="500" rows="4"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.item().description()}</textarea>
                    <p class="mt-1 text-xs text-[#646970]">Max 500 characters</p>
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Current Image</label>
                    <div class="mb-2 border border-[#c3c4c7] rounded-[4px] overflow-hidden inline-block">
                        <img src="${view.item().thumbnailPath()}" alt="${view.item().title()}"
                             class="w-[200px] h-[200px] object-cover">
                    </div>
                    <label for="image" class="block text-sm font-medium text-[#1e1e1e] mb-1">Replace Image</label>
                    <input type="file" id="image" name="image" accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970]">Required dimensions: 400x400 pixels. Leave empty to keep current image.</p>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="ctaUrl" class="block text-sm font-medium text-[#1e1e1e] mb-1">CTA URL</label>
                        <input type="url" id="ctaUrl" name="ctaUrl"
                               value="${view.item().ctaUrl() != null ? view.item().ctaUrl() : ""}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970]">Optional</p>
                    </div>
                    <div>
                        <label for="ctaText" class="block text-sm font-medium text-[#1e1e1e] mb-1">CTA Text</label>
                        <input type="text" id="ctaText" name="ctaText" maxlength="50"
                               value="${view.item().ctaText() != null ? view.item().ctaText() : ""}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970]">Optional, max 50 characters</p>
                    </div>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] mb-1">Display Order</label>
                        <input type="number" id="displayOrder" name="displayOrder" required min="0"
                               value="${view.item().displayOrder()}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                        <select id="status" name="status" required
                                class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            @for(var s : view.statuses())
                                @if(s == view.item().status())
                                    <option value="${s.name()}" selected>${s.name()}</option>
                                @else
                                    <option value="${s.name()}">${s.name()}</option>
                                @endif
                            @endfor
                        </select>
                    </div>
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Update Product / Service
                    </button>
                    <a href="/product-and-service"
                       class="px-4 py-2.5 bg-white hover:bg-gray-50 text-[#1e1e1e] text-sm font-medium border border-[#c3c4c7] rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
