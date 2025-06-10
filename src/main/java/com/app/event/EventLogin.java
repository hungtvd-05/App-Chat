package com.app.event;

import com.app.model.Model_Register;

public interface EventLogin {

    public void login();

    public void register(Model_Register data, EventMessage event);

    public void goRegister();

    public void goLogin();
}
