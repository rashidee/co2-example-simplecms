package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;
import com.simplecms.adminportal.contactsection.ContactService;
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
 * Controller for contact messages and responses.
 *
 * Traces: USA000087, USA000090, USA000093
 */
@Controller
@RequestMapping("/contact-section/messages")
@PreAuthorize("hasRole('EDITOR')")
class ContactMessagePageController {

    private final ContactService contactService;

    ContactMessagePageController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    String listMessages(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<ContactMessageDTO> messages = contactService.listMessages(pageable);
        model.addAttribute("view", ContactMessageListView.of(messages));
        return "contactsection/ContactMessageListPage";
    }

    @GetMapping("/{id}")
    String messageDetail(@PathVariable("id") UUID id, Model model) {
        ContactMessageDTO message = contactService.getMessageById(id);
        ContactResponseDTO response = contactService.getResponseByMessageId(id);
        model.addAttribute("view", ContactMessageDetailView.of(message, response));
        return "contactsection/ContactMessageDetailPage";
    }

    @PostMapping("/{id}/respond")
    String submitResponse(
            @PathVariable("id") UUID id,
            @RequestParam("responderName") String responderName,
            @RequestParam("responderEmail") String responderEmail,
            @RequestParam("responseContent") String responseContent,
            RedirectAttributes redirectAttributes) {
        try {
            contactService.submitResponse(id, responderName, responderEmail, responseContent);
            redirectAttributes.addFlashAttribute("successMessage", "Response submitted. Email will be sent shortly.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/contact-section/messages/" + id;
    }
}
