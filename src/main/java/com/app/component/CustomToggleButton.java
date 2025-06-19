/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;


public class CustomToggleButton extends JToggleButton {
    
    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private Color foregroundColor;
    private Color foregroundOver;
    private Color foregroundClick;
    private int radius = 0;
    private int borderSize = 1;
    private boolean toggled = false;
    

    public CustomToggleButton() {
        setColor(Color.WHITE);
        colorOver = Color.LIGHT_GRAY;
        colorClick = Color.DARK_GRAY; 
        borderColor = Color.BLACK;
        foregroundColor = Color.BLACK;
        foregroundOver = Color.BLACK;
        foregroundClick = Color.WHITE;
        setContentAreaFilled(false);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if(!toggled) {
                    setBackground(colorOver);
                    setForeground(foregroundOver);
                }
                over = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(!toggled) {
                    setBackground(color);
                    setForeground(foregroundColor);
                }
                over = false;
            }

            @Override
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
                setForeground(foregroundClick);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                toggled = !toggled;  // Toggle status change
                if(toggled) {
                    setBackground(colorClick);
                    setForeground(foregroundClick);
                } else {
                    setBackground(color);
                    setForeground(foregroundColor);
                }
                superMouseClicked();  // Call custom method for additional actions
            }
        });
    }
    
    private void superMouseClicked() {
        // Custom actions to be performed on mouse click, if any
    }

    @Override
    public void setSelected(boolean b) {
        if (b != isSelected()) {
            mouseReleased();
            super.setSelected(b);
        }
    }
    
    
    
    public void mouseReleased() {
                toggled = !toggled;  // Toggle status change
                if(toggled) {
                    setBackground(colorClick);
                    setForeground(foregroundClick);
                } else {
                    setBackground(color);
                    setForeground(foregroundColor);
                }
                superMouseClicked();  // Call custom method for additional actions
            }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(borderSize, borderSize, getWidth()-borderSize*2, getHeight()-borderSize*2, radius, radius);
        super.paintComponent(g);
    }
    

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColorOver() {
        return colorOver;
    }

    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
    }

    public Color getColorClick() {
        return colorClick;
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getForegroundOver() {
        return foregroundOver;
    }

    public void setForegroundOver(Color foregroundOver) {
        this.foregroundOver = foregroundOver;
    }

    public Color getForegroundClick() {
        return foregroundClick;
    }

    public void setForegroundClick(Color foregroundClick) {
        this.foregroundClick = foregroundClick;
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

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
    
    
}
