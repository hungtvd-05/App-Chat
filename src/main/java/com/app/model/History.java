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
public class History {
    private Long fromUserID;
    private Long toUserID;
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("fromUserID", fromUserID);
            obj.put("toUserID", toUserID);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
}