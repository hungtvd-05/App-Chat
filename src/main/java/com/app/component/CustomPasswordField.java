/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author LENOVO
 */

@Setter
@Getter
public class CustomPasswordField extends JPasswordField{

    private final Image eye;
    private final Image eye_hide;
    private boolean hide = true;
    private Color hintColor = new Color(200, 200, 200);
    private Color color = new Color(230, 245, 241);
    private Color borderColor = Color.GRAY;
    private int radius = 0;
    
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder();
    }

    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";
    
    private void createShowHide(Graphics2D g2) {
        int x = getWidth() - 40 + 5;
        int y = (getHeight() - 30)/2;
        g2.drawImage(hide?eye_hide:eye, x, y, null);
    }

    public CustomPasswordField() {
        eye = new ImageIcon(getClass().getResource("/com/app/icon/login/icon_eye_30px.png")).getImage();
        eye_hide = new ImageIcon(getClass().getResource("/com/app/icon/login/icon_eye-hide_30px.png")).getImage();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = getWidth() - 35;
                if (new Rectangle(x, 0 ,30,30).contains(e.getPoint())) {
                    hide = !hide;
                }
                if (hide) {
                    setEchoChar('•');
                } else {
                    setEchoChar((char)0);
                }
                repaint();
            }
            
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = getWidth() - 35;
                if (new Rectangle(x, 0 ,30,30).contains(e.getPoint())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }     
        });
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(Color.decode("#7A8C8D"));
        setFont(new java.awt.Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 152));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(color);
        g2.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, radius, radius);
        paintIcon(g);
        super.paintComponent(g);
        
        createShowHide(g2);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getPassword().length == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            g.setColor(hintColor);
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
        
    }

    private void paintIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            g2.drawImage(prefix, 10, y, this);
        }
        if (suffixIcon != null) {
            Image suffix = ((ImageIcon) suffixIcon).getImage();
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            g2.drawImage(suffix, getWidth() - suffixIcon.getIconWidth() - 10, y, this);
        }
    }

    public void initBorder() {
        int left = 30;
        int right = 30;
        if (prefixIcon != null) {
            left = prefixIcon.getIconWidth() + 15;
        }
        if (suffixIcon != null) {
            right = suffixIcon.getIconWidth() + 15;
        }
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, left, 10, right));
    }
    
    public void initBorderWithColor(Color borderColor) {
        int top = 10, left = 10, bottom = 10, right = 10;

        if (prefixIcon != null) {
            left += prefixIcon.getIconWidth() + 5;
        }
        if (suffixIcon != null) {
            right += suffixIcon.getIconWidth() + 5;
        }

        Border emptyBorder = new EmptyBorder(top, left, bottom, right);
        Border lineBorder = new LineBorder(borderColor, 1);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        setBorder(compoundBorder);
    }
    
    
    
}
