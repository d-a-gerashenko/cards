/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author gda
 */
public class DictionaryIO {

    private DictionaryIO() {

    }

    public static void load(Dictionary dictionary, File dictionaryDir) throws IOException {
        for (final File fileEntry : dictionaryDir.listFiles()) {
            if (fileEntry.isFile()) {
                BufferedImage image = ImageIO.read(fileEntry);
                dictionary.getImages().put(fileEntry.getName().replace(".bmp", ""), image);
            }
        }
    }

    public static void save(Dictionary dictionary, File dictionaryDir) throws IOException {
        for (final File fileEntry : dictionaryDir.listFiles()) {
            fileEntry.delete();
        }
        for (Map.Entry<String, BufferedImage> entry : dictionary.getImages().entrySet()) {
            ImageIO.write(entry.getValue(), "bmp", new File(dictionaryDir, entry.getKey() + ".bmp"));
        }

    }
}
