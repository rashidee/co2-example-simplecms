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
    _csrf = _csrf,
    content = @`
    <div class="max-w-2xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Create Product / Service</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/product-and-service/create" enctype="multipart/form-data" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970] dark:text-slate-400">Max 100 characters</p>
                </div>
                <div>
                    <label for="description" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Description</label>
                    <textarea id="description" name="description" required maxlength="500" rows="4"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]"></textarea>
                    <p class="mt-1 text-xs text-[#646970] dark:text-slate-400">Max 500 characters</p>
                </div>
                <div>
                    <label for="image" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Image</label>
                    <input type="file" id="image" name="image" required accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <p class="mt-1 text-xs text-[#646970] dark:text-slate-400">Required dimensions: 400x400 pixels</p>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="ctaUrl" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">CTA URL</label>
                        <input type="url" id="ctaUrl" name="ctaUrl"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970] dark:text-slate-400">Optional</p>
                    </div>
                    <div>
                        <label for="ctaText" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">CTA Text</label>
                        <input type="text" id="ctaText" name="ctaText" maxlength="50"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <p class="mt-1 text-xs text-[#646970] dark:text-slate-400">Optional, max 50 characters</p>
                    </div>
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required value="0" min="0"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Create Product / Service
                    </button>
                    <a href="/product-and-service"
                       class="px-4 py-2.5 bg-white dark:bg-slate-800 hover:bg-gray-50 text-[#1e1e1e] dark:text-slate-100 text-sm font-medium border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
