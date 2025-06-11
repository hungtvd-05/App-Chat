package com.app.component;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JButton;

public class MenuButton extends JButton {
    private Icon iconSimple;
    private Icon iconSelected;

    public Icon getIconSimple() {
        return iconSimple;
    }

    public void setIconSimple(Icon iconSimple) {
        this.iconSimple = iconSimple;
    }

    public Icon getIconSelected() {
        return iconSelected;
    }

    public void setIconSelected(Icon iconSelected) {
        this.iconSelected = iconSelected;
    }
    
    public MenuButton() {
        setContentAreaFilled(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b); 
        if (b) {
            setIcon(iconSelected);
        } else {
            setIcon(iconSimple);
        }
    }
    
}
