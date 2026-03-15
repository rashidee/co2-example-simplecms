package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;
import com.simplecms.adminportal.contactsection.ContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for contact info configuration page.
 *
 * Traces: USA000084
 */
@Controller
@RequestMapping("/contact-section")
@PreAuthorize("hasRole('EDITOR')")
class ContactSectionPageController {

    private final ContactService contactService;

    ContactSectionPageController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    String showContactInfo(Model model) {
        ContactInfoDTO info = contactService.getContactInfo();
        model.addAttribute("view", ContactInfoView.of(info));
        return "contactsection/ContactInfoPage";
    }

    @PostMapping
    String updateContactInfo(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("physicalAddress") String physicalAddress,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            Model model) {
        try {
            ContactInfoDTO updated = contactService.updateContactInfo(phoneNumber, emailAddress, physicalAddress, linkedinUrl);
            model.addAttribute("view", ContactInfoView.withSuccess(updated, "Contact information updated successfully."));
        } catch (IllegalArgumentException e) {
            ContactInfoDTO info = contactService.getContactInfo();
            model.addAttribute("view", ContactInfoView.withError(info, e.getMessage()));
        }
        return "contactsection/ContactInfoPage";
    }
}
