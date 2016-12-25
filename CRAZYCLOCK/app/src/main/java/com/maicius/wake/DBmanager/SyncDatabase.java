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
import android.util.Log;
import android.widget.Toast;

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
    private Cursor c;
    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(SyncDatabase.this, "我在后台偷流量了", Toast.LENGTH_SHORT).show();
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
    public class MyBinder extends Binder {
        public void uploadData() {
            Toast.makeText(SyncDatabase.this, "我还在后台偷流量", Toast.LENGTH_SHORT).show();
            dbManager = new DBManager(SyncDatabase.this);
            c = dbManager.query("sleep");
            c.moveToFirst();
            if(c.getCount() != 0){
                Log.w("Debug","同步数据中");
                final long sleep = Long.parseLong(c.getString(2));
                returnInfo = WebService.executeHttpGet(sleep,
                        WebService.State.SleepTime);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (returnInfo.equals("success")) {
                            dbManager.deleteData("sleep", "sleep", c.getString(2));
                            c.moveToNext();
                        }else {
                            //Debug
                            Toast.makeText(SyncDatabase.this, "返回值:"
                                    + returnInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
