/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.event;

import com.app.model.UserAccount;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public interface EventMenuLeft {
    public void newUser(List<UserAccount> users);
    public void userConnect(Long userId);
    public void userDisconnect(Long userId);
}
