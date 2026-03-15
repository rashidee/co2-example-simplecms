@param org.springframework.data.domain.Page<com.simplecms.adminportal.testimonial.TestimonialDTO> testimonials

@for(var t : testimonials.getContent())
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4">
        <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${t.customerName()}</h3>
        <p class="text-xs text-[#646970] mb-2 line-clamp-3">${t.customerReview()}</p>
        <div class="flex items-center gap-0.5 mb-2">
            @for(int i = 1; i <= 5; i++)
                <span class="text-sm ${i <= t.customerRating() ? "text-[#dba617]" : "text-[#c3c4c7]"}">&#9733;</span>
            @endfor
        </div>
        <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
            ${t.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
              t.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
              "bg-slate-100 text-slate-600"}">
            ${t.status().name()}
        </span>
        <div class="mt-3 flex gap-3">
            <a href="/testimonials/${t.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
            <form method="post" action="/testimonials/${t.id().toString()}/delete" class="inline"
                  x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this testimonial?')) $el.submit()">
                <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
            </form>
        </div>
    </div>
@endfor
