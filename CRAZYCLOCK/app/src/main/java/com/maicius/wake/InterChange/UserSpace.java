/**
 * User space
 * 检测网络状态
 * 当有网络时检查数据库是否有需要上传的数据
 * 如果有启动SyncDatabase;
 */
package com.maicius.wake.InterChange;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maicius.wake.AboutUs.AboutUs;
import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.DBmanager.ScreenOffUser;
import com.maicius.wake.DBmanager.ScreenUser;
import com.maicius.wake.DBmanager.SyncDatabase;
import com.maicius.wake.Friends.FriendsList;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.ConnectionDetector;
import com.maicius.wake.web.NetEventActivity;
import com.maicius.wake.web.ScreenListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSpace extends NetEventActivity {

    private TextView netStateView;
    private DBManager dbManager;
    SimpleDateFormat format;
    //private SyncDatabase.MyBinder myBinder;
    private ScreenListener screenListener;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            myBinder = (SyncDatabase.MyBinder)service;
//            myBinder.uploadData();
//            myBinder.downloadGreeting();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_space);
        netStateView = (TextView)findViewById(R.id.InterDetector);
        dbManager = new DBManager(this);
        screenListener = new ScreenListener(this);
        //startSyncDatabase();
        enableSleepTime();
        boolean netState = this.isNetConnect();
        if(netState){
            //Cursor sleepTable = dbManager.query("sleep");
            //if(sleepTable.getCount() !=0) {
                //Intent startIntent = new Intent(this, SyncDatabase.class);
                //startService(startIntent);
                //Intent bindIntent = new Intent(this, SyncDatabase.class);
                //bindService(bindIntent, connection, BIND_AUTO_CREATE);
            //}
            //else{
                //Intent stopIntent = new Intent(this, SyncDatabase.class);
                //stopService(stopIntent);
            //}
            netStateView.setVisibility(View.GONE);
        }else{
            netStateView.setVisibility(View.VISIBLE);
        }
        mInitUI();

    }
    public void onNetChange(int netState){
        super.onNetChange(netState);

        if(netState == ConnectionDetector.NETWORK_NONE){
            netStateView.setVisibility(View.VISIBLE);
            //Intent stopIntent = new Intent(this, SyncDatabase.class);
            //stopService(stopIntent);
            //unbindService(connection);
        }else{
            netStateView.setVisibility(View.GONE);
            Cursor sleepTable = dbManager.query("sleep");
            Toast.makeText(this, "SleepTable"+sleepTable.getCount(),Toast.LENGTH_SHORT).show();
            if(sleepTable.getCount() !=0) {
                Log.w("debug","bind start");
                //Intent bindIntent = new Intent(this, SyncDatabase.class);
                //bindService(bindIntent, connection, BIND_AUTO_CREATE);
            }
            else{
                Toast.makeText(this, "bind end",Toast.LENGTH_SHORT).show();
                Log.w("debug","service end");
            }
        }
    }


    private void mInitUI() {
        final TextView userspace_name = (TextView) findViewById(R.id.userspace_name);
        userspace_name.setText(MainActivity.s_nickname + "的空间");

        ImageView image_userInfo = (ImageView) findViewById(R.id.userInfo);
        ImageView image_getUpTime = (ImageView) findViewById(R.id.getUpTime);
        ImageView image_exit = (ImageView) findViewById(R.id.imageView2);
        ImageView image_friends = (ImageView) findViewById(R.id.friendImageView);
        ImageView image_sleepHistory = (ImageView) findViewById(R.id.sleep_history_image);
        ImageView image_phoneTime =(ImageView) findViewById(R.id.phoneIcon);
        ImageView image_exit_main = (ImageView) findViewById(R.id.exit_main);
        ImageView image_about_us = (ImageView) findViewById(R.id.aboutUs);

        image_phoneTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(UserSpace.this, ScreenOffList.class));
            }
        });
        image_userInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, UserInfo.class));
            }
        });
        image_exit_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, MainActivity.class));
            }
        });
        image_about_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, AboutUs.class));
            }
        });
        image_getUpTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserSpace.this, GetUpHistory.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", MainActivity.s_userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        image_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.s_isLogged=false;
                dbManager.deleteAppUser("sleep");
                dbManager.deleteAppUser("enableRecording");
                dbManager.deleteAppUser("getUpTime");
                dbManager.deleteAppUser("sleepTime");
                dbManager.deleteAppUser("greeting");
                dbManager.deleteAppUser("appUser");
                //StopSyncDatabse();
                disableSleepTime();

                UserSpace.this.finish();
            }
        });
        image_friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, FriendsList.class));
            }
        });

        image_sleepHistory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(UserSpace.this, SleepHistory.class));
            }
        });
    }
//    private void startSyncDatabase() {
//        Intent startIntent = new Intent(this, SyncDatabase.class);
//        startService(startIntent);
//        Intent bindIntent = new Intent(this, SyncDatabase.class);
//        bindService(bindIntent, connection, BIND_AUTO_CREATE);
//    }
//    private void StopSyncDatabse(){
//        Intent stopIntent = new Intent(this, SyncDatabase.class);
//        stopService(stopIntent);
//        unbindService(connection);
//    }
    public void enableSleepTime(){

        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                computeOffTime();
            }
            @Override
            public void onScreenOff() {

                insertCurrentTime();
            }

            @Override
            public void onUserPresent() {
                //computeOffTime();

            }
        });
    }
    public void disableSleepTime(){
        screenListener.unregisterListener();
    }
    private void insertCurrentTime(){
        format =
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date curTime = new Date(System.currentTimeMillis());
        String curSleepTime = format.format(curTime);
        ScreenUser user =
                new ScreenUser(MainActivity.s_userName, curSleepTime);
        dbManager.insertSQL(user);
        //Log.w("sss", "ScrenOff"+curSleepTime);
    }
    private void computeOffTime(){
        format =
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Cursor sleepTimeCur = dbManager.query("sleepTime");
        //Toast.makeText(this, "屏幕才关闭了", Toast.LENGTH_LONG).show();
        if (sleepTimeCur.getCount() != 0) {
            sleepTimeCur.moveToLast();
            String lastOffTime = sleepTimeCur.getString(2);
            Date curTime = new Date(System.currentTimeMillis());
            String OnTime = format.format(curTime);
            try {
                Date t1 = format.parse(OnTime);
                Date t2 = format.parse(lastOffTime);
                long diff = t1.getTime()-t2.getTime();

                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                Toast.makeText(this, "屏幕才关闭了"+minutes+"分钟", Toast.LENGTH_SHORT).show();
                ScreenOffUser user = new
                        ScreenOffUser(MainActivity.s_userName, String.valueOf(minutes));
                dbManager.insertSQL(user);
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
    }

}
