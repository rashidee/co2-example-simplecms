@param org.springframework.data.domain.Page<com.simplecms.adminportal.feature.FeatureDTO> features
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@for(var feature : features.getContent())
    <div class="bg-white dark:bg-slate-800 border border-[#c3c4c7] dark:border-slate-600 rounded-[8px] p-4">
        <div class="text-3xl text-[#2271b1] mb-3"><i class="${feature.icon()}"></i></div>
        <h3 class="font-semibold text-[#1e1e1e] dark:text-slate-100 text-sm mb-1">${feature.title()}</h3>
        <p class="text-xs text-[#646970] dark:text-slate-400 mb-2 line-clamp-3">${feature.description()}</p>
        <div class="mt-3 flex gap-3">
            <a href="/features-section/${feature.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
            <form method="post" action="/features-section/${feature.id().toString()}/delete" class="inline"
                  x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this feature?')) $el.submit()">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
            </form>
        </div>
    </div>
@endfor
