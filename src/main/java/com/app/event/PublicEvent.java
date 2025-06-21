package com.app.event;

public class PublicEvent {

    private static PublicEvent instance;
    private EventImageView eventImageView;
    private EventChat eventChat;
    private EventLogin eventLogin;
    private EventMain eventMain;
    private EventMenuLeft eventMenuLeft;
    private EventMenuRight eventMenuRight;
    private EventChatBody eventChatBody;
    
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

    public EventMain getEventMain() {
        return eventMain;
    }

    public void setEventMain(EventMain eventMain) {
        this.eventMain = eventMain;
    }

    public EventMenuLeft getEventMenuLeft() {
        return eventMenuLeft;
    }

    public void setEventMenuLeft(EventMenuLeft eventMenuLeft) {
        this.eventMenuLeft = eventMenuLeft;
    }

    public EventMenuRight getEventMenuRight() {
        return eventMenuRight;
    }

    public void setEventMenuRight(EventMenuRight eventMenuRight) {
        this.eventMenuRight = eventMenuRight;
    }

    public EventChatBody getEventChatBody() {
        return eventChatBody;
    }

    public void setEventChatBody(EventChatBody eventChatBody) {
        this.eventChatBody = eventChatBody;
    }
    
    
    
    
    
    
}
