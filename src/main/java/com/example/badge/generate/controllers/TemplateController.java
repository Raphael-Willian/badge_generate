package com.example.badge.generate.controllers;

import com.example.badge.generate.records.responses.TemplatesResponseDTO;
import com.example.badge.generate.services.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<TemplatesResponseDTO> createTemplate(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "nameX", required = false) Integer nameX,
            @RequestParam(value = "nameY", required = false) Integer nameY,
            @RequestParam(value = "roleX", required = false) Integer roleX,
            @RequestParam(value = "roleY", required = false) Integer roleY,
            @RequestParam(value = "photoX", required = false) Integer photoX,
            @RequestParam(value = "photoY", required = false) Integer photoY,
            @RequestParam(value = "photoWidth", required = false) Integer photoWidth,
            @RequestParam(value = "photoHeight", required = false) Integer photoHeight
    ) throws Exception {
        TemplatesResponseDTO dto = templateService.createTemplate(name, file, nameX, nameY, roleX, roleY, photoX, photoY, photoWidth, photoHeight);
        return ResponseEntity.ok(dto);
    }
}
