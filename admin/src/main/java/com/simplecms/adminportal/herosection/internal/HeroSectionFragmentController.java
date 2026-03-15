package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * Fragment controller for htmx partial updates on the hero section list.
 *
 * Traces: USA000033
 */
@Controller
@RequestMapping("/hero-section/fragments")
@PreAuthorize("hasRole('EDITOR')")
class HeroSectionFragmentController {

    private final HeroSectionService heroSectionService;

    HeroSectionFragmentController(HeroSectionService heroSectionService) {
        this.heroSectionService = heroSectionService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) HeroSectionStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDateTime effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDateTime.parse(effectiveDate) : null;
        LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDateTime.parse(expirationDate) : null;

        Page<HeroSectionDTO> heroSections = heroSectionService.list(status, effDate, expDate, pageable);
        model.addAttribute("heroSections", heroSections);
        return "herosection/fragments/HeroSectionCardGrid";
    }
}
