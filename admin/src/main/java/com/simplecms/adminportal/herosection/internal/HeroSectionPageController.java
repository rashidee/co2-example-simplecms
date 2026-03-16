package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Controller for hero section pages.
 * v1.0.4: Status auto-computed, image served from BLOB.
 *
 * Traces: USA000030, USA000033
 */
@Controller
@RequestMapping("/hero-section")
@PreAuthorize("hasRole('EDITOR')")
class HeroSectionPageController {

    private final HeroSectionService heroSectionService;

    HeroSectionPageController(HeroSectionService heroSectionService) {
        this.heroSectionService = heroSectionService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) HeroSectionStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDateTime effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate).atStartOfDay() : null;
        LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate).atStartOfDay() : null;

        Page<HeroSectionDTO> heroSections = heroSectionService.list(status, effDate, expDate, pageable);
        model.addAttribute("view", HeroSectionListView.of(heroSections, status,
            effectiveDate, expirationDate));
        return "herosection/HeroSectionListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", HeroSectionFormView.forCreate());
        return "herosection/HeroSectionCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("headline") String headline,
            @RequestParam("subheadline") String subheadline,
            @RequestParam("ctaUrl") String ctaUrl,
            @RequestParam("ctaText") String ctaText,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDateTime effDate = LocalDate.parse(effectiveDate).atStartOfDay();
            LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate).atStartOfDay() : null;

            heroSectionService.create(headline, subheadline, ctaUrl, ctaText,
                effDate, expDate, image);
            redirectAttributes.addFlashAttribute("successMessage", "Hero section created successfully.");
            return "redirect:/hero-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hero-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        HeroSectionDTO heroSection = heroSectionService.getById(id);
        model.addAttribute("view", HeroSectionFormView.forEdit(heroSection));
        return "herosection/HeroSectionEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("headline") String headline,
            @RequestParam("subheadline") String subheadline,
            @RequestParam("ctaUrl") String ctaUrl,
            @RequestParam("ctaText") String ctaText,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDateTime effDate = LocalDate.parse(effectiveDate).atStartOfDay();
            LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate).atStartOfDay() : null;

            heroSectionService.update(id, headline, subheadline, ctaUrl, ctaText,
                effDate, expDate, image);
            redirectAttributes.addFlashAttribute("successMessage", "Hero section updated successfully.");
            return "redirect:/hero-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hero-section/" + id + "/edit";
        }
    }

    /**
     * v1.0.4: Serve original image from BLOB.
     */
    @GetMapping("/{id}/image")
    ResponseEntity<byte[]> serveImage(@PathVariable("id") UUID id) {
        byte[] data = heroSectionService.getImageData(id);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(data);
    }

    /**
     * v1.0.4: Serve thumbnail image from BLOB.
     */
    @GetMapping("/{id}/thumbnail")
    ResponseEntity<byte[]> serveThumbnail(@PathVariable("id") UUID id) {
        byte[] data = heroSectionService.getThumbnailData(id);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(data);
    }
}
