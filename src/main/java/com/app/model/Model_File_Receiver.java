package com.app.model;

import com.app.event.EventFileReceiver;
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
    private int fileID;
    private File file;
    private long fileSize;
    private String fileExtention;
    private FileOutputStream fos;
    private Socket socket;
    private EventFileReceiver event;
    private final String PATH_FILE = "client_data/";

    public Model_File_Receiver(int fileID, Socket socket, EventFileReceiver event) {
        this.fileID = fileID;
        this.socket = socket;
        this.event = event;
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
                        fileExtention = os[0].toString();
                        if (!fileExtention.startsWith(".")) {
                            fileExtention = "." + fileExtention;
                        }
                        System.out.println("File extension: " + fileExtention);
                        fileSize = Long.parseLong(os[1].toString());
                        System.out.println("File size: " + fileSize);
                        file = new File(PATH_FILE + fileID + fileExtention);
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
                        close();
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