package com.app.component;

import com.app.dao.MessageDAO;
import com.app.enums.MessageType;
import com.app.event.PublicEvent;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import com.app.security.ChatManager;
import com.app.service.Service;
import com.app.swing.JIMSendTextPane;
import io.socket.client.Ack;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;

public class Chat_Bottom extends javax.swing.JPanel {

    private UserAccount user;
    private MessageDAO messageDAO;


    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
        panelMore.setUser(user);
    }

    public Chat_Bottom() {
        initComponents();
        init();
    }
    
    

    private void init() {
        messageDAO = new MessageDAO();
        mig = new MigLayout("fillx, filly", "0[fill]0[]0[]2", "2[fill]2[]0");
        setLayout(mig);
        JScrollPane scroll = new JScrollPane();
//        scroll.setBorder(null);
        JIMSendTextPane txt = new JIMSendTextPane();
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                refresh();
                if (e.getKeyChar() == 10 && e.isControlDown()) {
                    eventSend(txt);
                }
            }
        });
        txt.setHintText("Write message here...");
        scroll.setViewportView(txt);
        add(scroll, "w 100%");
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("filly", "0[]3[]0", "0[bottom]0"));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(22, 22));
        JButton cmd = new JButton();
        cmd.setBorder(null);
        cmd.setContentAreaFilled(false);
        cmd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmd.setIcon(new ImageIcon(getClass().getResource("/com/app/icon/send.png")));
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventSend(txt);
            }
        });
        JButton cmdMore = new JButton();
        cmdMore.setBorder(null);
        cmdMore.setContentAreaFilled(false);
        cmdMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/app/icon/more_disable.png")));
        cmdMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (panelMore.isVisible()) {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/app/icon/more_disable.png")));
                    panelMore.setVisible(false);
                    mig.setComponentConstraints(panelMore, "dock south,h 0!");
                    revalidate();
                } else {
                    cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/app/icon/more.png")));
                    panelMore.setVisible(true);
                    mig.setComponentConstraints(panelMore, "dock south,h 170!");
                    revalidate();
                }
            }
        });
        panel.add(cmdMore);
        panel.add(cmd);
        add(panel, "wrap");
        panelMore = new Panel_More();
        panelMore.setVisible(false);
        add(panelMore, "dock south, h 0!");
    }

    private void send(Model_Send_Message data) {
        Model_Save_Message saveMessage;
        try {
            PublicEvent.getInstance().getEventChatBody().clearSendStatus();
            PublicEvent.getInstance().getEventChatBody().showSending();
            saveMessage = ChatManager.getInstance().sendMessage(data, user);
            Service.getInstance().getClient().emit("send_to_user", data.toJSONObject(), new Ack() {
                @Override
                public void call(Object... os) {
                    saveMessage.setMesage_id(Long.parseLong(os[0].toString()));
                    ChatManager.getInstance().saveMessage(saveMessage);
                    PublicEvent.getInstance().getEventChatBody().showSent();
                }
            });
        } catch (Exception ex) {
            System.getLogger(Chat_Bottom.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        System.out.println(data.toJSONObject().toString());
    }

    private void refresh() {
        revalidate();
    }

    private void eventSend(JIMSendTextPane txt) {
        String text = txt.getText().trim();
        if (!txt.equals("")) {
            Model_Send_Message message
                    = new Model_Send_Message(
                            MessageType.TEXT,
                            Service.getInstance().getUserAccount().getUserId(),
                            user.getUserId(),
                            text,
                            LocalDateTime.now()
                    );
            send(message);
            PublicEvent.getInstance().getEventChat().sendMessage(message);
            txt.setText("");
            txt.grabFocus();
            refresh();
        } else {
            txt.grabFocus();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private MigLayout mig;
    private Panel_More panelMore;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
