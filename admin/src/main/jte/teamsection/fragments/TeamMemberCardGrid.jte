@param org.springframework.data.domain.Page<com.simplecms.adminportal.teamsection.TeamMemberDTO> teamMembers
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@for(var m : teamMembers.getContent())
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
        <img src="${m.profilePicturePath()}" alt="${m.name()}" class="w-full h-[200px] object-cover">
        <div class="p-4">
            <h3 class="font-semibold text-[#1e1e1e] text-sm">${m.name()}</h3>
            <p class="text-xs text-[#646970] mb-1">${m.role()}</p>
            @if(m.linkedinUrl() != null)
                <a href="${m.linkedinUrl()}" target="_blank" class="text-xs text-[#2271b1] hover:underline">LinkedIn</a>
            @endif
            <div class="mt-2">
                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                    ${m.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      m.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                      "bg-[#d63638]/10 text-[#d63638]"}">
                    ${m.status().name()}
                </span>
            </div>
            <div class="mt-3 flex gap-3">
                <a href="/team-section/${m.id().toString()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                <form method="post" action="/team-section/${m.id().toString()}/delete" class="inline"
                      x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                    @endif
                    <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                </form>
            </div>
        </div>
    </div>
@endfor
