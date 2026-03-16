package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of HeroSectionService.
 * v1.0.4: Auto-compute status, BLOB image storage, date validation.
 *
 * Traces: USA000030, USA000033, NFRA00018-036, CONSA0012
 */
@Service
@Transactional
@PreAuthorize("hasRole('EDITOR')")
class HeroSectionServiceImpl implements HeroSectionService {

    private static final Logger log = LoggerFactory.getLogger(HeroSectionServiceImpl.class);
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 500;
    private static final int THUMBNAIL_WIDTH = 400;
    private static final int THUMBNAIL_HEIGHT = 125;

    private final HeroSectionRepository repository;
    private final HeroSectionMapper mapper;

    HeroSectionServiceImpl(HeroSectionRepository repository, HeroSectionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDateTime effectiveDate,
                                     LocalDateTime expirationDate, Pageable pageable) {
        if (status == null && effectiveDate == null && expirationDate == null) {
            return repository.findAllByOrderByEffectiveDateDesc(pageable)
                .map(mapper::toDTO);
        }
        return repository.findWithFilters(
                status != null ? status.name() : null,
                effectiveDate, expirationDate, pageable)
            .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionDTO getById(UUID id) {
        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public HeroSectionDTO create(String headline, String subheadline, String ctaUrl,
                                 String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                                 MultipartFile image) {

        validateDates(effectiveDate, expirationDate);
        BufferedImage bufferedImage = validateAndReadImage(image);

        byte[] imageData = toBytes(image);
        byte[] thumbnailData = generateThumbnailBytes(bufferedImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        HeroSectionStatus status = HeroSectionStatus.computeFromDates(effectiveDate, expirationDate);

        HeroSectionEntity entity = new HeroSectionEntity();
        entity.setImageData(imageData);
        entity.setThumbnailData(thumbnailData);
        entity.setImagePath(null);
        entity.setThumbnailPath(null);
        entity.setHeadline(headline);
        entity.setSubheadline(subheadline);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        HeroSectionEntity saved = repository.save(entity);
        log.info("Hero section created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public HeroSectionDTO update(UUID id, String headline, String subheadline, String ctaUrl,
                                 String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                                 MultipartFile image) {

        validateDates(effectiveDate, expirationDate);

        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));

        if (image != null && !image.isEmpty()) {
            BufferedImage bufferedImage = validateAndReadImage(image);
            entity.setImageData(toBytes(image));
            entity.setThumbnailData(generateThumbnailBytes(bufferedImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
            entity.setImagePath(null);
            entity.setThumbnailPath(null);
        }

        HeroSectionStatus status = HeroSectionStatus.computeFromDates(effectiveDate, expirationDate);

        entity.setHeadline(headline);
        entity.setSubheadline(subheadline);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        HeroSectionEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageData(UUID id) {
        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));
        if (entity.getImageData() != null) {
            return entity.getImageData();
        }
        return getPlaceholderImage();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getThumbnailData(UUID id) {
        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));
        if (entity.getThumbnailData() != null) {
            return entity.getThumbnailData();
        }
        return getPlaceholderImage();
    }

    /**
     * Validate date constraints.
     * v1.0.4: Effective date must be before expiration date.
     */
    private void validateDates(LocalDateTime effectiveDate, LocalDateTime expirationDate) {
        if (expirationDate != null && !effectiveDate.isBefore(expirationDate)) {
            throw new IllegalArgumentException("Effective date must be before expiration date.");
        }
    }

    /**
     * Validate image dimensions are exactly 1600x500 and return the BufferedImage.
     */
    private BufferedImage validateAndReadImage(MultipartFile image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image file");
            }
            if (bufferedImage.getWidth() != IMAGE_WIDTH || bufferedImage.getHeight() != IMAGE_HEIGHT) {
                throw new IllegalArgumentException(
                    String.format("Image must be %dx%d pixels. Got %dx%d.",
                        IMAGE_WIDTH, IMAGE_HEIGHT,
                        bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
            return bufferedImage;
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image file", e);
        }
    }

    private byte[] toBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image bytes", e);
        }
    }

    /**
     * Generate thumbnail as byte array using Scalr.
     */
    private byte[] generateThumbnailBytes(BufferedImage original, int width, int height) {
        try {
            BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    private byte[] getPlaceholderImage() {
        try {
            var resource = getClass().getClassLoader().getResourceAsStream("static/images/placeholder-1600x500.png");
            if (resource != null) {
                return resource.readAllBytes();
            }
            return new byte[0];
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
