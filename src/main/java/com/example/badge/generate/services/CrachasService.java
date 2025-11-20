package com.example.badge.generate.services;

import com.example.badge.generate.models.Crachas;
import com.example.badge.generate.models.Templates;
import com.example.badge.generate.repositorys.CrachasRepository;
import com.example.badge.generate.repositorys.TemplatesRepository;
import com.example.badge.generate.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.Optional;

@Service
public class CrachasService {

    private final CrachasRepository badgeRepository;
    private final TemplatesRepository templateRepository;
    private final StorageService storageService;

    public CrachasService(CrachasRepository badgeRepository,
                        TemplatesRepository templateRepository,
                        StorageService storageService) {
        this.badgeRepository = badgeRepository;
        this.templateRepository = templateRepository;
        this.storageService = storageService;
    }

    /**
     * Cria o crachá final combinando: template background + foto (redimensionada) + texto
     * Retorna a URL/path da imagem final
     */
    public Crachas createBadge(Long employeeId, String employeeName, String employeeRole, Long templateId, MultipartFile photo) throws Exception {
        Optional<Templates> ot = templateRepository.findById(templateId);
        if (ot.isEmpty()) {
            throw new RuntimeException("Template not found");
        }
        Templates template = ot.get();

        // 1) carregar imagem de fundo
        InputStream bgInput;
        if (template.getBackgroundImageUrl().startsWith("http")) {
            // se for S3/public URL -> tentar abrir stream via URL (opcional)
            bgInput = new java.net.URL(template.getBackgroundImageUrl()).openStream();
        } else {
            // caminho relativo local uploads: baseDir/<path>
            // Para simplificar: StorageService não expõe base dir, então assumimos local e reconstruímos path.
            // Se usar S3, backgroundImageUrl deve ser URL pública.
            bgInput = new FileInputStream(template.getBackgroundImageUrl());
        }
        BufferedImage background = ImageIO.read(bgInput);

        // 2) carregar foto do colaborador
        BufferedImage photoImg = ImageIO.read(photo.getInputStream());
        // redimensionar
        int targetW = template.getPhotoWidth() != null ? template.getPhotoWidth() : 120;
        int targetH = template.getPhotoHeight() != null ? template.getPhotoHeight() : 120;
        BufferedImage resizedPhoto = resize(photoImg, targetW, targetH);

        // 3) compor
        BufferedImage combined = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        // melhorar qualidade
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(background, 0, 0, null);

        // desenhar foto
        int px = template.getPhotoX() != null ? template.getPhotoX() : 10;
        int py = template.getPhotoY() != null ? template.getPhotoY() : 10;
        g.drawImage(resizedPhoto, px, py, null);

        // desenhar textos: nome e role
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // fonte
        Font nameFont = new Font("Arial", Font.BOLD, 24);
        Font roleFont = new Font("Arial", Font.PLAIN, 16);

        // cor: usando preto; customize se quiser guardar cor no template
        g.setColor(Color.BLACK);

        int nx = template.getNameX() != null ? template.getNameX() : px + targetW + 10;
        int ny = template.getNameY() != null ? template.getNameY() : py + 30;
        g.setFont(nameFont);
        drawStringWrapped(g, employeeName, nx, ny);

        int rx = template.getRoleX() != null ? template.getRoleX() : nx;
        int ry = template.getRoleY() != null ? template.getRoleY() : ny + 30;
        g.setFont(roleFont);
        drawStringWrapped(g, employeeRole, rx, ry);

        g.dispose();

        // 4) salvar em storage
        String filename = "badge_" + System.currentTimeMillis() + ".png";
        // armazenar combined em byte[] ou InputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(combined, "png", baos);
        InputStream finalImageStream = new ByteArrayInputStream(baos.toByteArray());

        String storedPath = storageService.store(finalImageStream, "badges", filename);

        // 5) persistir Badge
        Crachas badge = new Crachas();
        badge.setEmployeeId(employeeId);
        badge.setEmployeeName(employeeName);
        badge.setEmployeeRole(employeeRole);
        badge.setTemplate(template);
        badge.setFinalBadgeImageUrl(storedPath);
        Crachas saved = badgeRepository.save(badge);

        return saved;
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    private static void drawStringWrapped(Graphics2D g, String text, int x, int y) {
        // simples: desenha uma linha; se precisar de wrap mais complexo, implementar
        g.drawString(text, x, y);
    }
}

