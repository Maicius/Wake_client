package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.DBmanager.GetUpUser;
import com.maicius.wake.DBmanager.GreetingUser;
import com.maicius.wake.DBmanager.ScreenUser;
import com.maicius.wake.DBmanager.SleepUser;
import com.maicius.wake.DBmanager.SyncDatabase;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.web.ConnectionDetector;
import com.maicius.wake.web.WebService;

/**
 * Created by Maicius on 2016/6/14.
 */
public class Notification extends Activity {

    private ProgressDialog proDialog;
    private DBManager dbManager;
    String greeting, greeting_sender;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        Cursor c = dbManager.query("greeting");
        if(c.getCount()!=0) {
            c.moveToLast();
            greeting = c.getString(3);
            greeting_sender ="From:"+ c.getString(2);
        }else{
            greeting = "喂，起床了吗？";
            greeting_sender = "温馨提醒";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(Notification.this);
        dialog.setTitle(greeting_sender).setMessage(greeting);

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new MyThread()).start();
                Notification.this.finish();
            }
        });

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //创建子线程
                new Thread(new MyThread()).start();
                Notification.this.finish();
            }
        });


        dialog.create().show();
    }

    public class MyThread implements Runnable {
        public void run() {
            Calendar currentTime = Calendar.getInstance();
            String info="";
            if(ConnectionDetector.getNetworkState(Notification.this) !=-1) {
                info = WebService.executeHttpGet
                        (currentTime.getTimeInMillis(), WebService.State.GetUpTime);
            }else{
                GetUpUser getUpUser = new
                        GetUpUser(MainActivity.s_userName, String.valueOf(currentTime.getTimeInMillis()));
                dbManager.insertSQL(getUpUser);
            }

            computeTimeDiff();
            proDialog.dismiss();
        }
    }

    private void computeTimeDiff() {
        String sleepTime;
        DBManager dbManager = new DBManager(this);
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Cursor sleepTimeCur = dbManager.query("sleepTime");
            if (sleepTimeCur.getCount() != 0) {
                sleepTimeCur.moveToLast();//用户最后一次关闭屏幕时间
                sleepTime = sleepTimeCur.getString(2);
            }
            else{
                Calendar currentTime = Calendar.getInstance();
                currentTime.add(Calendar.DAY_OF_MONTH, -1);
                Date date = currentTime.getTime();
                date.setHours(21);
                date.setMinutes(10);
                date.setSeconds(10);

                sleepTime = format.format(date);
            }
            Date curTime = new Date(System.currentTimeMillis());
            String getUptime = format.format(curTime);
            Date t1=format.parse(getUptime);
            Date t2 = format.parse(sleepTime);
            long diff = t1.getTime()-t2.getTime();

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            //long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            if(ConnectionDetector.getNetworkState(Notification.this)!=-1) {
                String info = WebService.executeHttpGet(hours,
                        WebService.State.SleepTime);
            }
            else {
                SleepUser sleepUser = new SleepUser(MainActivity.s_userName, hours);
                dbManager.insertSQL(sleepUser);
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
}
