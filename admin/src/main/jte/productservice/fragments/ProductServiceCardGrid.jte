@param org.springframework.data.domain.Page<com.simplecms.adminportal.productservice.ProductServiceDTO> items
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@for(var item : items.getContent())
    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] overflow-hidden">
        <img src="${item.thumbnailPath()}" alt="${item.title()}" class="w-full h-[200px] object-cover">
        <div class="p-4">
            <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mb-1">${item.title()}</h3>
            <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-2">${item.description()}</p>
            <div class="flex items-center justify-between mb-2">
                <span class="text-xs text-[#646970] dark:text-slate-400">Order: ${item.displayOrder()}</span>
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
