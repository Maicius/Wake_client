package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/23.
 */

public class SleepTimeUser {
    private String username;
    private String curTime;
    public SleepTimeUser(String username, String curTime){
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
