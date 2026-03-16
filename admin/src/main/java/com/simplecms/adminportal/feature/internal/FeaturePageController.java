package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import com.simplecms.adminportal.feature.FeatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Traces: USA000048-057
 */
@Controller
@RequestMapping("/features-section")
@PreAuthorize("hasRole('EDITOR')")
class FeaturePageController {

    private final FeatureService featureService;

    FeaturePageController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    String list(
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<FeatureDTO> features = featureService.list(pageable);
        model.addAttribute("view", FeatureListView.of(features));
        return "feature/FeatureListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", FeatureFormView.forCreate());
        return "feature/FeatureCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("icon") String icon,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("displayOrder") int displayOrder,
            RedirectAttributes redirectAttributes) {
        try {
            featureService.create(icon, title, description, displayOrder);
            redirectAttributes.addFlashAttribute("successMessage", "Feature created successfully.");
            return "redirect:/features-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/features-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        FeatureDTO feature = featureService.getById(id);
        model.addAttribute("view", FeatureFormView.forEdit(feature));
        return "feature/FeatureEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("icon") String icon,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("displayOrder") int displayOrder,
            RedirectAttributes redirectAttributes) {
        try {
            featureService.update(id, icon, title, description, displayOrder);
            redirectAttributes.addFlashAttribute("successMessage", "Feature updated successfully.");
            return "redirect:/features-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/features-section/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            featureService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Feature deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/features-section";
    }
}
