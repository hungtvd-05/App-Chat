package com.app.component;

import com.app.model.Model_File_Sender;
import com.app.model.Model_Image;
import com.app.util.Utils;
import java.awt.Color;
import java.time.LocalDateTime;
import javax.swing.Icon;

public class Chat_Left extends javax.swing.JLayeredPane {

    public Chat_Left() {
        initComponents();
        txt.setBackground(new Color(242, 242, 242));
    }
    
    public void setText(String text) {
        if (text.equals("")) {
            txt.hideText();
        } else {
            txt.setText(text);
        }
    }
       
    public void setImage(Model_File_Sender fileSender) {
        System.out.println("ham nay duoc goi de set anh 1");
        txt.setImage(false, fileSender);
    }
    
    public void setImage(String path) {
        System.out.println("ham nay duoc goi de set anh");
        txt.setImage(false, path);
    }
    
    public void setImage(Model_Image dataImage) {
        System.out.println("ham nay duoc goi de set anh 2");
        txt.setImage(false, dataImage);
    }
    
    public void setFile(String fileName, String fileSize) {
        txt.setFile(fileName, fileSize);
    }
    
    public void setEmoji(Icon icon) {
        txt.hideText();
        txt.setEmoji(false, icon);
    }

    public void setTime(LocalDateTime time) {
        txt.setTime(Utils.formatTime(time));    
        
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt = new com.app.component.Chat_Item();

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.app.component.Chat_Item txt;
    // End of variables declaration//GEN-END:variables
}
