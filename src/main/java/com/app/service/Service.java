package com.app.service;

import com.app.event.PublicEvent;
import com.app.model.UserAccount;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Service {
    private static Service instance;
    @Getter
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "26.73.222.249";
    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }
    
    @Getter
    @Setter
    private UserAccount userAccount;
    
    
    
    private Service() {
    }
    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            client.on("list_user", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    List<UserAccount> users = new ArrayList<>();
                    for (Object o: os) {
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
                    boolean status = (boolean)os[1];
                    if (status) {
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userId);
                    } else {
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userId);
                    }
                    
                }
            });
            client.open();
        } catch (URISyntaxException ex) {
            System.getLogger(Service.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
