@param com.simplecms.adminportal.contactsection.internal.ContactMessageListView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e]">Contact Messages</h1>
        <a href="/contact-section" class="text-sm text-[#2271b1] hover:underline">Back to Contact Info</a>
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
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Sender Name</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Sender Email</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Submitted At</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Response</th>
                    <th class="px-4 py-3 text-right font-medium text-[#1e1e1e]">Actions</th>
                </tr>
            </thead>
            <tbody>
                @for(var msg : view.messages().getContent())
                    <tr class="border-b border-[#c3c4c7] last:border-b-0">
                        <td class="px-4 py-3 text-[#1e1e1e]">${msg.senderName()}</td>
                        <td class="px-4 py-3 text-[#1e1e1e]">${msg.senderEmail()}</td>
                        <td class="px-4 py-3 text-[#646970]">${msg.submittedAt().toString()}</td>
                        <td class="px-4 py-3">
                            @if(msg.hasResponse())
                                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full bg-[#00a32a]/10 text-[#00a32a]">Responded</span>
                            @else
                                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full bg-[#dba617]/10 text-[#dba617]">Pending</span>
                            @endif
                        </td>
                        <td class="px-4 py-3 text-right">
                            <a href="/contact-section/messages/${msg.id().toString()}" class="text-[#2271b1] hover:underline">View</a>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
    `
)
