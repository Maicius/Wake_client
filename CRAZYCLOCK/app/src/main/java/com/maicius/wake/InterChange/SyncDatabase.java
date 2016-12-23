package com.maicius.wake.InterChange;
/**
 * 将本地数据上传到云端（本地只做数据的暂时存储）
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Maicius on 2016/12/24.
 */

public class SyncDatabase extends Service{
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
        return null;
    }
}
