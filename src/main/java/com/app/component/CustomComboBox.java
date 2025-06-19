/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComboBox;


/**
 *
 * @author ngoan
 */
public class CustomComboBox<E> extends JComboBox<E> {
    private String hint;
    private Color hintColor;
    private Font fontFamily;
    
    
    

    public CustomComboBox() {
        setBorder();
        setFontFamily(new java.awt.Font("sansserif", 0, 12));
        setHintColor(Color.GRAY);
        
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
    

    private void setBorder() {
        setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if(getSelectedItem() == null && hint != null){
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            g.setColor(hintColor);
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }


    public Color getHintColor() {
        return hintColor;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
    }

    public Font getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(Font fontFamily) {
        this.fontFamily = fontFamily;
    }
    
    
}
