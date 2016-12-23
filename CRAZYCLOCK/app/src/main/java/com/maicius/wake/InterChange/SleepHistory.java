package com.maicius.wake.InterChange;
/**
 * 记录睡眠长度
 * 监测屏幕的广播信息，记录每次屏幕关闭的时间并存储在本地数据库里，
 * 每当屏幕解锁后刷新该时间
 * 并在有网络的情况下将该时间上传到云端数据库
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

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
    private Switch enable_record;
    ScreenListener screenListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_history);
        enable_record = (Switch)findViewById(R.id.enable_record);
        dbManager = new DBManager(this);
        screenListener = new ScreenListener(this);
        enable_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    enableSleepTime();
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SleepHistory.this);
                    alertDialog.setTitle("提醒").setMessage("关闭后将不再记录您的睡眠信息");
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            disableSleepTime();
                        }
                    });
                    alertDialog.create().show();
                }
            }
        });

    }
    private void enableSleepTime(){

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
    private void disableSleepTime(){
        screenListener.unregisterListener();
    }

}
