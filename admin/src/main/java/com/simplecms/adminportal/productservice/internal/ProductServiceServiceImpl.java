package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Implementation of ProductServiceService.
 * v1.0.4: BLOB image storage, thumbnail generation in-memory.
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

    ProductServiceServiceImpl(ProductServiceRepository repository, ProductServiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductServiceDTO> list(Pageable pageable) {
        return repository.findWithFilters(pageable).map(mapper::toDTO);
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
                                    MultipartFile image) {
        BufferedImage bufferedImage = validateAndReadImage(image);

        byte[] imageData = toBytes(image);
        byte[] thumbnailData = generateThumbnailBytes(bufferedImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        ProductServiceEntity entity = new ProductServiceEntity();
        entity.setImageData(imageData);
        entity.setThumbnailData(thumbnailData);
        entity.setImagePath(null);
        entity.setThumbnailPath(null);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
        entity.setCreatedBy("EDITOR");

        ProductServiceEntity saved = repository.save(entity);
        log.info("Product/Service created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public ProductServiceDTO update(UUID id, String title, String description, String ctaUrl,
                                    String ctaText, int displayOrder,
                                    MultipartFile image) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));

        if (image != null && !image.isEmpty()) {
            BufferedImage bufferedImage = validateAndReadImage(image);
            entity.setImageData(toBytes(image));
            entity.setThumbnailData(generateThumbnailBytes(bufferedImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
            entity.setImagePath(null);
            entity.setThumbnailPath(null);
        }

        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
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

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageData(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        if (entity.getImageData() != null) {
            return entity.getImageData();
        }
        return getPlaceholderImage();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getThumbnailData(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        if (entity.getThumbnailData() != null) {
            return entity.getThumbnailData();
        }
        return getPlaceholderImage();
    }

    /**
     * Validate image dimensions are exactly 400x400 and return the BufferedImage.
     *
     * Traces: NFRA00039
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
     *
     * Traces: NFRA00057
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
            var resource = getClass().getClassLoader().getResourceAsStream("static/images/placeholder-400x400.png");
            if (resource != null) {
                return resource.readAllBytes();
            }
            return new byte[0];
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
