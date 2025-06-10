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
public class Model_Login {
    private String username;
    private String password;
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("password", password);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
    
}
