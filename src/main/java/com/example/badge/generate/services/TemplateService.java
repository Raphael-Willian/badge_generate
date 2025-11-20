package com.example.badge.generate.services;

import com.example.badge.generate.records.responses.TemplatesResponseDTO;
import com.example.badge.generate.models.Templates;
import com.example.badge.generate.repositorys.TemplatesRepository;
import com.example.badge.generate.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplatesRepository templateRepository;
    private final StorageService storageService;

    public TemplateService(TemplatesRepository templateRepository, StorageService storageService) {
        this.templateRepository = templateRepository;
        this.storageService = storageService;
    }

    public TemplatesResponseDTO createTemplate(String name,
                                                    MultipartFile file,
                                                    Integer nameX, Integer nameY,
                                                    Integer roleX, Integer roleY,
                                                    Integer photoX, Integer photoY,
                                                    Integer photoWidth, Integer photoHeight) throws Exception {

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String storedPath = storageService.store(file, "templates", filename);

        Templates template = new Templates();
        template.setName(name);
        template.setBackgroundImageUrl(storedPath);
        template.setNameX(nameX);
        template.setNameY(nameY);
        template.setRoleX(roleX);
        template.setRoleY(roleY);
        template.setPhotoX(photoX);
        template.setPhotoY(photoY);
        template.setPhotoWidth(photoWidth);
        template.setPhotoHeight(photoHeight);
        Templates saved = templateRepository.save(template);

        TemplatesResponseDTO dto = new TemplatesResponseDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setBackgroundImageUrl(saved.getBackgroundImageUrl());
        return dto;
    }

    public Optional<Templates> findById(Long id) {
        return templateRepository.findById(id);
    }
}