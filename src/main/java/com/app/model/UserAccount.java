package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

/**
 *
 * @author LENOVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    private long userId;
    private String userName;
    private String fullName;
    private String mail;
    private String phone;
    private String gender;
    private boolean status;
    private String image;
    private String pubkeyDSA; 
    private String pubkeyRSA;
    
    
    public UserAccount(Object json) {
        JSONObject obj = (JSONObject) json;
        
        try {
            this.userId = obj.getLong("userId");
            this.userName = obj.getString("userName");
            this.fullName = obj.getString("fullName");
            this.mail = obj.getString("mail");
            this.phone = obj.getString("phone");
            this.gender = obj.getString("gender");
            this.status = obj.getBoolean("status");
            this.image = obj.getString("image");
            this.pubkeyDSA = obj.getString("pubkeyDSA");
            this.pubkeyRSA = obj.getString("pubkeyRSA");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("userName", userName);
            obj.put("fullName", fullName);
            obj.put("mail", mail);
            obj.put("phone", phone);
            obj.put("gender", gender);
            obj.put("status", status);
            obj.put("image", image);
            obj.put("pubkeyDSA", pubkeyDSA);
            obj.put("pubkeyRSA", pubkeyRSA);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
    
      
}
