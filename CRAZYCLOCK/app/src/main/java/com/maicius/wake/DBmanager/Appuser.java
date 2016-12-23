package com.maicius.wake.DBmanager;
/**
 * Created by Maicius on 2016/12/21.
 */
public class AppUser {
    private String sqlusername;
    private String sqlpassword;
    private String sqlnickname;
    public AppUser(){}
    public AppUser(String username, String password, String nickname){
        this.sqlusername = username;
        this.sqlpassword = password;
        this.sqlnickname = nickname;
    }
    public String getSqlusername(){
        return sqlusername;
    }
    public String getSqlnickname(){
        return sqlnickname;
    }
    public String getSqlpassword(){
        return sqlpassword;
    }
}