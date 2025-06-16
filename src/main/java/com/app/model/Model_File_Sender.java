package com.app.model;

import com.app.event.EventFileSender;
import com.app.security.ChatManager;
import com.app.service.Service;
import com.app.swing.blurHash.BlurHash;
import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_File_Sender {

    private Model_Send_Message message;
    private UserAccount toUser;
    private long fileID;
    private String fileExtensions;
    private File file;
    private File encryptedFile;
    private long fileSize;
    private RandomAccessFile accFile;
    private Socket socket;
    private EventFileSender event;
    private long sentBytes = 0; // Theo dõi số byte đã gửi thành công
    private static final int MAX_RETRIES = 3;
    private volatile boolean isSending = false;

    public Model_File_Sender(File file, Socket socket, Model_Send_Message message, UserAccount toUser) throws IOException {
        this.file = file;
        this.socket = socket;
        this.message = message;
        fileExtensions = getExtensions(file.getName());
        this.toUser = toUser;
    }

    public synchronized byte[] readFile() throws IOException {
        long filePointer = accFile.getFilePointer();
        if (filePointer < fileSize) {
            int max = 512 * 1024; // 512KB
            long length = Math.min(max, fileSize - filePointer);
            byte[] data = new byte[(int) length];
            accFile.read(data);
            return data;
        }
        return null;
    }

    public synchronized void initSend() {
        if (isSending) {
            return;
        }
        if (event == null) {
            // Trì hoãn initSend, thử lại sau 100ms
            new Thread(() -> {
                try {
                    int attempts = 0;
                    while (event == null && attempts < 10) { // Thử 10 lần (1s)
                        Thread.sleep(100);
                        attempts++;
                    }
                    if (event == null) {
                        handleError("EventFileSender not set");
                        return;
                    }
                    startSendInternal();
                } catch (InterruptedException | IOException e) {
                    handleError("Error in delayed initSend: " + e.getMessage());
                } catch (Exception ex) {
                    System.getLogger(Model_File_Sender.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }).start();
            return;
        }
        try {
            startSendInternal();
        } catch (IOException e) {
            handleError("Error starting send: " + e.getMessage());
        } catch (Exception ex) {
            System.getLogger(Model_File_Sender.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    

    private void startSendInternal() throws IOException, Exception {
        
        encryptedFile = ChatManager.getInstance().sendFile(message, toUser, file);
        accFile = new RandomAccessFile(encryptedFile, "r");
        fileSize = accFile.length();
        
        convertFileToBlurHash(file, message);
        message.setFileExtension(fileExtensions);
        
        isSending = true;
        sentBytes = 0;
        socket.emit("send_to_user", message.toJSONObject(), new Ack() {
            @Override
            public void call(Object... args) {
                if (args.length > 0) {
                    fileID = Long.parseLong(args[0].toString());
                    try {
                        File dir = new File("client_data");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File destFile = new File("client_data/" + fileID + fileExtensions);
                        Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        startSend();
                    } catch (IOException e) {
                        handleError("Error starting send: " + e.getMessage());
                    }
                } else {
                    handleError("No fileID received from server");
                }
            }
        });
    }

    private void startSend() throws IOException {
        sentBytes = 0;
        if (event != null) {
            event.onStartSending();
        }
        sendingFile(0);
    }

    private void sendingFile(int retryCount) {
        try {
            Model_Package_Sender data = new Model_Package_Sender();
            data.setFileID(fileID);
            data.setOffset(accFile.getFilePointer());
            byte[] bytes = readFile();
            if (bytes != null) {
                data.setData(Base64.getEncoder().encodeToString(bytes));
                data.setFinish(false);
            } else {
                data.setFinish(true);
            }

            // Cập nhật giao diện trước khi gửi
            if (event != null && !data.isFinish()) {
                event.onSending(getPercentage());
            }

            socket.emit("send_file", data.toJsonObject(), new Ack() {
                @Override
                public void call(Object... args) {
                    if (args.length > 0) {
                        boolean act = (boolean) args[0];
                        System.out.println("Ack: " + act + ", sentBytes: " + sentBytes + ", percentage: " + getPercentage());
                        if (act) {
                            try {
                                if (!data.isFinish()) {
                                    sentBytes += bytes != null ? bytes.length : 0;
                                    sendingFile(0); // Reset retry
                                } else {
                                    close();
                                    Service.getInstance().fileSendFinish(Model_File_Sender.this);
                                    if (event != null) {
                                        event.onFinish();
                                    }

                                }
                            } catch (IOException e) {
                                handleError("Error processing chunk: " + e.getMessage());
                            }
                        } else if (retryCount < MAX_RETRIES) {
                            System.err.println("Server failed to receive chunk, retry " + (retryCount + 1));
                            sendingFile(retryCount + 1);
                        } else {
                            handleError("Max retries reached for chunk at offset " + data.getOffset());
                        }
                    }
                }
            });
            socket.on("file_completed", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    if (os.length > 0) {
                        Model_Send_Message msm = new Model_Send_Message(os[0]);
                        System.out.println("msm id " + msm.getId());
                        ChatManager.getInstance().saveMessage(
                                new Model_Save_Message(
                                        msm.getId(),
                                        msm.getMessageType().getValue(),
                                        msm.getFromUserID(),
                                        msm.getToUserID(),
                                        msm.getFileExtension(),
                                        msm.getBlurHash(),
                                        msm.getHeight_blur(),
                                        msm.getWidth_blur(),
                                        msm.getTime()
                                )
                        );
                    }
                }
            });
        } catch (IOException e) {
            handleError("Error sending chunk: " + e.getMessage());
        }
    }

    public double getPercentage() {
//        System.out.println("dang cap nhat");
        return ((double) sentBytes * 100) / fileSize;
    }

    public void close() {
        try {
            if (accFile != null) {
                accFile.close();
                accFile = null;
            }
        } catch (IOException e) {
            System.err.println("Error closing file: " + e.getMessage());
        }
    }

    private void handleError(String message) {
        System.err.println(message);
        close();
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    public void addEvent(EventFileSender event) {
        this.event = event;
    }
    
    private void convertFileToBlurHash(File file, Model_Send_Message dataImage) throws IOException {
        BufferedImage img = ImageIO.read(file);
        Dimension size = getAutoSize(new Dimension(img.getWidth(), img.getHeight()), new Dimension(200, 200));
        BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(img, 0, 0, size.width, size.height, null);
        String blurhash = BlurHash.encode(newImage);
        dataImage.setWidth_blur(size.width);
        dataImage.setHeight_blur(size.height);
        dataImage.setBlurHash(blurhash);
    }
    
    private Dimension getAutoSize(Dimension fromSize, Dimension toSize) {
        int w = toSize.width;
        int h = toSize.height;
        int iw = fromSize.width;
        int ih = fromSize.height;
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        return new Dimension(width, height);
    }
}
