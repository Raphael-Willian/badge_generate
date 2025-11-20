package com.example.badge.generate.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {
    // salva o arquivo e retorna a URL ou path pública
    String store(MultipartFile file, String subfolder, String filename) throws Exception;

    // alternativa para InputStream
    String store(InputStream inputStream, String subfolder, String filename) throws Exception;

    // delete
    void delete(String path) throws Exception;
}