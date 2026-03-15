package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Controller for product and service section pages.
 *
 * Traces: USA000036-045
 */
@Controller
@RequestMapping("/product-and-service")
@PreAuthorize("hasRole('EDITOR')")
class ProductServicePageController {

    private final ProductServiceService productServiceService;

    ProductServicePageController(ProductServiceService productServiceService) {
        this.productServiceService = productServiceService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) ProductServiceStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<ProductServiceDTO> items = productServiceService.list(status, pageable);
        model.addAttribute("view", ProductServiceListView.of(items, status));
        return "productservice/ProductServiceListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", ProductServiceFormView.forCreate());
        return "productservice/ProductServiceCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "ctaUrl", required = false) String ctaUrl,
            @RequestParam(value = "ctaText", required = false) String ctaText,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") ProductServiceStatus status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            productServiceService.create(title, description, ctaUrl, ctaText, displayOrder, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service created successfully.");
            return "redirect:/product-and-service";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/product-and-service/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        ProductServiceDTO item = productServiceService.getById(id);
        model.addAttribute("view", ProductServiceFormView.forEdit(item));
        return "productservice/ProductServiceEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "ctaUrl", required = false) String ctaUrl,
            @RequestParam(value = "ctaText", required = false) String ctaText,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") ProductServiceStatus status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            productServiceService.update(id, title, description, ctaUrl, ctaText, displayOrder, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service updated successfully.");
            return "redirect:/product-and-service";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/product-and-service/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            productServiceService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/product-and-service";
    }
}
