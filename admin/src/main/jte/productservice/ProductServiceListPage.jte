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
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Products & Services</h1>
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

    <!-- Filters -->
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/product-and-service/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
              class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                <select name="status" class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    <option value="">All</option>
                    @for(var s : view.statuses())
                        @if(s.equals(view.filterStatus()))
                            <option value="${s.name()}" selected>${s.name()}</option>
                        @else
                            <option value="${s.name()}">${s.name()}</option>
                        @endif
                    @endfor
                </select>
            </div>
            <button type="submit"
                    class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                Filter
            </button>
        </form>
    </div>

    <!-- Card Grid -->
    <div id="card-grid" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        @for(var item : view.items().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${item.thumbnailPath()}" alt="${item.title()}" class="w-full h-[200px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${item.title()}</h3>
                    <p class="text-xs text-[#646970] mb-2 line-clamp-2">${item.description()}</p>
                    <div class="flex items-center justify-between mb-2">
                        <span class="text-xs text-[#646970]">Order: ${item.displayOrder()}</span>
                        <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                            ${item.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              item.status().name().equals("DRAFT") ? "bg-amber-100 text-amber-700" :
                              "bg-slate-100 text-slate-600"}">
                            ${item.status().name()}
                        </span>
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
                <a href="/product-and-service?page=${i}&size=${view.items().getSize()}${view.filterStatus() != null ? "&status=" + view.filterStatus().name() : ""}"
                   class="px-3 py-1.5 text-sm rounded-[4px] ${i == view.items().getNumber() ? "bg-[#2271b1] text-white" : "bg-white border border-[#c3c4c7] text-[#1e1e1e] hover:bg-gray-50"}">
                    ${i + 1}
                </a>
            @endfor
        </div>
    @endif
    `
)
