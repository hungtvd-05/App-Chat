/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.model;

import com.app.service.Service;
import io.socket.client.Ack;
import io.socket.client.Socket;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author LENOVO
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_File_Sender {
    
    private Model_Send_Message message;
    private int fileID;
    private String fileExtensions;
    private File file;
    private long fileSize;
    private RandomAccessFile accFile;
    private Socket socket;
    
    public Model_File_Sender(File file, Socket socket, Model_Send_Message message) throws IOException {
        accFile = new RandomAccessFile(file, "r");
        this.file = file;
        this.socket = socket;
        this.message = message;
        fileExtensions = getExtensions(file.getName());
        fileSize = accFile.length();
    }

    public synchronized byte[] readFile() throws IOException {
        long filepointer = accFile.getFilePointer();
        if (filepointer != fileSize) {
            int max = 2000;
            long length = filepointer + max >= fileSize ? fileSize - filepointer : max;
            byte[] data = new byte[(int) length];
            accFile.read(data);
            return data;
        } else {
            return null;
        }
    }

    public void initSend() throws IOException {
        System.out.println("Init file to server and wait server response back");
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
                                sendingFile();
                            } else {
                                //  File send finish
                                Service.getInstance().fileSendFinish(Model_File_Sender.this);
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
