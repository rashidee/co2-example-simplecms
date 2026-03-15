@param com.simplecms.adminportal.testimonial.internal.TestimonialFormView view
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
        <h1 class="text-xl font-bold text-[#1e1e1e] dark:text-slate-100 mb-6">Create New Testimonial</h1>

        <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/testimonials/create" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div>
                    <label for="customerName" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Customer Name</label>
                    <input type="text" id="customerName" name="customerName" required maxlength="100"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="customerReview" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Customer Review</label>
                    <textarea id="customerReview" name="customerReview" required maxlength="1000" rows="5"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]"></textarea>
                </div>
                <div>
                    <label for="customerRating" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Rating (1-5)</label>
                    <select id="customerRating" name="customerRating" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        <option value="5">5 Stars</option>
                        <option value="4">4 Stars</option>
                        <option value="3">3 Stars</option>
                        <option value="2">2 Stars</option>
                        <option value="1">1 Star</option>
                    </select>
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required value="0" min="0"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] dark:text-slate-100 mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var status : view.statuses())
                            @if(status.name().equals("DRAFT"))
                                <option value="${status.name()}" selected>${status.name()}</option>
                            @else
                                <option value="${status.name()}">${status.name()}</option>
                            @endif
                        @endfor
                    </select>
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Create Testimonial
                    </button>
                    <a href="/testimonials"
                       class="px-4 py-2.5 bg-white dark:bg-slate-800 hover:bg-gray-50 text-[#1e1e1e] dark:text-slate-100 text-sm font-medium border border-[#c3c4c7] dark:border-slate-600 rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
