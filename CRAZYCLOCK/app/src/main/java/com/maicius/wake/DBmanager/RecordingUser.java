package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/25.
 */

public class RecordingUser {
    private String username;
    private int enableSleep;
    public RecordingUser(String username, int enableSleep){
        this.username =username;
        this.enableSleep = enableSleep;
    }
    public String getUsername(){
        return username;
    }
    public int getEnableSleep(){
        return enableSleep;
    }
}
