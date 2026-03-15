@param org.springframework.data.domain.Page<com.simplecms.adminportal.herosection.HeroSectionDTO> heroSections

@for(var hero : heroSections.getContent())
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
        <img src="${hero.thumbnailPath()}" alt="${hero.headline()}" class="w-full h-[125px] object-cover">
        <div class="p-4">
            <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${hero.headline()}</h3>
            <p class="text-xs text-[#646970] mb-2">${hero.subheadline()}</p>
            <div class="flex items-center justify-between text-xs text-[#646970]">
                <span>${hero.effectiveDate().toString()}${hero.expirationDate() != null ? " - " + hero.expirationDate().toString() : ""}</span>
                <span class="inline-block px-2 py-0.5 font-medium rounded-full
                    ${hero.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      hero.status().name().equals("DRAFT") ? "bg-amber-100 text-amber-700" :
                      hero.status().name().equals("READY") ? "bg-[#2271b1]/10 text-[#2271b1]" :
                      "bg-slate-100 text-slate-600"}">
                    ${hero.status().name()}
                </span>
            </div>
            <div class="mt-3">
                <a href="/hero-section/${hero.id().toString()}/edit"
                   class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
            </div>
        </div>
    </div>
@endfor
