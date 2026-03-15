@param org.springframework.data.domain.Page<com.simplecms.adminportal.blog.BlogPostDTO> posts

@for(var post : posts.getContent())
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
        <img src="${post.thumbnailPath()}" alt="${post.title()}" class="w-full h-[125px] object-cover">
        <div class="p-4">
            <span class="text-xs text-[#2271b1] font-medium">${post.categoryName()}</span>
            <h3 class="font-semibold text-[#1e1e1e] text-sm mt-1 mb-1">${post.title()}</h3>
            <p class="text-xs text-[#646970] mb-2 line-clamp-2">${post.summary()}</p>
            <p class="text-xs text-[#646970] mb-1">By ${post.authorName()}</p>
            <div class="flex items-center justify-between text-xs text-[#646970]">
                <span>${post.effectiveDate().toString()}${post.expirationDate() != null ? " - " + post.expirationDate().toString() : ""}</span>
                <span class="inline-block px-2 py-0.5 font-medium rounded-full
                    ${post.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      post.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                      post.status().name().equals("EXPIRED") ? "bg-[#d63638]/10 text-[#d63638]" :
                      "bg-[#2271b1]/10 text-[#2271b1]"}">
                    ${post.status().name()}
                </span>
            </div>
            <div class="mt-3 flex gap-3">
                <a href="/blog/${post.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                <form method="post" action="/blog/${post.id().toString()}/delete" class="inline"
                      x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
                    <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                </form>
            </div>
        </div>
    </div>
@endfor
