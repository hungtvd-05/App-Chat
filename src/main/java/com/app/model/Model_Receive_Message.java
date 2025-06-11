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
public class Model_Receive_Message {
    private MessageType messageType;
    private Long fromUserID;
    private String text;
    private Model_File_Sender file;
    private LocalDateTime time;

    public Model_Receive_Message(MessageType messageType, Long fromUserID, String text, LocalDateTime time) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.text = text;
        this.time = time;
    }
    

    public Model_Receive_Message(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            messageType = MessageType.toMessageType(obj.getInt("messageType"));
            fromUserID = obj.getLong("fromUserID");
            text = obj.getString("text");
            time = LocalDateTime.parse(obj.getString("time"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageType", messageType.getValue());
            json.put("fromUserID", fromUserID);
            json.put("text", text);
            json.put("time", time.toString());
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    }
} 
