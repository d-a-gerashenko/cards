/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gda
 */
public class Dictionary {

    private final HashMap<String, BufferedImage> images = new HashMap<>();

    public HashMap<String, BufferedImage> getImages() {
        return images;
    }

    public boolean include(String label, BufferedImage image) {
        if (find(image) == null) {
            images.put(label, normalize(image));
            return true;
        }
        return false;
    }

    public String find(BufferedImage imageToFind) {
        for (Map.Entry<String, BufferedImage> entry : images.entrySet()) {
            if (compare(normalize(imageToFind), entry.getValue()) > 0.93) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param img1
     * @param img2
     * @return Коэффициент похожести. 0 - разные, 1 - одинаковые.
     */
    public static double compare(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth(null);
        int width2 = img2.getWidth(null);
        int height1 = img1.getHeight(null);
        int height2 = img2.getHeight(null);
        if ((width1 != width2) || (height1 != height2)) {
            return 0;
        }
        long diff = 0;
        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = (rgb1) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = (rgb2) & 0xff;
                diff += Math.abs(r1 - r2);
                diff += Math.abs(g1 - g2);
                diff += Math.abs(b1 - b2);
            }
        }
        double n = width1 * height1 * 3;
        return 1 - (diff / 255.0) / n;
    }

    public BufferedImage normalize(BufferedImage image) {
        return aligneFilter(binaryFilter(image));
    }

    private static BufferedImage binaryFilter(BufferedImage image) {
        BufferedImage outImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY
        );
        Graphics g = outImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return outImage;
    }

    private static BufferedImage aligneFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int top = height / 2;
        int bottom = top;
        int left = width / 2;
        int right = left;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) != Color.WHITE.getRGB()) {
                    top = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                    left = Math.min(left, x);
                    right = Math.max(right, x);
                }
            }
        }

        BufferedImage outImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY
        );
        Graphics g = outImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
        g.drawImage(image.getSubimage(left, top, right - left, bottom - top), 0, 0, null);
        g.dispose();

        return outImage;
    }
}
