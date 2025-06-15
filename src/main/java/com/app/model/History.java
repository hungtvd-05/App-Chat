package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private Long fromUserID;
    private Long toUserID;
    private Long fromMessageID;
    
    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("fromUserID", fromUserID);
            obj.put("toUserID", toUserID);
            obj.put("fromMessageID", fromMessageID);
            return obj;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }
}