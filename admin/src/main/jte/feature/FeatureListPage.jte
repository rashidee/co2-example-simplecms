@param com.simplecms.adminportal.feature.internal.FeatureListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100">Features</h1>
        <a href="/features-section/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
            Add New
        </a>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-4 mb-6">
        <form hx-get="/features-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
            <button type="submit" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Filter</button>
        </form>
    </div>

    <div id="card-grid" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        @for(var feature : view.features().getContent())
            <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-4">
                <div class="text-3xl text-[#2271b1] mb-3"><i class="${feature.icon()}"></i></div>
                <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mb-1">${feature.title()}</h3>
                <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-3">${feature.description()}</p>
                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                    ${feature.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      feature.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                      "bg-slate-100 text-slate-600"}">
                    ${feature.status().name()}
                </span>
                <div class="mt-3 flex gap-3">
                    <a href="/features-section/${feature.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                    <form method="post" action="/features-section/${feature.id().toString()}/delete" class="inline"
                          x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this feature?')) $el.submit()">
                        @if(_csrf != null)
                            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                        @endif
                        <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                    </form>
                </div>
            </div>
        @endfor
    </div>
    `
)
