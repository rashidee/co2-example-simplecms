package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import com.simplecms.adminportal.testimonial.TestimonialService;
import com.simplecms.adminportal.testimonial.TestimonialStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Traces: USA000060-069, NFRA00069-078, CONSA0021
 */
@Service
@Transactional
class TestimonialServiceImpl implements TestimonialService {

    private static final Logger log = LoggerFactory.getLogger(TestimonialServiceImpl.class);

    private final TestimonialRepository repository;
    private final TestimonialMapper mapper;

    TestimonialServiceImpl(TestimonialRepository repository, TestimonialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestimonialDTO> list(TestimonialStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TestimonialDTO getById(UUID id) {
        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public TestimonialDTO create(String customerName, String customerReview,
                                 int customerRating, int displayOrder, TestimonialStatus status) {
        if (customerRating < 1 || customerRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        TestimonialEntity entity = new TestimonialEntity();
        entity.setCustomerName(customerName);
        entity.setCustomerReview(customerReview);
        entity.setCustomerRating(customerRating);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        TestimonialEntity saved = repository.save(entity);
        log.info("Testimonial created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public TestimonialDTO update(UUID id, String customerName, String customerReview,
                                 int customerRating, int displayOrder, TestimonialStatus status) {
        if (customerRating < 1 || customerRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));

        entity.setCustomerName(customerName);
        entity.setCustomerReview(customerReview);
        entity.setCustomerRating(customerRating);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        TestimonialEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));
        repository.delete(entity);
        log.info("Testimonial deleted: {}", id);
    }
}
