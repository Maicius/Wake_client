package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/24.
 */

public class SleepUser {
    private String sleepUsername;
    private long sleepTime;
    private String day;
    public SleepUser(String username, long sleepTime, String day){
        this.sleepUsername = username;
        this.sleepTime = sleepTime;
        this.day = day;
    }
    public long getSleepTime(){
        return sleepTime;
    }
    public String getDay(){
        return day;
    }
    public String getSleepUsername(){
        return sleepUsername;
    }
}
