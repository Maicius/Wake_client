package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/23.
 */

public class ScreenUser {
    private String username;
    private String curTime;
    public ScreenUser(){

    }
    public ScreenUser(String username, String curTime){
        this.username = username;
        this.curTime = curTime;
    }
    public String getCurTime(){
        return curTime;
    }
    public String getUsername(){
        return username;
    }
}
