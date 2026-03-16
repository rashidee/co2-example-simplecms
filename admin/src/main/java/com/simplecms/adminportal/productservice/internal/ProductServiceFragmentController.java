package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Fragment controller for htmx partial updates.
 *
 * Traces: USA000045
 */
@Controller
@RequestMapping("/product-and-service/fragments")
@PreAuthorize("hasRole('EDITOR')")
class ProductServiceFragmentController {

    private final ProductServiceService productServiceService;

    ProductServiceFragmentController(ProductServiceService productServiceService) {
        this.productServiceService = productServiceService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<ProductServiceDTO> items = productServiceService.list(pageable);
        model.addAttribute("items", items);
        return "productservice/fragments/ProductServiceCardGrid";
    }
}
