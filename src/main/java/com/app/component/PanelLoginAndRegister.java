/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.app.component;

import com.app.event.EventMessage;
import com.app.event.PublicEvent;
import com.app.form.Login;
import com.app.model.Model_Key;
import com.app.model.Model_Login;
import com.app.model.Model_Message;
import com.app.model.Model_Register;
import com.app.security.KeyUtil;
import com.app.security.Session;
import com.app.service.Service;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author LENOVO
 */
public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    /**
     * Creates new form PanelCover
     */
    private ActionListener event;

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        setOpaque(false);
        initLogin(eventLogin);
        initRegister(eventRegister);
        login.setVisible(true);
        register.setVisible(false);

    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]20[]10[]10[]10[]10[]10[]30[]20[]20[]150"));

        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", Font.BOLD, 30));
        label.setForeground(new Color(28, 194, 253)); // Match login title color
        register.add(label);

        CustomTextField tfFullName = new CustomTextField();
        tfFullName.setHint("Fullname");
        tfFullName.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_infor_24px.png")));
        register.add(tfFullName, "w 60%");

        CustomTextField tfUser = new CustomTextField();
        tfUser.setHint("Username");
        tfUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_user_24px.png")));
        register.add(tfUser, "w 60%");

        CustomPasswordField pfPassword = new CustomPasswordField();
        pfPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_password_20px.png")));
        pfPassword.setHint(" Password");
        register.add(pfPassword, "w 60%");

        CustomPasswordField pfPasswordConfirm = new CustomPasswordField();
        pfPasswordConfirm.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_password_20px.png")));
        pfPasswordConfirm.setHint(" Confirm your password");
        register.add(pfPasswordConfirm, "w 60%");

        CustomTextField tfMail = new CustomTextField();
        tfMail.setHint(" Mail");
        tfMail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_mail_20px.png")));
        register.add(tfMail, "w 60%");

        CustomTextField tfPhone = new CustomTextField();
        tfPhone.setHint(" Phone");
        tfPhone.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_phone_20px.png")));
        register.add(tfPhone, "w 60%");

        // Colors matching login screen
        Color mainColor = new Color(28, 194, 253);
        Color hoverColor = new Color(46, 210, 255);
        Color clickColor = new Color(12, 162, 220);
        Color white = new Color(255, 255, 255);
        Color lightBackground = new Color(245, 250, 252);
        Color lightHover = new Color(230, 245, 250);
        Color lightClick = new Color(210, 235, 245);

        CustomButton cmd = new CustomButton();
        cmd.setBackground(mainColor);
        cmd.setForeground(white);
        cmd.setBorderColor(mainColor);
        cmd.setColorClick(clickColor);
        cmd.setColorOver(hoverColor);
        cmd.setColor(mainColor);
        cmd.setForegroundColor(white);
        cmd.setForegroundClick(white);
        cmd.setForegroundOver(white);
        cmd.setRadius(10);
        cmd.setText("REGISTER");
        cmd.setFont(new Font("sansserif", Font.BOLD, 14));
        cmd.addActionListener(eventRegister);
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Model_Key key = KeyUtil.getInstance().createdKey();
//
//                Model_Register data = new Model_Register(
//                        tfUser.getText().trim(),
//                        String.copyValueOf(pfPassword.getPassword()),
//                        tfFullName.getText().trim(),
//                        tfMail.getText().trim(),
//                        tfPhone.getText().trim(),
//                        Base64.getEncoder().encodeToString(key.getDsa_public_key().getEncoded()),
//                        Base64.getEncoder().encodeToString(key.getRsa_public_key().getEncoded())
//                );
//                PublicEvent.getInstance().getEventLogin().register(data, new EventMessage() {
//                    @Override
//                    public void callMessage(Model_Message message) {
//
//                        if (!message.isAction()) {
//                            Login.getInstance().showMessage(PanelMessage.MessageType.SUCCESS, message.getMessage());
//                        } else {
//                            KeyUtil.getInstance().saveKey(Service.getInstance().getUserAccount().getUserId(), key);
//                            Session.getInstance().setKey(key);
//                            PublicEvent.getInstance().getEventMain().initChat();
//                        }
//                    }
//                });

                Model_Register data = new Model_Register(
                        tfUser.getText().trim(),
                        String.copyValueOf(pfPassword.getPassword()),
                        tfFullName.getText().trim(),
                        tfMail.getText().trim(),
                        tfPhone.getText().trim(),
                        "",
                        "",
                        ""
                );

                PublicEvent.getInstance().getEventLogin().initRegister(data, new EventMessage() {
                    @Override
                    public void callMessage(Model_Message message) {
                        if (!message.isAction()) {
                            Login.getInstance().showMessage(PanelMessage.MessageType.ERROR, message.getMessage());
                        }
                    }
                });
            }
        });
        register.add(cmd, "w 60%, h 40");
        register.setVisible(false);

        JSeparator jSeparator = new JSeparator();
        register.add(jSeparator, "w 60%");

        CustomButton cmdChange = new CustomButton();
        cmdChange.setBorderSize(1);
        cmdChange.setBorderColor(mainColor);
        cmdChange.setBackground(lightBackground);
        cmdChange.setForeground(mainColor);
        cmdChange.setColorOver(lightHover);
        cmdChange.setColorClick(lightClick);
        cmdChange.setColor(lightBackground);
        cmdChange.setRadius(10);
        cmdChange.setForegroundColor(mainColor);
        cmdChange.setForegroundClick(clickColor);
        cmdChange.setForegroundOver(hoverColor);
        cmdChange.setText("LOGIN");
        cmdChange.setFont(new Font("sansserif", Font.BOLD, 14));
        cmdChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event.actionPerformed(e);
            }
        });

        register.add(cmdChange, "w 60%, h 40");
    }

    private void initLogin(ActionListener eventLogin) {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]50[]10[]10[]50[]20[]20[]150"));

        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif", Font.BOLD, 30));
        label.setForeground(new Color(28, 194, 253)); // Màu tiêu đề
        login.add(label);

        CustomTextField tfUser = new CustomTextField();
        tfUser.setHint("Username");
        tfUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_user_24px.png")));
        login.add(tfUser, "w 60%");

        CustomPasswordField pfPassword = new CustomPasswordField();
        pfPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/com/app/icon/login/icon_password_20px.png")));
        pfPassword.setHint(" Password");
        login.add(pfPassword, "w 60%");

        CustomButton cmdForget = new CustomButton();
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdForget.setColorOver(null);
        cmdForget.setColorClick(null);
        cmdForget.setBorderSize(0);
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", Font.BOLD, 12));
        cmdForget.setText("Forgot your password?");
        cmdForget.setForegroundColor(new Color(100, 100, 100));
        cmdForget.setForegroundClick(new Color(100, 100, 100));
        cmdForget.setForegroundOver(new Color(100, 100, 100));
        login.add(cmdForget, "w 60%");

        Color mainColor = new Color(28, 194, 253);
        Color hoverColor = new Color(46, 210, 255);
        Color clickColor = new Color(12, 162, 220);
        Color white = new Color(255, 255, 255);
        Color lightBackground = new Color(245, 250, 252);
        Color lightHover = new Color(230, 245, 250);
        Color lightClick = new Color(210, 235, 245);

        CustomButton cmd = new CustomButton();
        cmd.setBackground(mainColor);
        cmd.setForeground(white);
        cmd.setBorderColor(mainColor);
        cmd.setColorClick(clickColor);
        cmd.setColorOver(hoverColor);
        cmd.setColor(mainColor);
        cmd.setForegroundColor(white);
        cmd.setForegroundClick(white);
        cmd.setForegroundOver(white);
        cmd.setRadius(10);
        cmd.setText("LOGIN");
        cmd.setFont(new Font("sansserif", Font.BOLD, 14));
        cmd.addActionListener(eventLogin);
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicEvent
                        .getInstance()
                        .getEventLogin()
                        .login(new Model_Login(
                                tfUser.getText(),
                                String.copyValueOf(pfPassword.getPassword()
                                )));
            }
        });
        login.add(cmd, "w 60%, h 40");

        JSeparator jSeparator = new JSeparator();
        login.add(jSeparator, "w 60%");

        CustomButton cmdChange = new CustomButton();
        cmdChange.setBorderSize(1);
        cmdChange.setBorderColor(mainColor);
        cmdChange.setBackground(lightBackground);
        cmdChange.setForeground(mainColor);
        cmdChange.setColorOver(lightHover);
        cmdChange.setColorClick(lightClick);
        cmdChange.setColor(lightBackground);
        cmdChange.setRadius(10);
        cmdChange.setForegroundColor(mainColor);
        cmdChange.setForegroundClick(clickColor);
        cmdChange.setForegroundOver(hoverColor);
        cmdChange.setText("REGISTER");
        cmdChange.setFont(new Font("sansserif", Font.BOLD, 14));
        cmdChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event.actionPerformed(e);

            }
        });
        login.add(cmdChange, "w 60%, h 40");
    }

    public void setEvent(ActionListener event) {
        this.event = event;
    }

    public void showLogin(boolean show) {
        if (show) {
            login.setVisible(false);
            register.setVisible(true);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        register = new javax.swing.JPanel();
        login = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.CardLayout());

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card3");

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
