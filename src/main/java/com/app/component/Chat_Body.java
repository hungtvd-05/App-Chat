package com.app.component;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import net.miginfocom.swing.MigLayout;

public class Chat_Body extends javax.swing.JPanel {

    public Chat_Body() {
        initComponents();
        init();
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemLeft("Hellllllloooo", "hahahahahah");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemLeft("Hellllllloooo", "hahahahahah");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemLeft("Hellllllloooo", "hahahahahah");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemLeft("Hellllllloooo", "hahahahahah");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemLeft("Hellllllloooo", "hahahahahah");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...", new ImageIcon(getClass().getResource("/com/app/icon/group.png")));
        addItemLeft("Hellllllloooo", "hahahahahah");
        addDate("20/05/2025");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...");
        addItemRight("I'm having issue with the wrapping.... When i get to 80% of the witdthm the rest it just doesnt show it...", new ImageIcon(getClass().getResource("/com/app/icon/marvel.png")));
        addItemLeft("", "tao", new ImageIcon(getClass().getResource("/com/app/icon/group.png")), new ImageIcon(getClass().getResource("/com/app/icon/group.png")));
        
    }
    
    private void init() {
        body.setLayout(new MigLayout("fillx"));
        sp.setVerticalScrollBar(new JScrollBar());
        sp.getVerticalScrollBar().setBackground(Color.WHITE);
    }
    
    public void addItemLeft(String text, String user, Icon... image) {
        Chat_Left_With_Profile item = new Chat_Left_With_Profile();
        item.setText(text);
        item.setImage(image);
        item.setTime();
        item.setUserProfile(user);
        body.add(item, "wrap, w 100::80%");
        body.repaint();
        body.revalidate();
    }
    
    public void addItemRight(String text, Icon... image) {
        Chat_Right item = new Chat_Right();
        item.setText(text);
        item.setImage(image);
        body.add(item, "wrap, al right, w 100::80%");
        body.repaint();
        body.revalidate();
    }
    
    public void addDate(String date) {
        Chat_Date item = new Chat_Date();
        item.setDate(date);
        body.add(item, "wrap, al center");
        body.repaint();
        body.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        body = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        sp.setBackground(new java.awt.Color(255, 255, 255));
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        body.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );

        sp.setViewportView(body);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
