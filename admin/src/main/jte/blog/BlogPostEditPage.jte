@param com.simplecms.adminportal.blog.internal.BlogPostFormView view
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
    content = @`
    <div class="max-w-3xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Edit Blog Post</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/blog/${view.post().id().toString()}/edit" enctype="multipart/form-data" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="categoryId" class="block text-sm font-medium text-[#1e1e1e] mb-1">Category</label>
                        <select id="categoryId" name="categoryId" required
                                class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            @for(var cat : view.categories())
                                @if(cat.id().equals(view.post().categoryId()))
                                    <option value="${cat.id().toString()}" selected>${cat.name()}</option>
                                @else
                                    <option value="${cat.id().toString()}">${cat.name()}</option>
                                @endif
                            @endfor
                        </select>
                    </div>
                    <div>
                        <label for="authorId" class="block text-sm font-medium text-[#1e1e1e] mb-1">Author</label>
                        <select id="authorId" name="authorId" required
                                class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            @for(var editor : view.editors())
                                @if(editor.id().equals(view.post().authorId()))
                                    <option value="${editor.id().toString()}" selected>${editor.firstName()} ${editor.lastName()}</option>
                                @else
                                    <option value="${editor.id().toString()}">${editor.firstName()} ${editor.lastName()}</option>
                                @endif
                            @endfor
                        </select>
                    </div>
                </div>
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           value="${view.post().title()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#646970] mb-1">Slug: ${view.post().slug()}</label>
                </div>
                <div>
                    <label for="summary" class="block text-sm font-medium text-[#1e1e1e] mb-1">Summary</label>
                    <textarea id="summary" name="summary" required rows="2" maxlength="300"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.post().summary()}</textarea>
                </div>
                <div>
                    <label for="content" class="block text-sm font-medium text-[#1e1e1e] mb-1">Content</label>
                    <textarea id="content" name="content" required rows="12"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.post().content()}</textarea>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="effectiveDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Effective Date</label>
                        <input type="date" id="effectiveDate" name="effectiveDate" required
                               value="${view.post().effectiveDate().toString()}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="expirationDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Expiration Date (optional)</label>
                        <input type="date" id="expirationDate" name="expirationDate"
                               value="${view.post().expirationDate() != null ? view.post().expirationDate().toString() : ""}"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var s : view.statuses())
                            @if(s.equals(view.post().status()))
                                <option value="${s.name()}" selected>${s.name()}</option>
                            @else
                                <option value="${s.name()}">${s.name()}</option>
                            @endif
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Current Image</label>
                    <img src="${view.post().imagePath()}" alt="${view.post().title()}" class="w-full max-h-[125px] object-cover rounded-[4px] mb-2">
                </div>
                <div>
                    <label for="image" class="block text-sm font-medium text-[#1e1e1e] mb-1">New Image (1600x500px, optional)</label>
                    <input type="file" id="image" name="image" accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
                        Update Post
                    </button>
                    <a href="/blog"
                       class="px-4 py-2.5 bg-white hover:bg-gray-50 text-[#1e1e1e] text-sm font-medium border border-[#c3c4c7] rounded-[4px]">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    `
)
