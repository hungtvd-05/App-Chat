package com.app.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Key {
    
    private PrivateKey dsa_private_key;
    
    private PrivateKey rsa_private_key;
    
    private PublicKey dsa_public_key;
    
    private PublicKey rsa_public_key;
}
