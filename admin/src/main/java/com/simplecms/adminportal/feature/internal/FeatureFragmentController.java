package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureService;
import com.simplecms.adminportal.feature.FeatureStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/features-section/fragments")
@PreAuthorize("hasRole('EDITOR')")
class FeatureFragmentController {

    private final FeatureService featureService;

    FeatureFragmentController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) FeatureStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("features", featureService.list(status, pageable));
        return "feature/fragments/FeatureCardGrid";
    }
}
