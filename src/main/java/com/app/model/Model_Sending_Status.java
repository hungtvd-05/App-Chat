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
 * @author ngoan
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Sending_Status {
    private String fromUsername;
    private String toUsername;
    
    public Model_Sending_Status(Object json) {
        JSONObject obj = (JSONObject) json;
        
        try {
            this.fromUsername = obj.getString("fromUsername");
            this.toUsername = obj.getString("toUsername");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("fromUsername", fromUsername);
            obj.put("toUsername", toUsername);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
    
}
