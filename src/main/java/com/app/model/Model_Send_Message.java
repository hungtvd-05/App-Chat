package com.app.model;

import com.app.enums.MessageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Send_Message {
    private MessageType messageType;
    private Long fromUserID;
    private Long toUserID;
    private String text;
    private LocalDateTime time;
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageType", messageType.getValue());
            json.put("fromUserID", fromUserID);
            json.put("toUserID", toUserID);
            json.put("text", text);
            json.put("time", time.toString());
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    }
} 
