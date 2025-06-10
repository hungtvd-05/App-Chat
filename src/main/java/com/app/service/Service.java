package com.app.service;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

public class Service {
    private static Service instance;
    private Socket client;

    private final int PORT_NUMBER = 9999;
    private final String IP = "26.73.222.249";
    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }
    private Service() {
    }
    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            client.open();
        } catch (URISyntaxException ex) {
            System.getLogger(Service.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public Socket getClient() {
        return client;
    }
}
