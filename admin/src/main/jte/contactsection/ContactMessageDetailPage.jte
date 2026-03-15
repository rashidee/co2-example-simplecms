@param com.simplecms.adminportal.contactsection.internal.ContactMessageDetailView view
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
        <div class="flex items-center justify-between mb-6">
            <h1 class="text-xl font-bold text-[#1e1e1e]">Message Detail</h1>
            <a href="/contact-section/messages" class="text-sm text-[#2271b1] hover:underline">Back to Messages</a>
        </div>

        @if(view.hasSuccess())
            <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">${view.successMessage()}</div>
        @endif
        @if(view.hasError())
            <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">${view.errorMessage()}</div>
        @endif

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6 mb-6">
            <h2 class="text-lg font-semibold text-[#1e1e1e] mb-4">Message</h2>
            <div class="space-y-3">
                <div>
                    <span class="text-sm font-medium text-[#646970]">From:</span>
                    <span class="text-sm text-[#1e1e1e] ml-2">${view.message().senderName()} (${view.message().senderEmail()})</span>
                </div>
                <div>
                    <span class="text-sm font-medium text-[#646970]">Submitted:</span>
                    <span class="text-sm text-[#1e1e1e] ml-2">${view.message().submittedAt().toString()}</span>
                </div>
                <div>
                    <span class="text-sm font-medium text-[#646970]">Message:</span>
                    <p class="text-sm text-[#1e1e1e] mt-1 whitespace-pre-wrap">${view.message().messageContent()}</p>
                </div>
            </div>
        </div>

        @if(view.hasResponse())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6 mb-6">
                <h2 class="text-lg font-semibold text-[#1e1e1e] mb-4">Response</h2>
                <div class="space-y-3">
                    <div>
                        <span class="text-sm font-medium text-[#646970]">Responder:</span>
                        <span class="text-sm text-[#1e1e1e] ml-2">${view.response().responderName()} (${view.response().responderEmail()})</span>
                    </div>
                    <div>
                        <span class="text-sm font-medium text-[#646970]">Sent:</span>
                        <span class="text-sm text-[#1e1e1e] ml-2">
                            @if(view.response().sentAt() != null)
                                ${view.response().sentAt().toString()}
                            @else
                                Pending delivery
                            @endif
                        </span>
                    </div>
                    <div>
                        <span class="text-sm font-medium text-[#646970]">Response:</span>
                        <p class="text-sm text-[#1e1e1e] mt-1 whitespace-pre-wrap">${view.response().responseContent()}</p>
                    </div>
                </div>
            </div>
        @else
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
                <h2 class="text-lg font-semibold text-[#1e1e1e] mb-4">Submit Response</h2>
                <form method="post" action="/contact-section/messages/${view.message().id().toString()}/respond" class="space-y-4">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                    @endif
                    <div>
                        <label for="responderName" class="block text-sm font-medium text-[#1e1e1e] mb-1">Your Name</label>
                        <input type="text" id="responderName" name="responderName" required
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="responderEmail" class="block text-sm font-medium text-[#1e1e1e] mb-1">Your Email</label>
                        <input type="email" id="responderEmail" name="responderEmail" required
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="responseContent" class="block text-sm font-medium text-[#1e1e1e] mb-1">Response</label>
                        <textarea id="responseContent" name="responseContent" required rows="5"
                                  class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]"></textarea>
                    </div>
                    <button type="submit" class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Send Response</button>
                </form>
            </div>
        @endif
    </div>
    `
)
