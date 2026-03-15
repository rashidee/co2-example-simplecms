package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * Traces: USA000072-081, NFRA00081-093, CONSA0024
 */
@Service
@Transactional
class TeamMemberServiceImpl implements TeamMemberService {

    private static final Logger log = LoggerFactory.getLogger(TeamMemberServiceImpl.class);
    private static final int IMAGE_SIZE = 400;

    private final TeamMemberRepository repository;
    private final TeamMemberMapper mapper;

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    TeamMemberServiceImpl(TeamMemberRepository repository, TeamMemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamMemberDTO> list(TeamMemberStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
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
                                int displayOrder, TeamMemberStatus status,
                                MultipartFile profilePicture) {
        validateImage(profilePicture);
        String picturePath = saveImage(profilePicture);

        TeamMemberEntity entity = new TeamMemberEntity();
        entity.setProfilePicturePath(picturePath);
        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        TeamMemberEntity saved = repository.save(entity);
        log.info("Team member created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public TeamMemberDTO update(UUID id, String name, String role, String linkedinUrl,
                                int displayOrder, TeamMemberStatus status,
                                MultipartFile profilePicture) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));

        if (profilePicture != null && !profilePicture.isEmpty()) {
            validateImage(profilePicture);
            String picturePath = saveImage(profilePicture);
            entity.setProfilePicturePath(picturePath);
        }

        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
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

    private String saveImage(MultipartFile image) {
        try {
            String filename = "team-" + UUID.randomUUID() + getExtension(image.getOriginalFilename());
            Path dir = Paths.get(uploadPath, "team-section");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/team-section/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return ".png";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".png";
    }
}
