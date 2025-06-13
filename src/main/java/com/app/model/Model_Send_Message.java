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
    private long id;
    private MessageType messageType;
    private long fromUserID;
    private long toUserID;
    private String content = "";
    private String fileExtension = "";
    private String blurHash = "";
    private int height_blur = 0;
    private int width_blur = 0 ;
    private Model_File_Sender file;
    private LocalDateTime time;
    
    public Model_Send_Message(MessageType messageType, long fromUserID, long toUserID, String content, LocalDateTime time) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.content = content;
        this.time = time;
    }
    
    
    
    public Model_Send_Message(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            id = obj.getLong("id");
            messageType = MessageType.toMessageType(obj.getInt("messageType"));
            fromUserID = obj.getLong("fromUserID");
            toUserID = obj.getLong("toUserID");
            content = obj.getString("content");
            fileExtension = obj.getString("fileExtension");
            blurHash = obj.getString("blurHash");
            width_blur = obj.getInt("width_blur");
            height_blur = obj.getInt("height_blur");
            
            time = LocalDateTime.parse(obj.getString("time"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("messageType", messageType.getValue());
            json.put("fromUserID", fromUserID);
            json.put("toUserID", toUserID);
            json.put("content", content);
            if (file != null) {
                fileExtension = file.getFileExtensions();
            }
            json.put("fileExtension", fileExtension);
            json.put("blurHash",blurHash);
            json.put("width_blur", width_blur);
            json.put("height_blur", height_blur);
            json.put("time", time.toString());
            
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    }
} 
