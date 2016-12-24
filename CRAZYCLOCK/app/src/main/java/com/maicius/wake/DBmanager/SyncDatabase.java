package com.maicius.wake.DBmanager;
/**
 * 将本地数据上传到云端（本地只做数据的暂时存储）
 */

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.maicius.wake.InterChange.FriendInfo;
import com.maicius.wake.web.ConnectionDetector;
import com.maicius.wake.web.WebService;

/**
 * Created by Maicius on 2016/12/24.
 */

public class SyncDatabase extends Service{
    private DBManager dbManager;
    private String returnInfo;
    private static Handler handler = new Handler();
    private MyBinder binder = new MyBinder();
    @Override
    public void onCreate(){
        super.onCreate();

    }
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy(){
        super.onDestroy();
    }
    public IBinder onBind(Intent intent){
        return binder;
    }
    class MyBinder extends Binder {
        public void uploadSleepTime() {
            dbManager = new DBManager(SyncDatabase.this);
            int netState = ConnectionDetector.getNetworkState(SyncDatabase.this);
            if (netState == 0 || netState == 1) {
                Cursor c = dbManager.query("sleep");
                c.moveToFirst();
                final long sleep = Long.parseLong(c.getString(2));
                returnInfo = WebService.executeHttpGet(sleep,
                        WebService.State.SleepTime);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (returnInfo.equals("success")) {
                            dbManager.deleteAppUser("sleep");
                            return;
                        } else if (returnInfo.equals("failed")) {
                            return;
                        } else {
                            //Debug
                            Toast.makeText(SyncDatabase.this, "返回值:"
                                    + returnInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {

            }
        }
    }
}
