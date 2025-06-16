package com.app.security;

import com.app.dao.MessageDAO;
import com.app.enums.MessageType;
import com.app.model.Model_Receive_Message;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import com.app.service.Service;
import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class ChatManager {

    private static ChatManager instance;
    private MessageDAO messageDAO = new MessageDAO();

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    // Gửi tin nhắn
    public Model_Save_Message sendMessage(Model_Send_Message sms, UserAccount user) throws Exception {
        // Tạo khóa AES
        SecretKey aesKey = ChatCrypto.generateAESKey();

        // Mã hóa tin nhắn bằng AES
        String encryptedMessage = ChatCrypto.encryptMessage(sms.getContent(), aesKey);

        // Ký tin nhắn bằng DSA
        PrivateKey senderPrivateKey = Session.getInstance().getDsaPrivateKey();
        String signature = ChatCrypto.signMessage(sms.getContent(), senderPrivateKey);

        // Mã hóa khóa AES bằng RSA của người nhận
        String receiverPubkeyRSA = user.getPubkeyRSA();
        PublicKey receiverPublicKey = KeyUtil.getInstance().decodePublicKey(receiverPubkeyRSA, "RSA");
        String encryptedAESKey = ChatCrypto.encryptAESKey(aesKey, receiverPublicKey);

        // Mã hóa khóa AES cho người gửi (để xem lại)
        String encryptedAESKeyForSender = ChatCrypto.encryptAESKey(aesKey, Session.getInstance().getRsaPublicKey());

        sms.setEncryptedContent(encryptedMessage);
        sms.setSignature(signature);
        sms.setEncryptedAESKey(encryptedAESKey);

        return new Model_Save_Message(
                sms.getMessageType().getValue(),
                sms.getFromUserID(),
                sms.getToUserID(),
                sms.getContent(),
                sms.getTime()
        );

    }

    // Nhận tin nhắn
    public String receiveMessage(Model_Receive_Message messageData) throws Exception {
        if (messageData.getMessageType().getValue() == MessageType.FILE.getValue() || messageData.getMessageType().getValue() == MessageType.IMAGE.getValue()) {
            saveMessage(
                    new Model_Save_Message(
                            messageData.getMessageID(),
                            messageData.getMessageType().getValue(),
                            messageData.getFromUserID(),
                            messageData.getToUserID(),
                            messageData.getDataImage().getFileExtension(),
                            messageData.getDataImage().getImage(),
                            messageData.getDataImage().getHeight(),
                            messageData.getDataImage().getWidth(),
                            messageData.getTime()
                    )
            );
            return "";
        }
        String encryptedMessage = messageData.getEncryptedContent();
        String signature = messageData.getSignature();
        String encryptedAESKey = messageData.getEncryptedAESKey();
        
        if (encryptedAESKey == null || encryptedAESKey.isEmpty()) {
            throw new Exception("Khóa AES mã hóa rỗng hoặc null!");
        }
        if (encryptedMessage == null || encryptedMessage.isEmpty()) {
            throw new Exception("Tin nhắn mã hóa rỗng hoặc null!");
        }
        if (signature == null || signature.isEmpty()) {
            throw new Exception("Chữ ký rỗng hoặc null!");
        }

        // Giải mã khóa AES
        PrivateKey receiverPrivateKey = Session.getInstance().getRsaPrivateKey();
        if (receiverPrivateKey == null) {
            throw new Exception("Private key RSA của người nhận là null!");
        }

        SecretKey aesKey;
        try {
            aesKey = ChatCrypto.decryptAESKey(encryptedAESKey, receiverPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi giải mã khóa AES: " + e.getMessage());
        }

        // Giải mã tin nhắn
        String decryptedMessage;
        try {
            decryptedMessage = ChatCrypto.decryptMessage(encryptedMessage, aesKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi giải mã tin nhắn: " + e.getMessage());
        }

        // Xác minh chữ ký
        PublicKey senderPublicKey = KeyUtil.getInstance().decodePublicKey(messageData.getPubkeyDSAFromUser(), "DSA");
        if (senderPublicKey == null) {
            throw new Exception("Public key DSA của người gửi là null!");
        }
        if (!ChatCrypto.verifySignature(decryptedMessage, signature, senderPublicKey)) {
            throw new Exception("Chữ ký không hợp lệ!");
        }


        // Lưu tin nhắn cục bộ
        try {
            saveMessage(
                    new Model_Save_Message(
                            messageData.getMessageID(),
                            messageData.getMessageType().getValue(),
                            messageData.getFromUserID(),
                            messageData.getToUserID(),
                            decryptedMessage,
                            messageData.getTime()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi lưu tin nhắn: " + e.getMessage());
        }

        return decryptedMessage;
    }
    
    public String receiveMessage(Model_Send_Message messageData) throws Exception {
        if (messageData.getMessageType().getValue() == MessageType.FILE.getValue() || messageData.getMessageType().getValue() == MessageType.IMAGE.getValue()) {
            saveMessage(
                    new Model_Save_Message(
                            messageData.getId(),
                            messageData.getMessageType().getValue(),
                            messageData.getFromUserID(),
                            messageData.getToUserID(),
                            messageData.getFileExtension(),
                            messageData.getBlurHash(),
                            messageData.getHeight_blur(),
                            messageData.getWidth_blur(),
                            messageData.getTime()
                    )
            );
            return "";
        }
        String encryptedMessage = messageData.getEncryptedContent();
        String signature = messageData.getSignature();
        String encryptedAESKey = messageData.getEncryptedAESKey();
        
        if (encryptedAESKey == null || encryptedAESKey.isEmpty()) {
            throw new Exception("Khóa AES mã hóa rỗng hoặc null!");
        }
        if (encryptedMessage == null || encryptedMessage.isEmpty()) {
            throw new Exception("Tin nhắn mã hóa rỗng hoặc null!");
        }
        if (signature == null || signature.isEmpty()) {
            throw new Exception("Chữ ký rỗng hoặc null!");
        }

        // Giải mã khóa AES
        PrivateKey receiverPrivateKey = Session.getInstance().getRsaPrivateKey();
        if (receiverPrivateKey == null) {
            throw new Exception("Private key RSA của người nhận là null!");
        }

        SecretKey aesKey;
        try {
            aesKey = ChatCrypto.decryptAESKey(encryptedAESKey, receiverPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi giải mã khóa AES: " + e.getMessage());
        }

        // Giải mã tin nhắn
        String decryptedMessage;
        try {
            decryptedMessage = ChatCrypto.decryptMessage(encryptedMessage, aesKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi giải mã tin nhắn: " + e.getMessage());
        }

        // Xác minh chữ ký
        PublicKey senderPublicKey = KeyUtil.getInstance().decodePublicKey(messageData.getPubkeyDSAFromUser(), "DSA");
        if (senderPublicKey == null) {
            throw new Exception("Public key DSA của người gửi là null!");
        }
        if (!ChatCrypto.verifySignature(decryptedMessage, signature, senderPublicKey)) {
            throw new Exception("Chữ ký không hợp lệ!");
        }


        // Lưu tin nhắn cục bộ
        try {
            saveMessage(
                    new Model_Save_Message(
                            messageData.getId(),
                            messageData.getMessageType().getValue(),
                            messageData.getFromUserID(),
                            messageData.getToUserID(),
                            decryptedMessage,
                            messageData.getTime()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi lưu tin nhắn: " + e.getMessage());
        }

        return decryptedMessage;
    }

    // Lưu tin nhắn cục bộ cho người gửi
    public void saveMessage(Model_Save_Message ms) {
        messageDAO.saveMessage(ms);
    }
    
    public List<Model_Save_Message> getHistoryFromDB(Long toUserId) {
        return messageDAO.getHistoryMessage(
                Service.getInstance().getUserAccount().getUserId(), 
                toUserId
        ).join();
    }

}
