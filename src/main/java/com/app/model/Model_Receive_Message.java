package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Receive_Message {
    private Long fromUserID;
    private String text;

    public Model_Receive_Message(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            fromUserID = obj.getLong("fromUserID");
            text = obj.getString("text");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromUserID", fromUserID);
            json.put("text", text);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
} 
