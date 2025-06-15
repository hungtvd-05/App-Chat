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
    private Long messageID;
    private MessageType messageType;
    private long fromUserID;
    private long toUserID;
    private String encryptedContent;
    private String signature;
    private String encryptedAESKey;
    private String pubkeyDSAFromUser;
    private String text;
    private Model_Image dataImage;
    private LocalDateTime time;

    public Model_Receive_Message(MessageType messageType, long fromUserID, String text, LocalDateTime time) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.text = text;
        this.time = time;
    }
    

    public Model_Receive_Message(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            messageID = obj.getLong("messageID");
            messageType = MessageType.toMessageType(obj.getInt("messageType"));
            fromUserID = obj.getLong("fromUserID");
            toUserID = obj.getLong("toUserID");
            encryptedContent = obj.getString("encryptedContent");
            signature = obj.getString("signature");
            encryptedAESKey = obj.getString("encryptedAESKey");
            pubkeyDSAFromUser = obj.getString("pubkeyDSAFromUser");
            if (!obj.isNull("dataImage")) {
                dataImage = new Model_Image(obj.get("dataImage"));
            }
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
            json.put("encryptedContent", encryptedContent);
            json.put("signature", signature);
            json.put("encryptedAESKey", encryptedAESKey);
            json.put("pubkeyDSAFromUser", pubkeyDSAFromUser);
            if (dataImage != null) {
                json.put("dataImage", dataImage.toJsonObject());
            }
            json.put("time", time.toString());
            return json;
        } catch (JSONException e) {
             System.out.println(e);
            return null;
        }
    }
} 
