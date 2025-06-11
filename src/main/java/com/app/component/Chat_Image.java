package com.app.component;

import com.app.event.PublicEvent;
import com.app.model.Model_File_Sender;
import com.app.swing.PictureBox;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class Chat_Image extends javax.swing.JPanel {

    public Chat_Image(boolean right) {
        initComponents();
//        setOpaque(true);
        setLayout(new MigLayout("", "0[" + (right ? "right" : "left") + "]0", "3[]3"));
    }

    public void addImage(Model_File_Sender fileSender) {
        Icon image = new ImageIcon(fileSender.getFile().getAbsolutePath());
        Image_Item pic = new Image_Item();
        pic.setPreferredSize(getAutoSize(image, 200, 200));
        pic.setImage(image, fileSender);
        addEvent(pic, image);
        add(pic, "wrap");
    }
    
    public void addImage(String... images) {
        for (String image : images) {
            Image_Item pic = new Image_Item();
            pic.setPreferredSize(new Dimension(200, 200));
            pic.setImage(image);
//            addEvent(pic, image);
            add(pic, "wrap");
        }
    }
    
    private void addEvent(Component com, Icon image) {
        com.setCursor(new Cursor(Cursor.HAND_CURSOR));
        com.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    PublicEvent.getInstance().getEventImageView().viewImage(image);
                }
            }
        });
    }

    private Dimension getAutoSize(Icon image, int w, int h) {
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        return new Dimension(width, height);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
