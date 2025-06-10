/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
}
