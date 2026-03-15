@param com.simplecms.adminportal.testimonial.internal.TestimonialFormView view
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
    <div class="max-w-2xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Edit Testimonial</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/testimonials/${view.testimonial().id().toString()}/edit" class="space-y-4">
                <div>
                    <label for="customerName" class="block text-sm font-medium text-[#1e1e1e] mb-1">Customer Name</label>
                    <input type="text" id="customerName" name="customerName" required maxlength="100"
                           value="${view.testimonial().customerName()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="customerReview" class="block text-sm font-medium text-[#1e1e1e] mb-1">Customer Review</label>
                    <textarea id="customerReview" name="customerReview" required maxlength="1000" rows="5"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.testimonial().customerReview()}</textarea>
                </div>
                <div>
                    <label for="customerRating" class="block text-sm font-medium text-[#1e1e1e] mb-1">Rating (1-5)</label>
                    <select id="customerRating" name="customerRating" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(int i = 5; i >= 1; i--)
                            @if(i == view.testimonial().customerRating())
                                <option value="${i}" selected>${i} Star${i > 1 ? "s" : ""}</option>
                            @else
                                <option value="${i}">${i} Star${i > 1 ? "s" : ""}</option>
                            @endif
                        @endfor
                    </select>
                </div>
                <div>
                    <label for="displayOrder" class="block text-sm font-medium text-[#1e1e1e] mb-1">Display Order</label>
                    <input type="number" id="displayOrder" name="displayOrder" required min="0"
                           value="${view.testimonial().displayOrder()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var status : view.statuses())
                            @if(status == view.testimonial().status())
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
                        Update Testimonial
                    </button>
                    <a href="/testimonials"
                       class="px-4 py-2.5 bg-white hover:bg-gray-50 text-[#1e1e1e] text-sm font-medium border border-[#c3c4c7] rounded-[4px] transition-colors duration-200">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
