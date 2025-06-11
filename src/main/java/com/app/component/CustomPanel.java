/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.component;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author LENOVO
 */
public class CustomPanel extends JPanel{
    private Color color;
    private Color borderColor;
    private boolean gradient = false;
    private Color gradientColor1 ;
    private Color gradientColor2;
    int radius = 0;
    int borderSize = 1;
    
    private int gradient_x = 1;
    private int gradient_y = 1;
    
    public CustomPanel() {
        color = Color.WHITE;
        borderColor = Color.BLACK;
        if (gradientColor1 == null) {
            gradientColor1 = color;
        }
        if (gradientColor2 == null) {
            gradientColor2 = color;
        }

        
        setOpaque(false);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        GradientPaint colorGradient = new GradientPaint(0, 0, color, 0, 0, color);

        if (gradient) {
            colorGradient = new GradientPaint(0, 0, gradientColor1, gradient_x*getWidth(), gradient_y*getHeight(), gradientColor2);
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setPaint(colorGradient);
        g2.fillRoundRect(borderSize, borderSize, getWidth()-borderSize*2, getHeight()-borderSize*2, radius, radius);
         
        super.paintComponent(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }
    
    

    public int getGradient_x() {
        return gradient_x;
    }

    public void setGradient_x(int gradient_x) {
        if (gradient_x>1) {
            this.gradient_x = 1;
            return;
        }
        if (gradient_x<0) {
            this.gradient_x = 0;
            return;
        }
        this.gradient_x = gradient_x;
    }

    public int getGradient_y() {
        return gradient_y;
    }

    public void setGradient_y(int gradient_y) {
        if (gradient_y>1) {
            this.gradient_y = 1;
            return;
        }
        if (gradient_y<0) {
            this.gradient_y = 0;
            return;
        }
        this.gradient_y = gradient_y;
    }

    public boolean isGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getGradientColor1() {
        return gradientColor1;
    }

    public void setGradientColor1(Color gradientColor1) {
        this.gradientColor1 = gradientColor1;
    }

    public Color getGradientColor2() {
        return gradientColor2;
    }

    public void setGradientColor2(Color gradientColor2) {
        this.gradientColor2 = gradientColor2;
    }
    
    
}