package com.app.component;

import com.app.emoji.Emogi;
import com.app.enums.MessageType;
import com.app.event.EventChatBody;
import com.app.event.PublicEvent;
import com.app.model.History;
import com.app.model.Model_Image;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import com.app.security.ChatManager;
import com.app.service.Service;
import com.app.util.Utils;
import io.socket.client.Ack;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class Chat_Body extends javax.swing.JPanel {

    @Getter
    private UserAccount user;
    private JLabel sendStatus;
    private Typing typing = new Typing();
    private javax.swing.Timer typingTimer;

    public Chat_Body() {
        initComponents();
        init();
    }

    private void init() {
        body.setLayout(new MigLayout("fillx"));
        sp.setVerticalScrollBar(new JScrollBar());
        sp.getVerticalScrollBar().setBackground(Color.WHITE);
        javax.swing.JPanel spacer = new javax.swing.JPanel();
        spacer.setBackground(new java.awt.Color(255, 255, 255));
        body.add(spacer, "grow, push, wrap");

        PublicEvent.getInstance().setEventChatBody(new EventChatBody() {
            @Override
            public void showSending() {
                removeItemSendStatus();
                addItemSendStatus("Sending...");
            }

            @Override
            public void showSent() {
                removeItemSendStatus();
                addItemSendStatus("Sent");
            }

            @Override
            public void clearSendStatus() {
                removeItemSendStatus();

            }

            @Override
            public void showTypingStatus(String username) {
                if (user.getUserName().equals(username)) {
                    addTypingStatus();
                    if (typingTimer != null && typingTimer.isRunning()) {
                        typingTimer.restart();
                    } else {
                        typingTimer = new javax.swing.Timer(1000, e -> removeTypingStatus());
                        typingTimer.setRepeats(false);
                        typingTimer.start();
                    }
                }
            }

            @Override
            public void clearTypingStatus() {
                removeTypingStatus();
            }

        });
    }

    public void setUser(UserAccount user) {
        clearChat();
        this.user = user;
        String lastDate = "";

        List<Model_Save_Message> listSave_Messages = ChatManager.getInstance().getHistoryFromDB(user.getUserId());
        for (Model_Save_Message save_ms : listSave_Messages) {

            try {
                String currentDate = Utils.formatTime(save_ms.getTime(), "MMM dd, yyyy");
                if (!currentDate.equals(lastDate)) {
                    addDate(currentDate);
                    lastDate = currentDate;
                }
                if (save_ms.getFromUserID() == Service.getInstance().getUserAccount().getUserId()) {
                    addItemRight(new Model_Send_Message(
                            save_ms.getMesage_id(),
                            MessageType.toMessageType(save_ms.getMessageType()),
                            save_ms.getContent(),
                            save_ms.getFileExtension(),
                            save_ms.getBlurHash(),
                            save_ms.getHeight_blur(),
                            save_ms.getWidth_blur(),
                            save_ms.getTime()
                    ));
                } else {

                    addItemLeft(new Model_Send_Message(
                            save_ms.getMesage_id(),
                            MessageType.toMessageType(save_ms.getMessageType()),
                            save_ms.getContent(),
                            save_ms.getFileExtension(),
                            save_ms.getBlurHash(),
                            save_ms.getHeight_blur(),
                            save_ms.getWidth_blur(),
                            save_ms.getTime()
                    ), save_ms.getFileExtension());
                }
            } catch (Exception ex) {
                System.getLogger(Chat_Body.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        long fromMessageID = 0;

        if (listSave_Messages.size() > 0) {
            fromMessageID = listSave_Messages.getLast().getMesage_id();
        }

        History history = new History(Service.getInstance().getUserAccount().getUserId(), user.getUserId(), fromMessageID);

        Service.getInstance().getClient().emit("list_message", history.toJsonObject(), new Ack() {
            @Override
            public void call(Object... os) {
                String lastDate = "";
                for (Object o : os) {
                    Model_Send_Message message = new Model_Send_Message(o);
                    String currentDate = Utils.formatTime(message.getTime(), "MMM dd, yyyy");
                    if (!currentDate.equals(lastDate)) {
                        addDate(currentDate);
                        lastDate = currentDate;
                    }
                    if (message.getFromUserID() == Service.getInstance().getUserAccount().getUserId()) {
                        addItemRight(message);
                    } else {
                        try {
                            message.setContent(ChatManager.getInstance().receiveMessage(message));
                        } catch (Exception ex) {
                            message.setContent("");
                        }

                        addItemLeft(message, message.getFileExtension());
                    }

                }
            }
        });
    }

    public void addTypingStatus() {
        body.add(typing, "wrap, al left, w 100::80%");
        body.repaint();
        body.revalidate();
        scrollToBottom();
    }

    public void removeTypingStatus() {
        body.remove(typing);
        body.repaint();
        body.revalidate();
    }

    public void addItemSendStatus(String message) {
        sendStatus = new javax.swing.JLabel(message);
        sendStatus.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 10));
        sendStatus.setForeground(new java.awt.Color(150, 150, 150));
        sendStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        body.add(sendStatus, "wrap, al right, w 100::80%");
        body.repaint();
        body.revalidate();
        scrollToBottom();
    }

    public void removeItemSendStatus() {
        if (sendStatus != null) {
            body.remove(sendStatus);
            sendStatus = null;
            body.repaint();
            body.revalidate();
        }
    }

    public void addItemLeft(Model_Send_Message data) {
        if (data.getMessageType() == MessageType.TEXT) {
            Chat_Left item = new Chat_Left();
            item.setText(data.getContent());
            item.setTime(data.getTime());
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.EMOJI) {
            Chat_Left item = new Chat_Left();
            item.setEmoji(Emogi.getInstance().getImoji(Integer.valueOf(data.getContent().toString())).getIcon());
            item.setTime(data.getTime());
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.IMAGE) {
            Chat_Left item = new Chat_Left();
            item.setText("");
            item.setTime(data.getTime());
            item.setImage(new Model_Image(
                    data.getId(),
                    data.getBlurHash(),
                    data.getFileExtension(),
                    data.getWidth_blur(),
                    data.getHeight_blur(),
                    data.getEncryptedContent(),
                    data.getSignature(),
                    data.getEncryptedAESKey(),
                    data.getPubkeyDSAFromUser()
            ));
            body.add(item, "wrap, w 100::80%");
        }
        scrollToBottom();
        repaint();
        revalidate();
    }

    public void addItemLeft(Model_Send_Message data, String fileExtension) {
        if (data.getMessageType() == MessageType.TEXT) {
            Chat_Left item = new Chat_Left();
            item.setText(data.getContent());
            item.setTime(data.getTime());
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.EMOJI) {
            Chat_Left item = new Chat_Left();
            item.setEmoji(Emogi.getInstance().getImoji(Integer.valueOf(data.getContent())).getIcon());
            item.setTime(data.getTime());
            body.add(item, "wrap, w 100::80%");
        } else if (data.getMessageType() == MessageType.IMAGE) {
            Chat_Left item = new Chat_Left();
            item.setText("");

            String relativePath = "client_data/" + data.getId() + fileExtension;
            if (Utils.isImageFileExists(relativePath)) {
                item.setImage(relativePath);
            } else {

                item.setImage(new Model_Image(
                        data.getId(),
                        data.getBlurHash(),
                        data.getFileExtension(),
                        data.getWidth_blur(),
                        data.getHeight_blur(),
                        data.getEncryptedContent(),
                        data.getSignature(),
                        data.getEncryptedAESKey(),
                        data.getPubkeyDSAFromUser()
                ));
            }

            item.setTime(data.getTime());
            body.add(item, "wrap, w 100::80%");
        }
        scrollToBottom();
        repaint();
        revalidate();
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

    public void addItemLeft(String text, String user, String[] image) {
        Chat_Left_With_Profile item = new Chat_Left_With_Profile();
        item.setText(text);
//        item.setImage(image);
        item.setTime();
        item.setUserProfile(user);
        body.add(item, "wrap, w 100::80%");
        body.repaint();
        body.revalidate();
    }

    public void addItemFile(String text, String user, String fileName, String fileSize) {
        Chat_Left_With_Profile item = new Chat_Left_With_Profile();
        item.setText(text);
        item.setFile(fileName, fileSize);
        item.setTime();
        item.setUserProfile(user);
        body.add(item, "wrap, w 100::80%");
        body.repaint();
        body.revalidate();
    }

    public void addItemRight(Model_Send_Message data) {
        if (data.getMessageType() == MessageType.TEXT) {
            Chat_Right item = new Chat_Right();
            item.setText(data.getContent());
            item.setTime(data.getTime());
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getMessageType() == MessageType.EMOJI) {
            Chat_Right item = new Chat_Right();
            item.setEmoji(Emogi.getInstance().getImoji(Integer.valueOf(data.getContent())).getIcon());
            item.setTime(data.getTime());
            body.add(item, "wrap, al right, w 100::80%");
        } else if (data.getMessageType() == MessageType.IMAGE) {
            Chat_Right item = new Chat_Right();
            item.setText("");
            String relativePath = "client_data/" + data.getId() + data.getFileExtension();
            if (Utils.isImageFileExists(relativePath)) {
                item.setImage(relativePath);
            } else if (data.getFile() != null) {
                item.setImage(data.getFile());
            }
//            else if (data.getBlurHash().length() > 0) {
//                item.setImage(new Model_Image(data.getId(), data.getBlurHash(), data.getFileExtension(), data.getWidth_blur(), data.getHeight_blur(), data.getContent(), data.getSignature(), data.getEncryptedAESKey(), data.getPubkeyDSAFromUser()));
//            }
            item.setTime(data.getTime());
            body.add(item, "wrap, al right, w 100::80%");

        }
        repaint();
        revalidate();
        scrollToBottom();
    }

    public void addItemRight(String text, Icon... image) {
        Chat_Right item = new Chat_Right();
        item.setText(text);
        item.setImage(image);
        body.add(item, "wrap, al right, w 100::80%");
        body.repaint();
        body.revalidate();
        item.setTime(null);
        scrollToBottom();
    }

    public void addItemFileRight(String text, String fileName, String fileSize) {
        Chat_Right item = new Chat_Right();
        item.setText(text);
        item.setFile(fileName, fileSize);
        body.add(item, "wrap, al right, w 100::80%");
        body.repaint();
        body.revalidate();
        scrollToBottom();
    }

    public void addDate(String date) {
        Chat_Date item = new Chat_Date();
        item.setDate(date);
        body.add(item, "wrap, al center");
        body.repaint();
        body.revalidate();
    }

    public void clearChat() {
        body.removeAll();
        repaint();
        revalidate();
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
            .addComponent(sp)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void scrollToBottom() {
        JScrollBar verticalBar = sp.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
