package com.app.model;

import com.app.event.EventFileSender;
import com.app.service.Service;
import io.socket.client.Ack;
import io.socket.client.Socket;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_File_Sender {

    private Model_Send_Message message;
    private long fileID;
    private String fileExtensions;
    private File file;
    private long fileSize;
    private RandomAccessFile accFile;
    private Socket socket;
    private EventFileSender event;

    public Model_File_Sender(File file, Socket socket, Model_Send_Message message) throws IOException {
        accFile = new RandomAccessFile(file, "r");
        this.file = file;
        this.socket = socket;
        this.message = message;
        fileExtensions = getExtensions(file.getName());
        fileSize = accFile.length();
    }

    public synchronized byte[] readFile() throws IOException {
        long filePointer = accFile.getFilePointer();
        if (filePointer < fileSize) {
            int max = 1024 * 1024 * 5;
            long length = Math.min(max, fileSize - filePointer);
            byte[] data = new byte[(int) length];
            accFile.read(data);
            return data;
        }
        return null;
    }

    public void initSend() throws IOException {
        socket.emit("send_to_user", message.toJSONObject(), new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    int fileID = (int) os[0];
                    try {
                        startSend(fileID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    

    public void startSend(int fileID) throws IOException {
        this.fileID = fileID;
        if (event != null) {
            event.onStartSending();
        }
        sendingFile();
    }

    private void sendingFile() throws IOException {
        Model_Package_Sender data = new Model_Package_Sender();
        data.setFileID(fileID);
        byte[] bytes = readFile();
        if (bytes != null) {
            data.setData(bytes);
            data.setFinish(false);
        } else {
            data.setFinish(true);
            close();
        }
        socket.emit("send_file", data.toJsonObject(), new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    boolean act = (boolean) os[0];
                    if (act) {
                        try {
                            if (!data.isFinish()) {
                                if (event != null) {
                                    event.onSending(getPercentage());
                                }
                                sendingFile();
                            } else {
                                //  File send finish
                                Service.getInstance().fileSendFinish(Model_File_Sender.this);
                                if (event != null) {
                                    event.onFinish();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public double getPercentage() throws IOException {
        double percentage;
        long filePointer = accFile.getFilePointer();
        percentage = filePointer * 100 / fileSize;
        return percentage;
    }

    public void close() throws IOException {
        accFile.close();
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

}
