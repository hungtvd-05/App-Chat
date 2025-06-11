package com.app.event;

import com.app.model.UserAccount;

public interface EventMain {
    
    public void showLoading(boolean show);
    
    public void initChat();
    
    public void selectUser(UserAccount user);
    
    public void updateUser(UserAccount user);
    
}
