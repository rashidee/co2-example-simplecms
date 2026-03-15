package com.simplecms.adminportal.shared.vite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Service
public class ViteManifestService {

    private static final Logger log = LoggerFactory.getLogger(ViteManifestService.class);

    private final ObjectMapper objectMapper;
    private final boolean developmentMode;
    private Map<String, ViteManifestEntry> manifest = Collections.emptyMap();

    public ViteManifestService(ObjectMapper objectMapper,
                               @Value("${gg.jte.development-mode:false}") boolean developmentMode) {
        this.objectMapper = objectMapper;
        this.developmentMode = developmentMode;
    }

    @PostConstruct
    void loadManifest() {
        if (developmentMode) {
            log.info("Vite development mode active — manifest not loaded");
            return;
        }
        try {
            ClassPathResource resource = new ClassPathResource("static/.vite/manifest.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    manifest = objectMapper.readValue(is,
                            new TypeReference<Map<String, ViteManifestEntry>>() {});
                    log.info("Loaded Vite manifest with {} entries", manifest.size());
                }
            } else {
                log.warn("Vite manifest.json not found — assets may not resolve correctly");
            }
        } catch (IOException e) {
            log.error("Failed to load Vite manifest.json", e);
        }
    }

    public String resolve(String entryName) {
        if (developmentMode) {
            return "http://localhost:5173/" + entryName;
        }
        ViteManifestEntry entry = manifest.get(entryName);
        if (entry == null) {
            log.warn("Vite manifest entry not found for: {}", entryName);
            return "/" + entryName;
        }
        return "/" + entry.file();
    }

    public String[] resolveCss(String entryName) {
        if (developmentMode) {
            return new String[0];
        }
        ViteManifestEntry entry = manifest.get(entryName);
        if (entry == null || entry.css() == null) {
            return new String[0];
        }
        return entry.css().stream()
                .map(css -> "/" + css)
                .toArray(String[]::new);
    }

    public record ViteManifestEntry(
        String file,
        String src,
        boolean isEntry,
        java.util.List<String> css
    ) {}
}
