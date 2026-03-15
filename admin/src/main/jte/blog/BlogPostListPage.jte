@param com.simplecms.adminportal.blog.internal.BlogPostListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100">Blog Posts</h1>
        <div class="flex gap-3">
            <a href="/blog/categories" class="px-4 py-2 border border-[#c3c4c7] dark:border-slate-600 text-sm font-medium rounded-[4px] hover:bg-gray-50 dark:hover:bg-slate-700">Categories</a>
            <a href="/blog/create" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New Post</a>
        </div>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-4 mb-6">
        <form hx-get="/blog/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
              class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Status</label>
                <select name="status" class="px-3 py-2 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px]">
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
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Effective Date</label>
                <input type="date" name="effectiveDate" value="${view.filterEffectiveDate() != null ? view.filterEffectiveDate() : ""}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px]">
            </div>
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Expiration Date</label>
                <input type="date" name="expirationDate" value="${view.filterExpirationDate() != null ? view.filterExpirationDate() : ""}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px]">
            </div>
            <button type="submit" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Filter</button>
        </form>
    </div>

    <div id="card-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for(var post : view.posts().getContent())
            <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] overflow-hidden">
                <img src="${post.thumbnailPath()}" alt="${post.title()}" class="w-full h-[125px] object-cover">
                <div class="p-4">
                    <span class="text-xs text-[#2271b1] font-medium">${post.categoryName()}</span>
                    <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mt-1 mb-1">${post.title()}</h3>
                    <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-2">${post.summary()}</p>
                    <p class="text-xs text-[#646970] dark:text-slate-400 mb-1">By ${post.authorName()}</p>
                    <div class="flex items-center justify-between text-xs text-[#646970] dark:text-slate-400">
                        <span>${post.effectiveDate().toString()}${post.expirationDate() != null ? " - " + post.expirationDate().toString() : ""}</span>
                        <span class="inline-block px-2 py-0.5 font-medium rounded-full
                            ${post.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              post.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                              post.status().name().equals("EXPIRED") ? "bg-[#d63638]/10 text-[#d63638]" :
                              "bg-[#2271b1]/10 text-[#2271b1]"}">
                            ${post.status().name()}
                        </span>
                    </div>
                    <div class="mt-3 flex gap-3">
                        <a href="/blog/${post.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                        <form method="post" action="/blog/${post.id().toString()}/delete" class="inline"
                              x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
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
    `
)
