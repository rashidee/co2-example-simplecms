@param com.simplecms.adminportal.blog.internal.BlogCategoryListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100">Blog Categories</h1>
        <div class="flex gap-3">
            <a href="/blog" class="px-4 py-2 border border-[#c3c4c7] dark:border-slate-600 text-sm font-medium rounded-[4px] hover:bg-gray-50 dark:hover:bg-slate-700">Back to Posts</a>
            <a href="/blog/categories/create" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New Category</a>
        </div>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] overflow-hidden">
        <table class="w-full text-sm">
            <thead class="bg-gray-50 dark:bg-slate-700 border-b border-[#c3c4c7] dark:border-slate-600">
                <tr>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e] dark:text-slate-100">Name</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e] dark:text-slate-100">Description</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e] dark:text-slate-100">Created</th>
                    <th class="px-4 py-3 text-right font-medium text-[#1e1e1e] dark:text-slate-100">Actions</th>
                </tr>
            </thead>
            <tbody>
                @for(var cat : view.categories())
                    <tr class="border-b border-[#c3c4c7] dark:border-slate-600 last:border-b-0">
                        <td class="px-4 py-3 text-[#1e1e1e] dark:text-slate-100 font-medium">${cat.name()}</td>
                        <td class="px-4 py-3 text-[#646970] dark:text-slate-400">${cat.description() != null ? cat.description() : ""}</td>
                        <td class="px-4 py-3 text-[#646970] dark:text-slate-400">${cat.createdAt().toString()}</td>
                        <td class="px-4 py-3 text-right space-x-2">
                            <a href="/blog/categories/${cat.id().toString()}/edit" class="text-[#2271b1] hover:underline">Edit</a>
                            <form method="post" action="/blog/categories/${cat.id().toString()}/delete" class="inline"
                                  x-data x-on:submit.prevent="if(confirm('Are you sure? Categories with posts cannot be deleted.')) $el.submit()">
                                @if(_csrf != null)
                                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                                @endif
                                <button type="submit" class="text-[#d63638] hover:underline">Delete</button>
                            </form>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
    `
)
