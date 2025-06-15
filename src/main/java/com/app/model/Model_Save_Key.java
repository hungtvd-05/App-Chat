package com.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "keys")
public class Model_Save_Key {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id", unique = true)
    private long userID;
    
    @Column(name = "dsa_private_key", columnDefinition = "TEXT")
    private String dsa_private_key;
    
    @Column(name = "rsa_private_key", columnDefinition = "TEXT")
    private String rsa_private_key;
    
    @Column(name = "dsa_public_key", columnDefinition = "TEXT")
    private String dsa_public_key;
    
    @Column(name = "rsa_public_key", columnDefinition = "TEXT")
    private String rsa_public_key;

    public Model_Save_Key(long userID, String dsa_private_key, String rsa_private_key, String dsa_public_key, String rsa_public_key) {
        this.userID = userID;
        this.dsa_private_key = dsa_private_key;
        this.rsa_private_key = rsa_private_key;
        this.dsa_public_key = dsa_public_key;
        this.rsa_public_key = rsa_public_key;
    }
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userID", userID);
            obj.put("dsa_public_key", dsa_public_key);
            obj.put("rsa_public_key", rsa_public_key);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
    
}
