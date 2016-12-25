package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/25.
 */

public class GetUpUser {
    private String username;
    private String getUpTime;
    public GetUpUser(String username, String getUpTime){
        this.username = username;
        this.getUpTime = getUpTime;
    }
    public String getUsername(){
        return username;
    }
    public String getGetUpTime(){
        return getUpTime;
    }

}
