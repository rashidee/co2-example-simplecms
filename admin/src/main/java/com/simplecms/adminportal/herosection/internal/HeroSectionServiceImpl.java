package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of HeroSectionService.
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

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    HeroSectionServiceImpl(HeroSectionRepository repository, HeroSectionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDateTime effectiveDate,
                                     LocalDateTime expirationDate, Pageable pageable) {
        return repository.findWithFilters(status, effectiveDate, expirationDate, pageable)
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
                                 HeroSectionStatus status, MultipartFile image) {

        validateImage(image);

        String imagePath = saveImage(image, "hero");
        String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        HeroSectionEntity entity = new HeroSectionEntity();
        entity.setImagePath(imagePath);
        entity.setThumbnailPath(thumbnailPath);
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
                                 HeroSectionStatus status, MultipartFile image) {

        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imagePath = saveImage(image, "hero");
            String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            entity.setImagePath(imagePath);
            entity.setThumbnailPath(thumbnailPath);
        }

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

    /**
     * Validate image dimensions are exactly 1600x500.
     *
     * Traces: NFRA00021
     */
    private void validateImage(MultipartFile image) {
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
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image file", e);
        }
    }

    private String saveImage(MultipartFile image, String prefix) {
        try {
            String filename = prefix + "-" + UUID.randomUUID() + getExtension(image.getOriginalFilename());
            Path dir = Paths.get(uploadPath, "hero-section");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/hero-section/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Generate thumbnail using Scalr center-crop.
     *
     * Traces: NFRA00036
     */
    private String generateThumbnail(String originalPath, int width, int height) {
        try {
            Path source = Paths.get(uploadPath).resolve(originalPath.replaceFirst("^/uploads/", ""));
            BufferedImage original = ImageIO.read(source.toFile());
            BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT, width, height);

            String thumbFilename = "thumb-" + source.getFileName();
            Path thumbPath = source.getParent().resolve(thumbFilename);
            ImageIO.write(thumbnail, "png", thumbPath.toFile());

            return "/uploads/hero-section/" + thumbFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return ".png";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".png";
    }
}
