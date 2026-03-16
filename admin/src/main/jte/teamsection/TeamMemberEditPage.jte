@param com.simplecms.adminportal.teamsection.internal.TeamMemberFormView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Edit Team Member</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/team-section/${view.teamMember().id().toString()}/edit" enctype="multipart/form-data" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="name" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Name</label>
                    <input type="text" id="name" name="name" required maxlength="100"
                           value="${view.teamMember().name()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="role" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Role</label>
                    <input type="text" id="role" name="role" required maxlength="100"
                           value="${view.teamMember().role()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="linkedinUrl" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">LinkedIn URL</label>
                    <input type="url" id="linkedinUrl" name="linkedinUrl"
                           value="${view.teamMember().linkedinUrl() != null ? view.teamMember().linkedinUrl() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required
                           value="${view.teamMember().displayOrder()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Current Picture</label>
                    <img src="${view.teamMember().imageUrl()}" alt="${view.teamMember().name()}" class="w-24 h-24 object-cover rounded-[4px] mb-2">
                </div>
                <div>
                    <label for="profilePicture" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">New Profile Picture (400x400px, optional)</label>
                    <input type="file" id="profilePicture" name="profilePicture" accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
                        Update
                    </button>
                    <a href="/team-section"
                       class="px-4 py-2.5 bg-white dark:bg-slate-800 hover:bg-gray-50 text-[#1e1e1e] dark:text-slate-100 text-sm font-medium border border-[#c3c4c7] dark:border-slate-600 rounded-[4px]">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
