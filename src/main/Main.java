/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

/**
 *
 * @author gda
 */
public class Main {
    
    private static final CardStencil[] CARD_STENCILS = new CardStencil[5];
    private static final int CARD_STEP = 72;
    private static final CardStencil CARD_STENCIL_INITIAL = new CardStencil(
            new Rectangle(146, 588, 33, 30),
            new Rectangle(166, 632, 36, 36)
    );
    private static final Dictionary TEXT_DICTIONARY = new Dictionary();
    private static final Dictionary SUIT_DICTIONARY = new Dictionary();
    private static final File TEXT_DICTIONARY_DIR = new File("text_d");
    private static final File SUIT_DICTIONARY_DIR = new File("suit_d");
    
    static {
        TEXT_DICTIONARY_DIR.mkdirs();
        SUIT_DICTIONARY_DIR.mkdirs();
        for (int i = 0; i < CARD_STENCILS.length; i++) {
            CARD_STENCILS[i] = new CardStencil(
                    new Rectangle(
                            CARD_STENCIL_INITIAL.getText().x + CARD_STEP * i,
                            CARD_STENCIL_INITIAL.getText().y,
                            CARD_STENCIL_INITIAL.getText().width,
                            CARD_STENCIL_INITIAL.getText().height
                    ),
                    new Rectangle(
                            CARD_STENCIL_INITIAL.getSuit().x + CARD_STEP * i,
                            CARD_STENCIL_INITIAL.getSuit().y,
                            CARD_STENCIL_INITIAL.getSuit().width,
                            CARD_STENCIL_INITIAL.getSuit().height
                    )
            );
        }
    }
    
    public static void main(String[] args) throws Exception {

//        train("imgs");
//        saveDictionaries();
        // После сохранения проставляем вручную названия файлов.
        loadDictionaries();
        System.out.println(args[0]);
        recognize(args[0]); // Распознать всю папку.
//        recognizeImage(""); // Распознать файл.
    }
    
    private static void recognize(String imagesDirPath) throws IOException {
        for (final File fileEntry : new File(imagesDirPath).listFiles()) {
            if (fileEntry.isFile()) {
                recognizeImage(fileEntry.getAbsolutePath());
            }
        }
    }
    
    private static void recognizeImage(String imagePath) throws IOException {
        System.out.println(imagePath);
        BufferedImage image = ImageIO.read(new File(imagePath));
        for (int i = 0; i < CARD_STENCILS.length; i++) {
            CardStencil CARD_STENCIL = CARD_STENCILS[i];
            BufferedImage textImage = image.getSubimage(
                    CARD_STENCIL.getText().x,
                    CARD_STENCIL.getText().y,
                    CARD_STENCIL.getText().width,
                    CARD_STENCIL.getText().height
            );
            String textString = TEXT_DICTIONARY.find(textImage);
            
            BufferedImage suitImage = image.getSubimage(
                    CARD_STENCIL.getSuit().x,
                    CARD_STENCIL.getSuit().y,
                    CARD_STENCIL.getSuit().width,
                    CARD_STENCIL.getSuit().height
            );
            String suitString = SUIT_DICTIONARY.find(suitImage);
            
            System.out.println(i + ": " + textString + "/" + suitString);
        }
    }
    
    private static void train(String imagesDirPath) throws IOException {
        for (final File fileEntry : new File(imagesDirPath).listFiles()) {
            if (fileEntry.isFile()) {
                BufferedImage image = ImageIO.read(fileEntry);
                for (CardStencil CARD_STENCIL : CARD_STENCILS) {
                    BufferedImage textImage = image.getSubimage(
                            CARD_STENCIL.getText().x,
                            CARD_STENCIL.getText().y,
                            CARD_STENCIL.getText().width,
                            CARD_STENCIL.getText().height
                    );
                    TEXT_DICTIONARY.include(String.valueOf(TEXT_DICTIONARY.getImages().size()), textImage);
                    
                    BufferedImage suitImage = image.getSubimage(
                            CARD_STENCIL.getSuit().x,
                            CARD_STENCIL.getSuit().y,
                            CARD_STENCIL.getSuit().width,
                            CARD_STENCIL.getSuit().height
                    );
                    SUIT_DICTIONARY.include(String.valueOf(SUIT_DICTIONARY.getImages().size()), suitImage);
                }
            }
        }
    }
    
    private static void loadDictionaries() throws IOException {
        DictionaryIO.load(TEXT_DICTIONARY, TEXT_DICTIONARY_DIR);
        DictionaryIO.load(SUIT_DICTIONARY, SUIT_DICTIONARY_DIR);
    }
    
    private static void saveDictionaries() throws IOException {
        DictionaryIO.save(TEXT_DICTIONARY, TEXT_DICTIONARY_DIR);
        DictionaryIO.save(SUIT_DICTIONARY, SUIT_DICTIONARY_DIR);
    }
    
}
