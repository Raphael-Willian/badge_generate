package com.example.badge.generate.records.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CrachasRequestDTO {

    private Long employeeId;
    private String employeeName;
    private String employeeRole;
    private Long templateId;
    private MultipartFile photo;
}
