/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Rectangle;

/**
 *
 * @author gda
 */
public class CardStencil {

    private final Rectangle text;
    private final Rectangle suit;

    public CardStencil(Rectangle text, Rectangle suit) {
        this.text = text;
        this.suit = suit;
    }

    public Rectangle getText() {
        return text;
    }

    public Rectangle getSuit() {
        return suit;
    }

}
