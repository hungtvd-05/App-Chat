/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.event;

/**
 *
 * @author LENOVO
 */
public interface EventFileSender {

    public void onSending(double percentage);

    public void onStartSending();

    public void onFinish();

}
