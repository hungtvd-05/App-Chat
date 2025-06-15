package com.app.security;

import com.app.model.Model_Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.Data;

@Data
public class Session {
    private static Session instance;
    private PrivateKey dsaPrivateKey;
    private PrivateKey rsaPrivateKey;
    private PublicKey dsaPublicKey;
    private PublicKey rsaPublicKey;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void setKey(Model_Key key) {
        this.dsaPrivateKey = key.getDsa_private_key();
        this.rsaPrivateKey = key.getRsa_private_key();
        this.dsaPublicKey = key.getDsa_public_key();
        this.rsaPublicKey = key.getRsa_public_key();
    }

}
