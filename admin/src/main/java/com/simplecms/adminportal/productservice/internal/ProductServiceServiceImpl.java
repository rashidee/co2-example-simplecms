package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
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
import java.util.UUID;

/**
 * Implementation of ProductServiceService.
 *
 * Traces: USA000036-045, NFRA00039-057, CONSA0015
 */
@Service
@Transactional
@PreAuthorize("hasRole('EDITOR')")
class ProductServiceServiceImpl implements ProductServiceService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceServiceImpl.class);
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 400;
    private static final int THUMBNAIL_WIDTH = 200;
    private static final int THUMBNAIL_HEIGHT = 200;

    private final ProductServiceRepository repository;
    private final ProductServiceMapper mapper;

    @Value("${app.upload.base-path:./uploads}")
    private String uploadPath;

    ProductServiceServiceImpl(ProductServiceRepository repository, ProductServiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductServiceDTO> list(ProductServiceStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductServiceDTO getById(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public ProductServiceDTO create(String title, String description, String ctaUrl,
                                    String ctaText, int displayOrder,
                                    ProductServiceStatus status, MultipartFile image) {
        validateImage(image);

        String imagePath = saveImage(image, "product");
        String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        ProductServiceEntity entity = new ProductServiceEntity();
        entity.setImagePath(imagePath);
        entity.setThumbnailPath(thumbnailPath);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        ProductServiceEntity saved = repository.save(entity);
        log.info("Product/Service created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public ProductServiceDTO update(UUID id, String title, String description, String ctaUrl,
                                    String ctaText, int displayOrder,
                                    ProductServiceStatus status, MultipartFile image) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imagePath = saveImage(image, "product");
            String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            entity.setImagePath(imagePath);
            entity.setThumbnailPath(thumbnailPath);
        }

        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        ProductServiceEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        repository.delete(entity);
        log.info("Product/Service deleted: {}", id);
    }

    /**
     * Validate image dimensions are exactly 400x400.
     *
     * Traces: NFRA00039
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
            Path dir = Paths.get(uploadPath, "product-service").toAbsolutePath();
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/product-service/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Generate thumbnail using Scalr center-crop.
     *
     * Traces: NFRA00057
     */
    private String generateThumbnail(String originalPath, int width, int height) {
        try {
            Path source = Paths.get(uploadPath).toAbsolutePath().resolve(originalPath.replaceFirst("^/uploads/", ""));
            BufferedImage original = ImageIO.read(source.toFile());
            BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT, width, height);

            String thumbFilename = "thumb-" + source.getFileName();
            Path thumbPath = source.getParent().resolve(thumbFilename);
            ImageIO.write(thumbnail, "png", thumbPath.toFile());

            return "/uploads/product-service/" + thumbFilename;
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
