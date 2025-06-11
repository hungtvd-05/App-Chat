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
    private Model_File_Sender file;
    private LocalDateTime time;
    
    public Model_Send_Message(MessageType messageType, Long fromUserID, Long toUserID, String text, LocalDateTime time) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.text = text;
        this.time = time;
    }
    
    
    
    public Model_Send_Message(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            messageType = MessageType.toMessageType(obj.getInt("messageType"));
            fromUserID = obj.getLong("fromUserID");
            toUserID = obj.getLong("toUserID");
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
            json.put("toUserID", toUserID);
            json.put("text", text);
            json.put("time", time.toString());
            if (messageType == MessageType.FILE || messageType == MessageType.IMAGE) {
                json.put("text", file.getFileExtensions());
            } else {
                json.put("text", text);
            }
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    }
} 
