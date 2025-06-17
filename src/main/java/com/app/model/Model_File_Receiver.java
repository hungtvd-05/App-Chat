package com.app.model;

import com.app.event.EventFileReceiver;
import com.app.security.ChatManager;
import com.app.service.Service;
import io.socket.client.Ack;
import io.socket.client.Socket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_File_Receiver {

    private long fileID;
    private File file;
    private long fileSize;
    private String encryptedContent;
    private String signature;
    private String encryptedAESKey;
    private String pubkeyDSAFromUser;
    private String fileExtention;
    private FileOutputStream fos;
    private Socket socket;
    private EventFileReceiver event;
    private final String PATH_FILE = "client_data/";

    public Model_File_Receiver(Model_Image dataImage, Socket socket, EventFileReceiver event) {
        this.fileID = dataImage.getFileID();
        this.socket = socket;
        this.event = event;
        this.encryptedContent = dataImage.getEncryptedContent();
        this.signature = dataImage.getSignature();
        this.encryptedAESKey = dataImage.getEncryptedAESKey();
        this.pubkeyDSAFromUser = dataImage.getPubkeyDSAFromUser();
        this.fileExtention = dataImage.getFileExtension();

        System.out.println("Model_File_Receiver initialized:");
        System.out.println("fileID: " + fileID);
        System.out.println("encryptedContent: " + encryptedContent);
        System.out.println("signature: " + signature);
        System.out.println("encryptedAESKey: " + encryptedAESKey);
        System.out.println("pubkeyDSAFromUser: " + pubkeyDSAFromUser);
        System.out.println("fileExtention: " + fileExtention);

        if (encryptedContent == null || encryptedContent.equals("")
                || signature == null || signature.equals("")
                || encryptedAESKey == null || encryptedAESKey.equals("")
                || pubkeyDSAFromUser == null || pubkeyDSAFromUser.equals("")) {
            socket.emit("get_message_by_id", fileID, new Ack() {
                @Override
                public void call(Object... os) {
                    System.out.println("yeu cau duoc tra ve");
                    Model_Send_Message msm = new Model_Send_Message(os[0]);
                    System.out.println(msm);
                    encryptedContent = msm.getEncryptedContent();
                    signature = msm.getSignature();
                    encryptedAESKey = msm.getEncryptedAESKey();
                    pubkeyDSAFromUser = msm.getPubkeyDSAFromUser();
                }
            });
        }

    }

    public void initReceive() {
        socket.emit("get_file", fileID, new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    try {
                        File dir = new File(PATH_FILE);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
//                        fileExtention = os[0].toString();
                        if (!fileExtention.startsWith(".")) {
                            fileExtention = "." + fileExtention;
                        }
                        fileSize = Long.parseLong(os[1].toString());
                        System.out.println("File size: " + fileSize);
                        file = File.createTempFile("encrypted_", ".enc");
                        file.deleteOnExit();

                        fos = new FileOutputStream(file);
                        event.onStartReceiving();
                        startSaveFile();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        try {
                            close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void startSaveFile() throws IOException, JSONException {
        Model_Request_File data = new Model_Request_File(fileID, fos.getChannel().position());
        socket.emit("reques_file", data.toJsonObject(), new Ack() {
            @Override
            public void call(Object... os) {
                try {
                    if (os.length > 0 && os[0] instanceof String && !os[0].equals("eof") && !os[0].equals("error")) {
                        String base64Chunk = (String) os[0];
                        byte[] b = Base64.getDecoder().decode(base64Chunk);
                        System.out.println("Received Base64 chunk, decoded size: " + b.length);
                        writeFile(b);
                        event.onReceiving(getPercentage());
                        startSaveFile();
                    } else if (os.length > 0 && os[0].equals("eof")) {
                        fos.flush();
                        close();

                        ChatManager.getInstance().receiveFile(file, fileID, fileExtention, encryptedContent, signature, encryptedAESKey, pubkeyDSAFromUser);

                        file.delete();
                        event.onFinish(new File(PATH_FILE + fileID + fileExtention));
                        Service.getInstance().fileReceiveFinish(Model_File_Receiver.this);
                    } else if (os.length > 0 && os[0].equals("error")) {
                        System.err.println("Server error: " + (os.length > 1 ? os[1] : "Unknown"));
                        close();
                        event.onFinish(null);
                    } else {
                        System.err.println("Unexpected response from server");
                        close();
                        event.onFinish(null);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    try {
                        close();
                        event.onFinish(null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    System.getLogger(Model_File_Receiver.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        });
    }

    private synchronized void writeFile(byte[] data) throws IOException {
        fos.write(data);
        long position = fos.getChannel().position(); // Sửa lỗi: dùng position() thay getPosition()
        System.out.println("Written chunk at position: " + position + ", size: " + data.length);
    }

    public double getPercentage() throws IOException {
        return (double) fos.getChannel().position() * 100 / fileSize;
    }

    public void close() throws IOException {
        if (fos != null) {
            fos.close();
            fos = null; // Đặt lại để tránh sử dụng sau khi đóng
        }
        System.out.println("File size on disk: " + file.length() + ", expected: " + fileSize);
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                System.err.println("Invalid image file: " + file.getAbsolutePath());
            } else {
                System.out.println("Image file is valid: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error reading image: " + e.getMessage());
        }
    }
}
