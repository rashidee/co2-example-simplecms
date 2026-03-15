@param com.simplecms.adminportal.herosection.internal.HeroSectionListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e]">Hero Sections</h1>
        <a href="/hero-section/create"
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
        <form hx-get="/hero-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Effective Date</label>
                <input type="date" name="effectiveDate" value="${view.filterEffectiveDate() != null ? view.filterEffectiveDate() : ""}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
            </div>
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Expiration Date</label>
                <input type="date" name="expirationDate" value="${view.filterExpirationDate() != null ? view.filterExpirationDate() : ""}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
            </div>
            <button type="submit"
                    class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                Filter
            </button>
        </form>
    </div>

    <!-- Card Grid -->
    <div id="card-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for(var hero : view.heroSections().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${hero.thumbnailPath()}" alt="${hero.headline()}" class="w-full h-[125px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${hero.headline()}</h3>
                    <p class="text-xs text-[#646970] mb-2">${hero.subheadline()}</p>
                    <div class="flex items-center justify-between text-xs text-[#646970]">
                        <span>${hero.effectiveDate().toString()}${hero.expirationDate() != null ? " - " + hero.expirationDate().toString() : ""}</span>
                        <span class="inline-block px-2 py-0.5 font-medium rounded-full
                            ${hero.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              hero.status().name().equals("DRAFT") ? "bg-amber-100 text-amber-700" :
                              hero.status().name().equals("READY") ? "bg-[#2271b1]/10 text-[#2271b1]" :
                              "bg-slate-100 text-slate-600"}">
                            ${hero.status().name()}
                        </span>
                    </div>
                    <div class="mt-3">
                        <a href="/hero-section/${hero.id().toString()}/edit"
                           class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
                    </div>
                </div>
            </div>
        @endfor
    </div>

    <!-- Pagination -->
    @if(view.heroSections().getTotalPages() > 1)
        <div class="mt-6 flex justify-center gap-2">
            @for(int i = 0; i < view.heroSections().getTotalPages(); i++)
                <a href="/hero-section?page=${i}&size=${view.heroSections().getSize()}${view.filterStatus() != null ? "&status=" + view.filterStatus().name() : ""}${view.filterEffectiveDate() != null ? "&effectiveDate=" + view.filterEffectiveDate() : ""}${view.filterExpirationDate() != null ? "&expirationDate=" + view.filterExpirationDate() : ""}"
                   class="px-3 py-1.5 text-sm rounded-[4px] ${i == view.heroSections().getNumber() ? "bg-[#2271b1] text-white" : "bg-white border border-[#c3c4c7] text-[#1e1e1e] hover:bg-gray-50"}">
                    ${i + 1}
                </a>
            @endfor
        </div>
    @endif
    `
)
