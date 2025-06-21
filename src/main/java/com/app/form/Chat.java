package com.app.form;

import com.app.component.Chat_Body;
import com.app.component.Chat_Bottom;
import com.app.component.Chat_Title;
import com.app.event.EventChat;
import com.app.event.PublicEvent;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import net.miginfocom.swing.MigLayout;

public class Chat extends javax.swing.JPanel {
    
    private Chat_Title chatTitle;
    private Chat_Body chatBody;
    private Chat_Bottom chatBottom;
    
    public Chat() {
        initComponents();
        init();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx", "0[fill]0", "5[]0[100%, fill]0[shrink 0]0"));
        chatTitle = new Chat_Title();
        chatBody = new Chat_Body();
        chatBottom = new Chat_Bottom();
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendMessage(Model_Send_Message data) {
                PublicEvent.getInstance().getEventChatBody().clearSendStatus();
                PublicEvent.getInstance().getEventChatBody().showSending();
                chatBody.addItemRight(data);
                PublicEvent.getInstance().getEventChatBody().showSent();
            }

            @Override
            public void reiceveMessage(Model_Send_Message data) {
                if (chatTitle.getUser() != null && chatTitle.getUser().getUserId() == data.getFromUserID()) {
                    chatBody.addItemLeft(data);
                }
            }
        });
        add(chatTitle, "wrap");
        add(chatBody, "wrap, grow");
        add(chatBottom, "h ::50%");
    }
    
    public void setUser(UserAccount user) {
        chatTitle.setUserName(user);
        chatBottom.setUser(user);
        chatBody.setUser(user);
    }
    
    public void updateUser(UserAccount user) {
        chatTitle.updateUser(user);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
