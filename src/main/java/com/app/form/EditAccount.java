/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.app.form;

import com.app.component.glasspanepopup.GlassPanePopup;
import com.app.event.EventClick;
import com.app.event.PublicEvent;
import com.app.main.Main;
import com.app.model.Model_Update_User;
import com.app.model.UserAccount;
import com.app.service.Service;
import com.app.util.Utils;
import io.socket.client.Ack;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author LENOVO
 */
public class EditAccount extends javax.swing.JPanel {

    /**
     * Creates new form EditAccount
     */
    
    private String image = "";
    
    public EditAccount() {
        initComponents();
        init();
    }
    
    private void init( ) {
        clear();
        setDefault();
        enableChangePassword(false);
        setOpaque(false);
        cbGender.setLightWeightPopupEnabled(false);
        tfUsername.setEnabled(false);
        tfUsername.setColor(new Color(222, 222, 222));
        lbUsername.setForeground(new Color(128, 128, 128));
        imageAvatar.addEvent(new EventClick() {
            @Override
            public void onClick() {
                JFileChooser ch = new JFileChooser();
                ch.setMultiSelectionEnabled(false);
                ch.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.isDirectory() || Utils.isImageFile(file);
                    }

                    @Override
                    public String getDescription() {
                        return "Image File";
                    }
                });
                int option = ch.showOpenDialog(Main.getFrames()[0]);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = ch.getSelectedFile();
                    try {
                        byte[] imageBytes = Files.readAllBytes(file.toPath());

                        image = Base64.getEncoder().encodeToString(imageBytes);
                        
                        ImageIcon icon = new ImageIcon(imageBytes);

                        imageAvatar.setImage(icon);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error choose avatar");
                    }
                }
                
                
            }
        });
        
        cbChangePassword.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    
                    enableChangePassword(true);
                } else {
                    enableChangePassword(false);
                }
            }
        });
    }
    
    private void setDefault() {
        
        UserAccount user = Service.getInstance().getUserAccount();
        tfFullName.setText(user.getFullName());
        tfUsername.setText(user.getUserName());
        tfMail.setText(user.getMail());
        tfPhone.setText(user.getPhone());
        pfCurrentPassword.setText("");
        pfConfirmNewPassword.setText("");
        pfNewPassword.setText("");
        image = user.getImage();
        String gender = user.getGender();
        cbGender.setSelectedItem(gender);
        if (image.length() > 0) {
            imageAvatar.setImage(image);
        }
        
        
    }
    
    private void enableChangePassword(boolean isEnabled) {
        if (isEnabled) {
            lbCurrentPassword.setForeground(Color.BLACK);
            lbConfirmPassword.setForeground(Color.BLACK);
            lbNewPassword.setForeground(Color.BLACK);
            pfNewPassword.setColor(Color.WHITE);
            pfConfirmNewPassword.setColor(Color.WHITE);
            pfCurrentPassword.setColor(Color.WHITE);
        } else {
            lbCurrentPassword.setForeground(new Color(128, 128, 128));
            lbConfirmPassword.setForeground(new Color(128, 128, 128));
            lbNewPassword.setForeground(new Color(128, 128, 128));
            pfNewPassword.setColor(new Color(222, 222, 222));
            pfConfirmNewPassword.setColor(new Color(222, 222, 222));
            pfCurrentPassword.setColor(new Color(222, 222, 222));
        }
        pfCurrentPassword.setEnabled(isEnabled);
        pfConfirmNewPassword.setEnabled(isEnabled);
        pfNewPassword.setEnabled(isEnabled);
    }
    
    private void clear() {
        tfFullName.setText("");
        tfUsername.setText("");
        tfMail.setText("");
        tfPhone.setText("");
        pfCurrentPassword.setText("");
        pfConfirmNewPassword.setText("");
        pfNewPassword.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new com.app.component.CustomPanel();
        tfUsername = new com.app.component.CustomTextField();
        imageAvatar = new com.app.swing.ImageAvatar();
        lbFullName = new javax.swing.JLabel();
        tfFullName = new com.app.component.CustomTextField();
        lbMail = new javax.swing.JLabel();
        tfMail = new com.app.component.CustomTextField();
        lbPhone = new javax.swing.JLabel();
        tfPhone = new com.app.component.CustomTextField();
        cbGender = new com.app.component.CustomComboBox();
        lbGender = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lbUsername = new javax.swing.JLabel();
        pfCurrentPassword = new com.app.component.CustomPasswordField();
        lbCurrentPassword = new javax.swing.JLabel();
        cbChangePassword = new javax.swing.JCheckBox();
        lbNewPassword = new javax.swing.JLabel();
        pfNewPassword = new com.app.component.CustomPasswordField();
        pfConfirmNewPassword = new com.app.component.CustomPasswordField();
        lbConfirmPassword = new javax.swing.JLabel();
        updateButton = new com.app.component.CustomButton();
        lbMess = new javax.swing.JLabel();

        panel.setBorderColor(new java.awt.Color(204, 204, 204));
        panel.setColor(new java.awt.Color(244, 244, 244));
        panel.setRadius(10);

        tfUsername.setText("username");
        tfUsername.setColor(new java.awt.Color(255, 255, 255));
        tfUsername.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tfUsername.setRadius(5);
        tfUsername.setSelectionColor(new java.awt.Color(0, 0, 0));

        imageAvatar.setBorderSize(1);
        imageAvatar.setImage(new javax.swing.ImageIcon(getClass().getResource("/com/app/icon/user.png"))); // NOI18N

        lbFullName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbFullName.setText("Fullname");

        tfFullName.setText("Donald JohnTrump");
        tfFullName.setColor(new java.awt.Color(255, 255, 255));
        tfFullName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tfFullName.setRadius(5);
        tfFullName.setSelectionColor(new java.awt.Color(0, 0, 0));

        lbMail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbMail.setText("Mail");

        tfMail.setText("mail123@gmail.com");
        tfMail.setColor(new java.awt.Color(255, 255, 255));
        tfMail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tfMail.setRadius(5);
        tfMail.setSelectionColor(new java.awt.Color(0, 0, 0));

        lbPhone.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbPhone.setText("Phone");

        tfPhone.setText("0123456789");
        tfPhone.setColor(new java.awt.Color(255, 255, 255));
        tfPhone.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tfPhone.setRadius(5);
        tfPhone.setSelectionColor(new java.awt.Color(0, 0, 0));

        cbGender.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));
        cbGender.setForeground(new java.awt.Color(128, 128, 128));
        cbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female", "Other" }));
        cbGender.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cbGender.setFontFamily(new java.awt.Font("Arial", 0, 18)); // NOI18N

        lbGender.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbGender.setText("Gender");

        lbUsername.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbUsername.setText("Username");

        pfCurrentPassword.setForeground(new java.awt.Color(128, 128, 128));
        pfCurrentPassword.setText("passwordsupervip");
        pfCurrentPassword.setColor(new java.awt.Color(255, 255, 255));
        pfCurrentPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pfCurrentPassword.setRadius(5);

        lbCurrentPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbCurrentPassword.setText("Current Password");

        cbChangePassword.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        cbChangePassword.setText("Change password");

        lbNewPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbNewPassword.setText("New Password");

        pfNewPassword.setForeground(new java.awt.Color(128, 128, 128));
        pfNewPassword.setText("passwordsupervip");
        pfNewPassword.setColor(new java.awt.Color(255, 255, 255));
        pfNewPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pfNewPassword.setRadius(5);

        pfConfirmNewPassword.setForeground(new java.awt.Color(128, 128, 128));
        pfConfirmNewPassword.setText("passwordsupervip");
        pfConfirmNewPassword.setColor(new java.awt.Color(255, 255, 255));
        pfConfirmNewPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pfConfirmNewPassword.setRadius(5);

        lbConfirmPassword.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbConfirmPassword.setText("Confirm New Password");

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        lbMess.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbMess.setForeground(new java.awt.Color(255, 102, 102));
        lbMess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbMess.setText("Invalid");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(334, 334, 334)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(imageAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lbNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pfNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lbCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pfCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lbMail, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfMail, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lbFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1)
                            .addComponent(cbChangePassword)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lbConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pfConfirmNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbMess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(imageAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbFullName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbChangePassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfConfirmNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbMess, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        Model_Update_User user = new Model_Update_User(
                tfUsername.getText().trim(),
                tfFullName.getText().trim(),
                tfMail.getText().trim(),
                tfPhone.getText().trim(),
                cbGender.getSelectedItem().toString(),
                image,
                String.copyValueOf(pfCurrentPassword.getPassword()),
                String.copyValueOf(pfNewPassword.getPassword()),
                String.copyValueOf(pfConfirmNewPassword.getPassword()));
        
        Service.getInstance().getClient().emit("edit_account",user.toJSONObject(), new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    boolean isAction = (boolean)os[0];
                    if (isAction) {
                        Service.getInstance().setUserAccount(new UserAccount(os[1]));
                        GlassPanePopup.closePopupLast();
                        PublicEvent.getInstance().getEventMenuRight().onRefresh();
                    } else {
                        lbMess.setText(os[1].toString());
                    }
                }
            }
        });
    }//GEN-LAST:event_updateButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbChangePassword;
    private com.app.component.CustomComboBox cbGender;
    private com.app.swing.ImageAvatar imageAvatar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbConfirmPassword;
    private javax.swing.JLabel lbCurrentPassword;
    private javax.swing.JLabel lbFullName;
    private javax.swing.JLabel lbGender;
    private javax.swing.JLabel lbMail;
    private javax.swing.JLabel lbMess;
    private javax.swing.JLabel lbNewPassword;
    private javax.swing.JLabel lbPhone;
    private javax.swing.JLabel lbUsername;
    private com.app.component.CustomPanel panel;
    private com.app.component.CustomPasswordField pfConfirmNewPassword;
    private com.app.component.CustomPasswordField pfCurrentPassword;
    private com.app.component.CustomPasswordField pfNewPassword;
    private com.app.component.CustomTextField tfFullName;
    private com.app.component.CustomTextField tfMail;
    private com.app.component.CustomTextField tfPhone;
    private com.app.component.CustomTextField tfUsername;
    private com.app.component.CustomButton updateButton;
    // End of variables declaration//GEN-END:variables
}
