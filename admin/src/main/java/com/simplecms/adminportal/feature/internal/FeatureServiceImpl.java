package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import com.simplecms.adminportal.feature.FeatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of FeatureService.
 *
 * Traces: USA000048-057, NFRA00060-066, CONSA0018
 */
@Service
@Transactional
class FeatureServiceImpl implements FeatureService {

    private static final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository repository;
    private final FeatureMapper mapper;

    FeatureServiceImpl(FeatureRepository repository, FeatureMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeatureDTO> list(Pageable pageable) {
        return repository.findWithFilters(pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureDTO getById(UUID id) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public FeatureDTO create(String icon, String title, String description,
                             int displayOrder) {
        FeatureEntity entity = new FeatureEntity();
        entity.setIcon(icon);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setDisplayOrder(displayOrder);
        entity.setCreatedBy("EDITOR");

        FeatureEntity saved = repository.save(entity);
        log.info("Feature created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public FeatureDTO update(UUID id, String icon, String title, String description,
                             int displayOrder) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));

        entity.setIcon(icon);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setDisplayOrder(displayOrder);
        entity.setUpdatedBy("EDITOR");

        FeatureEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));
        repository.delete(entity);
        log.info("Feature deleted: {}", id);
    }
}
