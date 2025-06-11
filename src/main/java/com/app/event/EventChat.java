package com.app.event;

import com.app.model.Model_Receive_Message;
import com.app.model.Model_Send_Message;

public interface EventChat {
    public void sendMessage(Model_Send_Message data);
    public void reiceveMessage(Model_Receive_Message data);
}
