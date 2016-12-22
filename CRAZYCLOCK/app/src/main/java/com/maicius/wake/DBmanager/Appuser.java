package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/21.
 */
public class AppUser {
    public String sqlusername;
    public String sqlpassword;
    public String sqlnickname;
    public AppUser(){}
    public AppUser(String username, String password, String nickname){
        this.sqlusername = username;
        this.sqlpassword = password;
        this.sqlnickname = nickname;
    }
}
