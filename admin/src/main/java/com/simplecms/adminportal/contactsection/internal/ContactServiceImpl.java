package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Traces: USA000084-093, NFRA00096-108, CONSA0027
 */
@Service
@Transactional
class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactInfoRepository infoRepository;
    private final ContactMessageRepository messageRepository;
    private final ContactResponseRepository responseRepository;
    private final ContactMapper mapper;

    ContactServiceImpl(ContactInfoRepository infoRepository,
                       ContactMessageRepository messageRepository,
                       ContactResponseRepository responseRepository,
                       ContactMapper mapper) {
        this.infoRepository = infoRepository;
        this.messageRepository = messageRepository;
        this.responseRepository = responseRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactInfoDTO getContactInfo() {
        return infoRepository.findSingle()
            .map(mapper::toInfoDTO)
            .orElse(null);
    }

    /**
     * Upsert contact info as single record.
     *
     * Traces: USA000084, CONSA0027
     */
    @Override
    public ContactInfoDTO updateContactInfo(String phoneNumber, String emailAddress,
                                            String physicalAddress, String linkedinUrl) {
        ContactInfoEntity entity = infoRepository.findSingle().orElse(new ContactInfoEntity());

        entity.setPhoneNumber(phoneNumber);
        entity.setEmailAddress(emailAddress);
        entity.setPhysicalAddress(physicalAddress);
        entity.setLinkedinUrl(linkedinUrl);

        if (entity.getId() == null) {
            entity.setCreatedBy("EDITOR");
        }
        entity.setUpdatedBy("EDITOR");

        ContactInfoEntity saved = infoRepository.save(entity);
        log.info("Contact info updated");
        return mapper.toInfoDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessageDTO> listMessages(Pageable pageable) {
        return messageRepository.findAllOrderBySubmittedAtDesc(pageable)
            .map(entity -> {
                boolean hasResponse = responseRepository.existsByContactMessageId(entity.getId());
                ContactMessageDTO dto = mapper.toMessageDTO(entity);
                return new ContactMessageDTO(dto.id(), dto.senderName(), dto.senderEmail(),
                    dto.messageContent(), dto.submittedAt(), hasResponse);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageDTO getMessageById(UUID id) {
        ContactMessageEntity entity = messageRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Message not found: " + id));
        boolean hasResponse = responseRepository.existsByContactMessageId(id);
        ContactMessageDTO dto = mapper.toMessageDTO(entity);
        return new ContactMessageDTO(dto.id(), dto.senderName(), dto.senderEmail(),
            dto.messageContent(), dto.submittedAt(), hasResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponseDTO getResponseByMessageId(UUID messageId) {
        return responseRepository.findByContactMessageId(messageId)
            .map(mapper::toResponseDTO)
            .orElse(null);
    }

    /**
     * Submit a response. sentAt is null -- the Quartz job will pick it up
     * and set sentAt after sending the email.
     *
     * Traces: USA000093, NFRA00108
     */
    @Override
    public ContactResponseDTO submitResponse(UUID messageId, String responderName,
                                             String responderEmail, String responseContent) {
        // Verify message exists
        messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        // Check no existing response
        if (responseRepository.existsByContactMessageId(messageId)) {
            throw new IllegalArgumentException("A response has already been submitted for this message");
        }

        ContactResponseEntity entity = new ContactResponseEntity();
        entity.setContactMessageId(messageId);
        entity.setResponderName(responderName);
        entity.setResponderEmail(responderEmail);
        entity.setResponseContent(responseContent);
        entity.setSentAt(null); // Will be set by Quartz job after email is sent
        entity.setCreatedBy("EDITOR");

        ContactResponseEntity saved = responseRepository.save(entity);
        log.info("Response submitted for message: {}", messageId);
        return mapper.toResponseDTO(saved);
    }
}
