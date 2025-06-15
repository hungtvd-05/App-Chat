package com.app.service;

import com.app.event.EventFileReceiver;
import com.app.event.PublicEvent;
import com.app.model.Model_File_Receiver;
import com.app.model.Model_File_Sender;
import com.app.model.Model_Receive_Message;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.model.UserAccount;
import com.app.security.ChatManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class Service {

    private static Service instance;
    @Getter
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    @Getter
    @Setter
    private UserAccount userAccount;
    private List<Model_File_Sender> fileSender;
    private List<Model_File_Receiver> fileReceiver;

    public Service() {
        fileSender = new ArrayList<>();
        fileReceiver = new ArrayList<>();
    }

    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            client.on("list_user", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    List<UserAccount> users = new ArrayList<>();
                    for (Object o : os) {
                        UserAccount user = new UserAccount(o);
                        if (user.getUserId() != userAccount.getUserId()) {
                            users.add(user);
                        }
                    }
                    PublicEvent.getInstance().getEventMenuLeft().newUser(users);
                }
            });
            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Long userId = Long.valueOf(os[0].toString());
                    boolean status = (boolean) os[1];
                    if (status) {
                        System.out.println(os[2].toString());
                        System.out.println(os[3].toString());
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userId, os[2].toString(), os[3].toString());
                    } else {
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userId);
                    }

                }
            });
            client.on("receive_ms", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Receive_Message message = new Model_Receive_Message(os[0]);
                    
                    try {
                        message.setText(ChatManager.getInstance().receiveMessage(message));
                    } catch (Exception ex) {
                        message.setText("");
                    }
                    PublicEvent.getInstance().getEventChat().reiceveMessage(message);
                }
            });
            
            client.open();
        } catch (URISyntaxException ex) {
            System.getLogger(Service.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public Model_File_Sender addFile(File file, Model_Send_Message message) throws IOException {
        Model_File_Sender data = new Model_File_Sender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }

    public void addFileReceiver(long fileID, EventFileReceiver event) throws IOException {
        Model_File_Receiver data = new Model_File_Receiver(fileID, client, event);
        fileReceiver.add(data);
        if (fileReceiver.size() == 1) {
            data.initReceive();
        }
    }

    public void fileSendFinish(Model_File_Sender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            fileSender.get(0).initSend();
        }
    }

    public void fileReceiveFinish(Model_File_Receiver data) throws IOException {
        fileReceiver.remove(data);
        if (!fileReceiver.isEmpty()) {
            fileReceiver.get(0).initReceive();
        }
    }

}
