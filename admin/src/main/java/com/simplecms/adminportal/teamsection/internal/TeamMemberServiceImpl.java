package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * Implementation of TeamMemberService.
 * v1.0.4: BLOB image storage, image validation.
 *
 * Traces: USA000072-081, NFRA00081-093, CONSA0024
 */
@Service
@Transactional
class TeamMemberServiceImpl implements TeamMemberService {

    private static final Logger log = LoggerFactory.getLogger(TeamMemberServiceImpl.class);
    private static final int IMAGE_SIZE = 400;

    private final TeamMemberRepository repository;
    private final TeamMemberMapper mapper;

    TeamMemberServiceImpl(TeamMemberRepository repository, TeamMemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamMemberDTO> list(Pageable pageable) {
        return repository.findWithFilters(pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMemberDTO getById(UUID id) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public TeamMemberDTO create(String name, String role, String linkedinUrl,
                                int displayOrder,
                                MultipartFile profilePicture) {
        validateImage(profilePicture);
        byte[] imageData = toBytes(profilePicture);

        TeamMemberEntity entity = new TeamMemberEntity();
        entity.setProfilePictureData(imageData);
        entity.setProfilePicturePath(null);
        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setCreatedBy("EDITOR");

        TeamMemberEntity saved = repository.save(entity);
        log.info("Team member created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public TeamMemberDTO update(UUID id, String name, String role, String linkedinUrl,
                                int displayOrder,
                                MultipartFile profilePicture) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));

        if (profilePicture != null && !profilePicture.isEmpty()) {
            validateImage(profilePicture);
            entity.setProfilePictureData(toBytes(profilePicture));
            entity.setProfilePicturePath(null);
        }

        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setUpdatedBy("EDITOR");

        TeamMemberEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));
        repository.delete(entity);
        log.info("Team member deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageData(UUID id) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));
        if (entity.getProfilePictureData() != null) {
            return entity.getProfilePictureData();
        }
        return getPlaceholderImage();
    }

    /**
     * Validate image dimensions are exactly 400x400 square.
     *
     * Traces: NFRA00081
     */
    private void validateImage(MultipartFile image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image file");
            }
            if (bufferedImage.getWidth() != IMAGE_SIZE || bufferedImage.getHeight() != IMAGE_SIZE) {
                throw new IllegalArgumentException(
                    String.format("Profile picture must be %dx%d pixels. Got %dx%d.",
                        IMAGE_SIZE, IMAGE_SIZE,
                        bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
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
