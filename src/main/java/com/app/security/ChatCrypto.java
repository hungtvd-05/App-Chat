package com.app.security;

import com.app.model.FileData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChatCrypto {

    // Tạo khóa AES
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    // Mã hóa tin nhắn bằng AES (CBC mode)
    public static String encryptMessage(String message, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    // Giải mã tin nhắn bằng AES
    public static String decryptMessage(String encryptedBase64, SecretKey aesKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedBase64);
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    // Ký tin nhắn bằng DSA
    public static String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature dsa = Signature.getInstance("SHA256withDSA");
        dsa.initSign(privateKey);
        dsa.update(message.getBytes());
        byte[] signature = dsa.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    // Xác minh chữ ký DSA
    public static boolean verifySignature(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        byte[] signature = Base64.getDecoder().decode(signatureBase64);
        Signature dsa = Signature.getInstance("SHA256withDSA");
        dsa.initVerify(publicKey);
        dsa.update(message.getBytes());
        return dsa.verify(signature);
    }

    // Mã hóa khóa AES bằng RSA
    public static String encryptAESKey(SecretKey aesKey, PublicKey rsaPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] encrypted = cipher.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Giải mã khóa AES bằng RSA
    public static SecretKey decryptAESKey(String encryptedBase64, PrivateKey rsaPrivateKey) throws Exception {
        try {
            if (encryptedBase64 == null || encryptedBase64.isEmpty()) {
                throw new IllegalArgumentException("Chuỗi Base64 rỗng hoặc null");
            }
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new SecretKeySpec(decryptedBytes, "AES");
        } catch (IllegalArgumentException e) {
            throw new Exception("Lỗi Base64: " + e.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new Exception("Lỗi thuật toán RSA: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new Exception("Khóa RSA không hợp lệ: " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new Exception("Lỗi padding RSA: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new Exception("Kích thước khối không hợp lệ: " + e.getMessage());
        }
    }

    public static FileData encryptFile(File inputFile, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        File tempFile = File.createTempFile("encrypted_", ".enc");
        tempFile.deleteOnExit(); 

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(tempFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
            cos.flush(); 
        }
        return new FileData(tempFile, iv);
    }

    public static File decryptFile(File encryptedFile, SecretKey aesKey, byte[] iv, String outputFileName) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);

        File outputFile = new File(outputFileName);
        try (FileInputStream fis = new FileInputStream(encryptedFile); CipherInputStream cis = new CipherInputStream(fis, cipher); FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return outputFile;
    }

    public static String signFile(File inputFile, PrivateKey privateKey) throws Exception {
        Signature dsa = Signature.getInstance("SHA256withDSA");
        dsa.initSign(privateKey);
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dsa.update(buffer, 0, bytesRead);
            }
        }
        byte[] signature = dsa.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verifyFileSignature(File inputFile, String signatureBase64, PublicKey publicKey) throws Exception {
        byte[] signature = Base64.getDecoder().decode(signatureBase64);
        Signature dsa = Signature.getInstance("SHA256withDSA");
        dsa.initVerify(publicKey);
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dsa.update(buffer, 0, bytesRead);
            }
        }
        return dsa.verify(signature);
    }
}
