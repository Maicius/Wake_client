package com.maicius.wake.InterChange;

import android.app.Activity;
import android.os.Bundle;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.DBmanager.SleepTimeUser;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.ScreenListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maicius on 2016/12/22.
 */
public class SleepHistory extends Activity {
    private DBManager dbManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_history);
    }
    private void enableSleepTime(){
        dbManager = new DBManager(this);
        ScreenListener screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {
                SimpleDateFormat format =
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date curTime = new Date(System.currentTimeMillis());
                String curSleepTime = format.format(curTime);
                SleepTimeUser user =
                        new SleepTimeUser(MainActivity.s_userName, curSleepTime);
                dbManager.insertSQL(user);
            }

            @Override
            public void onUserPresent() {
                dbManager.deleteAppUser("sleepTime");
            }
        });
    }

}
