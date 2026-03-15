@param com.simplecms.adminportal.teamsection.internal.TeamMemberListView view
@param com.simplecms.adminportal.shared.vite.ViteManifestService vite
@param String currentPath = ""
@param String userRole = ""
@param String userInitials = ""
@param String userName = ""
@param String cspNonce = ""

@template.layout.MainLayout(
    vite = vite,
    currentPath = currentPath,
    userRole = userRole,
    userInitials = userInitials,
    userName = userName,
    cspNonce = cspNonce,
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Team Members</h1>
        <a href="/team-section/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New</a>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/team-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
              class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                <select name="status" class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px]">
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
        @for(var m : view.teamMembers().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${m.profilePicturePath()}" alt="${m.name()}" class="w-full h-[200px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm">${m.name()}</h3>
                    <p class="text-xs text-[#646970] mb-1">${m.role()}</p>
                    @if(m.linkedinUrl() != null)
                        <a href="${m.linkedinUrl()}" target="_blank" class="text-xs text-[#2271b1] hover:underline">LinkedIn</a>
                    @endif
                    <div class="mt-2">
                        <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                            ${m.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              m.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                              "bg-[#d63638]/10 text-[#d63638]"}">
                            ${m.status().name()}
                        </span>
                    </div>
                    <div class="mt-3 flex gap-3">
                        <a href="/team-section/${m.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                        <form method="post" action="/team-section/${m.id().toString()}/delete" class="inline"
                              x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
                            <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        @endfor
    </div>
    `
)
