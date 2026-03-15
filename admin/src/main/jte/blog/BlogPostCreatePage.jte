@param com.simplecms.adminportal.blog.internal.BlogPostFormView view
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
    <div class="max-w-3xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">Create Blog Post</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/blog/create" enctype="multipart/form-data" class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="categoryId" class="block text-sm font-medium text-[#1e1e1e] mb-1">Category</label>
                        <select id="categoryId" name="categoryId" required
                                class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            <option value="">Select a category</option>
                            @for(var cat : view.categories())
                                <option value="${cat.id().toString()}">${cat.name()}</option>
                            @endfor
                        </select>
                    </div>
                    <div>
                        <label for="authorId" class="block text-sm font-medium text-[#1e1e1e] mb-1">Author</label>
                        <select id="authorId" name="authorId" required
                                class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                            <option value="">Select an author</option>
                            @for(var editor : view.editors())
                                <option value="${editor.id().toString()}">${editor.firstName()} ${editor.lastName()}</option>
                            @endfor
                        </select>
                    </div>
                </div>
                <div>
                    <label for="title" class="block text-sm font-medium text-[#1e1e1e] mb-1">Title</label>
                    <input type="text" id="title" name="title" required maxlength="100"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="summary" class="block text-sm font-medium text-[#1e1e1e] mb-1">Summary</label>
                    <textarea id="summary" name="summary" required rows="2" maxlength="300"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]"></textarea>
                </div>
                <div>
                    <label for="content" class="block text-sm font-medium text-[#1e1e1e] mb-1">Content</label>
                    <textarea id="content" name="content" required rows="12"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]"></textarea>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="effectiveDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Effective Date</label>
                        <input type="date" id="effectiveDate" name="effectiveDate" required
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                    <div>
                        <label for="expirationDate" class="block text-sm font-medium text-[#1e1e1e] mb-1">Expiration Date (optional)</label>
                        <input type="date" id="expirationDate" name="expirationDate"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                    </div>
                </div>
                <div>
                    <label for="status" class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                    <select id="status" name="status" required
                            class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                        @for(var s : view.statuses())
                            <option value="${s.name()}">${s.name()}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label for="image" class="block text-sm font-medium text-[#1e1e1e] mb-1">Image (1600x500px)</label>
                    <input type="file" id="image" name="image" required accept="image/*"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div class="flex gap-3">
                    <button type="submit"
                            class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
                        Create Post
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
