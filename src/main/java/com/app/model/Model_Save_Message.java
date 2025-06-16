package com.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Model_Save_Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "mesage_id", unique = true)
    private long mesage_id;
    @Column(name = "message_type")
    private int messageType;
    @Column(name = "from_user_id")
    private long fromUserID;
    @Column(name = "to_user_id")
    private long toUserID;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "file_extension")
    private String fileExtension = "";
    @Column(name = "blurhash")
    private String blurHash = "";
    @Column(name = "height_blur")
    private int height_blur = 0;
    @Column(name = "width_blur")
    private int width_blur = 0 ;
    @Column(name = "created_at")
    private LocalDateTime time;

    public Model_Save_Message(int messageType, long fromUserID, long toUserID, String content, LocalDateTime time) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.content = content;
        this.time = time;
    }

    public Model_Save_Message(long mesage_id, int messageType, long fromUserID, long toUserID, String content, LocalDateTime time) {
        this.mesage_id = mesage_id;
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.content = content;
        this.time = time;
    }
    
    public Model_Save_Message(long mesage_id, int messageType, long fromUserID, long toUserID, String fileExtension, String blurHash, int height_blur, int width_blur, LocalDateTime time) {
        this.mesage_id = mesage_id;
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.content = "";
        this.fileExtension = fileExtension;
        this.blurHash = blurHash;
        this.height_blur = height_blur;
        this.width_blur = width_blur;
        this.time = time;
    }

}
