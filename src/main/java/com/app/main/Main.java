package com.app.main;

import com.app.event.EventImageView;
import com.app.event.PublicEvent;
import com.app.swing.ComponentResizer;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import lombok.Getter;

public class Main extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());
    
    private static Main instance;

    public Main() {
        this.instance = this;
        initComponents();
        init();
    }

    private void init() {
        java.net.URL imgURL = getClass().getResource("/com/app/icon/icon.png");
        if (imgURL != null) {
            setIconImage(new ImageIcon(imgURL).getImage());
        }
        ComponentResizer com = new ComponentResizer();
        com.registerComponent(this);
//        com.setMinimumSize(new Dimension(900, 500));
        com.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        com.setSnapSize(new Dimension(10, 10));
        view_Image.setVisible(false);
        login.setVisible(true);
        initEvent();
    }
    
    private void initEvent() {
        PublicEvent.getInstance().addEventImageView(new EventImageView() {
            @Override
            public void viewImage(Icon image) {
                view_Image.viewImage(image);
            }

            @Override
            public void saveImage(Icon image) {
                System.out.println("11111");
            }
        });
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    public void showHome() {
        login.setVisible(false);
        home.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        border = new javax.swing.JPanel();
        background = new javax.swing.JPanel();
        body = new javax.swing.JLayeredPane();
        view_Image = new com.app.form.View_Image();
        home = new com.app.form.Home();
        login = new com.app.form.Login();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        border.setBackground(new java.awt.Color(255, 255, 255));

        background.setBackground(new java.awt.Color(255, 255, 255));

        body.setLayout(new java.awt.CardLayout());
        body.setLayer(view_Image, javax.swing.JLayeredPane.POPUP_LAYER);
        body.add(view_Image, "card3");
        body.add(home, "card2");
        body.add(login, "card4");

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE)
                .addContainerGap())
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(body)
                .addContainerGap())
        );

        javax.swing.GroupLayout borderLayout = new javax.swing.GroupLayout(border);
        border.setLayout(borderLayout);
        borderLayout.setHorizontalGroup(
            borderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        borderLayout.setVerticalGroup(
            borderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, borderLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(border, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(border, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        try {
            // Thiết lập FlatLaf Look and Feel
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to set FlatLaf Look and Feel", ex);
            // Fallback về Nimbus nếu FlatLaf thất bại
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException fallbackEx) {
                logger.log(java.util.logging.Level.SEVERE, "Failed to set Nimbus Look and Feel", fallbackEx);
            }
        }

        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JLayeredPane body;
    private javax.swing.JPanel border;
    private com.app.form.Home home;
    private com.app.form.Login login;
    private com.app.form.View_Image view_Image;
    // End of variables declaration//GEN-END:variables
}
