package com.app.event;

public class PublicEvent {

    private static PublicEvent instance;
    private EventImageView eventImageView;
    private EventChat eventChat;
    private EventLogin eventLogin;
    
    public static PublicEvent getInstance() {
        if (instance == null) {
            instance = new PublicEvent();
        }
        return instance;
    }

    private PublicEvent() {

    }
    
    public EventLogin getEventLogin() {
        return eventLogin;
    }

    public void setEventLogin(EventLogin eventLogin) {
        this.eventLogin = eventLogin;
    }

    public void addEventImageView(EventImageView event) {
        this.eventImageView = event;
    }
    
    public void addEventChat(EventChat event) {
        this.eventChat = event;
    }

    public EventImageView getEventImageView() {
        return eventImageView;
    }
    
    public EventChat getEventChat() {
        return eventChat;
    }
}
