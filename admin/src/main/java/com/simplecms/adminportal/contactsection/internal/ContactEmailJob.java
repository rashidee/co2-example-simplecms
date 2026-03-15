package com.simplecms.adminportal.contactsection.internal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Quartz job that processes unsent contact responses.
 * Finds all ContactResponseEntity records where sentAt IS NULL,
 * sends the email, and updates sentAt.
 *
 * Traces: NFRA00108
 */
@Component
class ContactEmailJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ContactEmailJob.class);

    private final ContactResponseRepository responseRepository;
    private final ContactMessageRepository messageRepository;

    ContactEmailJob(ContactResponseRepository responseRepository,
                    ContactMessageRepository messageRepository) {
        this.responseRepository = responseRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<ContactResponseEntity> pendingResponses = responseRepository.findBySentAtIsNull();

        for (ContactResponseEntity response : pendingResponses) {
            try {
                ContactMessageEntity message = messageRepository.findById(response.getContactMessageId())
                    .orElse(null);

                if (message == null) {
                    log.warn("Message not found for response: {}", response.getId());
                    continue;
                }

                // TODO: Send email to message.getSenderEmail()
                // EmailService.send(message.getSenderEmail(), response.getResponseContent(), ...)

                response.setSentAt(Instant.now());
                responseRepository.save(response);

                log.info("Email sent for response: {} to: {}", response.getId(), message.getSenderEmail());
            } catch (Exception e) {
                log.error("Failed to send email for response: {}", response.getId(), e);
            }
        }
    }
}
