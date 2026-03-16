@param com.simplecms.adminportal.productservice.internal.ProductServiceListView view
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
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100">Products & Services</h1>
        <a href="/product-and-service/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
            Add New
        </a>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <!-- Card Grid -->
    <div id="card-grid" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        @for(var item : view.items().getContent())
            <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] overflow-hidden">
                <img src="${item.thumbnailUrl()}" alt="${item.title()}" class="w-full h-[200px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mb-1">${item.title()}</h3>
                    <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-2">${item.description()}</p>
                    <div class="flex items-center justify-between mb-2">
                        <span class="text-xs text-[#646970] dark:text-slate-400">Order: ${item.displayOrder()}</span>
                    </div>
                    <div class="mt-3 flex gap-3">
                        <a href="/product-and-service/${item.id().toString()}/edit"
                           class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
                        <form method="post" action="/product-and-service/${item.id().toString()}/delete" class="inline"
                              x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this item?')) $el.submit()">
                            @if(_csrf != null)
                                <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                            @endif
                            <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        @endfor
    </div>

    <!-- Pagination -->
    @if(view.items().getTotalPages() > 1)
        <div class="mt-6 flex justify-center gap-2">
            @for(int i = 0; i < view.items().getTotalPages(); i++)
                <a href="/product-and-service?page=${i}&size=${view.items().getSize()}"
                   class="px-3 py-1.5 text-sm rounded-[4px] ${i == view.items().getNumber() ? "bg-[#2271b1] text-white" : "bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 text-[#1e1e1e] dark:text-slate-100 hover:bg-gray-50"}">
                    ${i + 1}
                </a>
            @endfor
        </div>
    @endif
    `
)
