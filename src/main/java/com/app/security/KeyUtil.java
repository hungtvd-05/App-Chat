package com.app.security;

import com.app.dao.KeyDAO;
import com.app.model.Model_Key;
import com.app.model.Model_Save_Key;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {

    private static KeyUtil instance;
    private KeyDAO keyDAO = new KeyDAO();

    public static KeyUtil getInstance() {
        if (instance == null) {
            instance = new KeyUtil();
        }
        return instance;
    }

    // Chuyển chuỗi Base64 thành PublicKey
    public PublicKey decodePublicKey(String base64PublicKey, String algorithm) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }

    // Chuyển chuỗi Base64 thành PrivateKey
    public PrivateKey decodePrivateKey(String base64PrivateKey, String algorithm) throws Exception {
        System.out.println(base64PrivateKey);
        byte[] privateKeyBytes = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }

    // Lưu private key vào file
    public Model_Key createdKey() {

        KeyPairGenerator keyGenDSA;
        try {
            keyGenDSA = KeyPairGenerator.getInstance("DSA");
            keyGenDSA.initialize(2048);
            KeyPair DSAKeyPair = keyGenDSA.generateKeyPair();
            KeyPairGenerator keyGenRSA = KeyPairGenerator.getInstance("RSA");
            keyGenRSA.initialize(4096);
            KeyPair RSAKeyPair = keyGenRSA.generateKeyPair();

            PrivateKey dsaPrivateKey = DSAKeyPair.getPrivate();
            PrivateKey rsaPrivateKey = RSAKeyPair.getPrivate();

            PublicKey dsaPublicKey = DSAKeyPair.getPublic();
            PublicKey rsaPublicKey = RSAKeyPair.getPublic();

            return new Model_Key(
                    dsaPrivateKey,
                    rsaPrivateKey,
                    dsaPublicKey,
                    rsaPublicKey
            );

        } catch (NoSuchAlgorithmException ex) {
            System.getLogger(KeyUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return null;

    }

    public Model_Save_Key saveKey(Long userID, Model_Key key) {

        String base64PrivateKeyDSA = Base64.getEncoder().encodeToString(key.getDsa_private_key().getEncoded());
        String base64PrivateKeyRSA = Base64.getEncoder().encodeToString(key.getRsa_private_key().getEncoded());

        String base64PublicKeyDSA = Base64.getEncoder().encodeToString(key.getDsa_public_key().getEncoded());
        String base64PublicKeyRSA = Base64.getEncoder().encodeToString(key.getRsa_public_key().getEncoded());

        return keyDAO.addAndgetKey(new Model_Save_Key(userID, base64PrivateKeyDSA, base64PrivateKeyRSA, base64PublicKeyDSA, base64PublicKeyRSA)).join();
        
    }
    
    public Model_Save_Key getKeyFromDB(long userID) {
        return keyDAO.getKey(userID).join();
    }
    
    public Model_Key decodeKey(Model_Save_Key msk) {
        try {
            return new Model_Key(
                    decodePrivateKey(msk.getDsa_private_key(), "DSA"),
                    decodePrivateKey(msk.getRsa_private_key(), "RSA"),
                    decodePublicKey(msk.getDsa_public_key(), "DSA"),
                    decodePublicKey(msk.getRsa_public_key(), "RSA")
            );
        } catch (Exception ex) {
            return null;
        }
    }

    // Xác minh cặp khóa DSA
    public boolean verifyKeyPair(PrivateKey privateKey, PublicKey publicKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // Thông điệp mẫu để kiểm tra
        String testMessage = "TestKeyPairVerification";
        byte[] messageBytes = testMessage.getBytes(StandardCharsets.UTF_8);

        // Chọn thuật toán chữ ký dựa trên algorithm
        String signatureAlgorithm = algorithm.equals("RSA") ? "SHA256withRSA" : "SHA256withDSA";
        Signature signature = Signature.getInstance(signatureAlgorithm);

        // Ký thông điệp với private key
        signature.initSign(privateKey);
        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        // Xác minh chữ ký với public key
        signature.initVerify(publicKey);
        signature.update(messageBytes);
        return signature.verify(digitalSignature);
    }
}
