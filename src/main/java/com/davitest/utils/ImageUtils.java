package com.davitest.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    private ImageUtils() {}

    public static String createTestProfileImage() {
        String dir  = "src/test/resources/images";
        String path = dir + "/test-profile.png";

        new File(dir).mkdirs();
        File imgFile = new File(path);

        // Si ya existe, reutilizarla
        if (imgFile.exists()) {
            return imgFile.getAbsolutePath();
        }

        try {
            BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();

            // Fondo azul corporativo
            g2d.setColor(new Color(0, 102, 204));
            g2d.fillRect(0, 0, 200, 200);

            // Texto blanco
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("QA Test", 55, 100);
            g2d.drawString("Photo", 70, 130);
            g2d.dispose();

            ImageIO.write(img, "png", imgFile);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la imagen de prueba: " + e.getMessage(), e);
        }

        return imgFile.getAbsolutePath();
    }
}
