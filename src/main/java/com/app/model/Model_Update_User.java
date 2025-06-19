/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ngoan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Update_User {
    private String userName;
    private String fullName;
    private String mail;
    private String phone;
    private String gender;
    private String image;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("userName", userName);
            json.put("fullName", fullName);
            json.put("mail", mail);
            json.put("phone", phone);
            json.put("gender", gender);
            json.put("image", image);
            json.put("currentPassword", currentPassword);
            json.put("newPassword", newPassword);
            json.put("confirmNewPassword", confirmNewPassword);
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    } 
    
    
}
