package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import com.maicius.wake.web.WebService;

/**
 * Created by Maicius on 2016/6/14.
 */
public class Notification extends Activity {

    private ProgressDialog proDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("maicius", "Notification Entered******************");
        AlertDialog.Builder dialog = new AlertDialog.Builder(Notification.this);
        dialog.setTitle("温馨提醒").setMessage("你真的醒了吗？");

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Notification.this.finish();
            }
        });

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                proDialog = new ProgressDialog(Notification.this);
                proDialog.setTitle("提示");
                proDialog.setMessage("正在上传起床时间，请稍后...");
                proDialog.setCancelable(false);
                proDialog.show();
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

            String info = WebService.executeHttpGet(currentTime.getTimeInMillis(), WebService.State.GetUpTime);
            Log.v("sss", info + "***");
            proDialog.dismiss();
        }
    }
}
