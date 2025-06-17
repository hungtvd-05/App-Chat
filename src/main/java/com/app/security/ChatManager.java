package com.app.security;

import com.app.dao.MessageDAO;
import com.app.enums.MessageType;
import com.app.model.FileData;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import com.app.service.Service;
import java.io.File;
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

    public File sendFile(Model_Send_Message sms, UserAccount user, File inputFile) throws Exception {
        // Tạo khóa AES
        SecretKey aesKey = ChatCrypto.generateAESKey();

        // Mã hóa file bằng AES
        FileData encryptedFileData = ChatCrypto.encryptFile(inputFile, aesKey);
        File encryptedFile = encryptedFileData.getFile();
        byte[] iv = encryptedFileData.getIv();

        // Ký file gốc bằng DSA
        PrivateKey senderPrivateKey = Session.getInstance().getDsaPrivateKey();
        String signature = ChatCrypto.signFile(inputFile, senderPrivateKey);

        // Mã hóa khóa AES bằng RSA của người nhận
        String receiverPubkeyRSA = user.getPubkeyRSA();
        PublicKey receiverPublicKey = KeyUtil.getInstance().decodePublicKey(receiverPubkeyRSA, "RSA");
        String encryptedAESKey = ChatCrypto.encryptAESKey(aesKey, receiverPublicKey);

        sms.setEncryptedAESKey(encryptedAESKey);
        sms.setSignature(signature);
        sms.setEncryptedContent(Base64.getEncoder().encodeToString(iv));

        return encryptedFile;
    }

    // Nhận tin nhắn
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
            System.out.println("luu tin nhan");
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

    public String receiveFile(File encryptedFile, long fileID, String fileExtention, String encryptedContent, String signature, String encryptedAESKey, String pubkeyDSAFromUser) throws Exception {
              
        PrivateKey receiverPrivateKey = Session.getInstance().getRsaPrivateKey();
        if (receiverPrivateKey == null) {
            throw new Exception("Private key RSA của người nhận là null!");
        }
        SecretKey aesKey = ChatCrypto.decryptAESKey(encryptedAESKey, receiverPrivateKey);

        // Giải mã file
        byte[] iv = Base64.getDecoder().decode(encryptedContent);
        String outputFileName = "client_data/" + fileID + fileExtention;
        File decryptedFile = ChatCrypto.decryptFile(encryptedFile, aesKey, iv, outputFileName);

        // Xác minh chữ ký DSA
        PublicKey senderPublicKey = KeyUtil.getInstance().decodePublicKey(pubkeyDSAFromUser, "DSA");
        if (senderPublicKey == null) {
            throw new Exception("Public key DSA của người gửi là null!");
        }
        if (!ChatCrypto.verifyFileSignature(decryptedFile, signature, senderPublicKey)) {
            decryptedFile.delete(); // Xóa file nếu chữ ký không hợp lệ
            throw new Exception("Chữ ký file không hợp lệ!");
        }

        return decryptedFile.getAbsolutePath(); // Trả về đường dẫn file đã giải mã
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
