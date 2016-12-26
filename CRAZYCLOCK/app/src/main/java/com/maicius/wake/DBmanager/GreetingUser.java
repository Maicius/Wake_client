package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/26.
 */

public class GreetingUser {
    private String receiveUser;
    private String sendUser;
    private String greeting;
    public GreetingUser(String receiveUser, String sendUser, String greeting){
        this.receiveUser = receiveUser;
        this.sendUser = sendUser;
        this.greeting = greeting;
    }
    public String getReceiveUser(){
        return  receiveUser;
    }
    public String getSendUser(){
        return sendUser;
    }
    public String getGreeting(){
        return greeting;
    }
}
