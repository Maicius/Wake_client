
package com.maicius.wake.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * Created by Maicius on 2016/12/23.
 * 获取屏幕状态,用以判断开始睡眠的时间
 */
public class ScreenListener {
    private Context context;
    private ScreenBroadcastReceiver screenBroadcastReceiver;
    private ScreenStateListener screenStateListener;

    public ScreenListener(Context context){
        this.context = context;
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
    }
    private class ScreenBroadcastReceiver extends BroadcastReceiver{
        private String action = null;
        public void  onReceive(Context context, Intent intent){
            action = intent.getAction();
            //开屏
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                screenStateListener.onScreenOn();
            } else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                screenStateListener.onScreenOff();
            } else if(Intent.ACTION_USER_PRESENT.equals(action)){
                screenStateListener.onUserPresent();
            }
        }
    }

    /*开启监听状态*/
    public void begin(ScreenStateListener listener){
        screenStateListener = listener;
        registerListener();
        getScreenState();
    }
    /*获取screen状态*/
    private void getScreenState(){
        PowerManager manager =
                (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        if(manager.isScreenOn()){
            if(screenStateListener !=null){
                screenStateListener.onScreenOn();
            }
        }else{
            if(screenStateListener !=null){
                screenStateListener.onScreenOff();
            }
        }
    }
    /*停止screen状态监听*/
    public void unregisterListener(){
        context.unregisterReceiver(screenBroadcastReceiver);
    }
    private void registerListener(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        context.registerReceiver(screenBroadcastReceiver, filter);
    }

    private interface ScreenStateListener{
        void onScreenOn();
        void onScreenOff();
        void onUserPresent();
    }
}
