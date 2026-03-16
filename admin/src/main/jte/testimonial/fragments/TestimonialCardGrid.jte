@param org.springframework.data.domain.Page<com.simplecms.adminportal.testimonial.TestimonialDTO> testimonials
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@for(var t : testimonials.getContent())
    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-4">
        <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mb-1">${t.customerName()}</h3>
        <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-3">${t.customerReview()}</p>
        <div class="flex items-center gap-0.5 mb-2">
            @for(int i = 1; i <= 5; i++)
                <span class="text-sm ${i <= t.customerRating() ? "text-[#dba617]" : "text-[#c3c4c7]"}">&#9733;</span>
            @endfor
        </div>
        <div class="mt-3 flex gap-3">
            <a href="/testimonials/${t.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
            <form method="post" action="/testimonials/${t.id().toString()}/delete" class="inline"
                  x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this testimonial?')) $el.submit()">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
            </form>
        </div>
    </div>
@endfor
