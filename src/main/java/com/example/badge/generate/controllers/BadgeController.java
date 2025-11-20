package com.example.badge.generate.controllers;


import com.example.badge.generate.records.responses.CrachasResponseDTO;
import com.example.badge.generate.models.Crachas;
import com.example.badge.generate.services.CrachasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final CrachasService badgeService;

    public BadgeController(CrachasService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping
    public ResponseEntity<CrachasResponseDTO> createBadge(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("employeeName") String employeeName,
            @RequestParam("employeeRole") String employeeRole,
            @RequestParam("templateId") Long templateId,
            @RequestParam("photo") MultipartFile photo
    ) throws Exception {
        Crachas saved = badgeService.createBadge(employeeId, employeeName, employeeRole, templateId, photo);
        CrachasResponseDTO dto = new CrachasResponseDTO();
        dto.setId(saved.getId());
        dto.setFinalBadgeImageUrl(saved.getFinalBadgeImageUrl());
        return ResponseEntity.ok(dto);
    }
}

