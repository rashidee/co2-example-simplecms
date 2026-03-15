@param com.simplecms.adminportal.user.internal.UserListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e]">Users</h1>
        <a href="/users/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
            Add New User
        </a>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
        <table class="w-full text-sm">
            <thead class="bg-gray-50 border-b border-[#c3c4c7]">
                <tr>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Email</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Name</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Role</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Status</th>
                    <th class="px-4 py-3 text-right font-medium text-[#1e1e1e]">Actions</th>
                </tr>
            </thead>
            <tbody>
                @for(var user : view.users().getContent())
                    <tr class="border-b border-[#c3c4c7] last:border-b-0">
                        <td class="px-4 py-3 text-[#1e1e1e]">${user.email()}</td>
                        <td class="px-4 py-3 text-[#1e1e1e]">${user.firstName()} ${user.lastName()}</td>
                        <td class="px-4 py-3">${user.role().name()}</td>
                        <td class="px-4 py-3">
                            <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                                ${user.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" : "bg-[#d63638]/10 text-[#d63638]"}">
                                ${user.status().name()}
                            </span>
                        </td>
                        <td class="px-4 py-3 text-right space-x-2">
                            <a href="/users/${user.id().toString()}/edit" class="text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
                            <form method="post" action="/users/${user.id().toString()}/delete" class="inline"
                                  x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this user?')) $el.submit()">
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
