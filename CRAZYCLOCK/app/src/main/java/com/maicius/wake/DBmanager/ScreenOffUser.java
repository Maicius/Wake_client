package com.maicius.wake.DBmanager;

/**
 * Created by Maicius on 2016/12/26.
 */

public class ScreenOffUser {
        private String username;
        private String screenOffTime;
        public ScreenOffUser(){

        }
        public ScreenOffUser(String username, String curTime){
            this.username = username;
            this.screenOffTime = curTime;
        }
        public String getScreenOffTime(){
            return screenOffTime;
        }
        public String getUsername(){
            return username;
        }
}
