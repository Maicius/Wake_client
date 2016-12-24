package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/24.
 */

public class SleepUser {
    private String sleepUsername;
    private long sleepTime;
    public SleepUser(String username, long sleepTime){
        this.sleepUsername = username;
        this.sleepTime = sleepTime;
    }
    public long getSleepTime(){
        return sleepTime;
    }
    public String getSleepUsername(){
        return sleepUsername;
    }
}
