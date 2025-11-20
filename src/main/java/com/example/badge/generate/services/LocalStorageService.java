package com.example.badge.generate.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Service
public class LocalStorageService implements StorageService {

    private final Path baseDir;

    public LocalStorageService(@Value("${storage.local.base-dir:uploads}") String baseDir) {
        this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file, String subfolder, String filename) throws IOException {
        Path folder = baseDir;
        if (subfolder != null && !subfolder.isBlank()) {
            folder = baseDir.resolve(subfolder);
            Files.createDirectories(folder);
        }
        Path target = folder.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        // Retorne path relativo
        return baseDir.relativize(target).toString().replace("\\", "/");
    }

    @Override
    public String store(InputStream inputStream, String subfolder, String filename) throws IOException {
        Path folder = baseDir;
        if (subfolder != null && !subfolder.isBlank()) {
            folder = baseDir.resolve(subfolder);
            Files.createDirectories(folder);
        }
        Path target = folder.resolve(filename);
        Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        return baseDir.relativize(target).toString().replace("\\", "/");
    }

    @Override
    public void delete(String path) throws IOException {
        Path target = baseDir.resolve(path).normalize();
        Files.deleteIfExists(target);
    }
}
