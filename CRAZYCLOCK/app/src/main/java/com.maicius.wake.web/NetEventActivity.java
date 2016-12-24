package com.maicius.wake.web;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 * Created by Maicius on 2016/12/24.
 */

public abstract class NetEventActivity extends FragmentActivity implements InternetBroadcastReceiver.NetEvent{
    public static InternetBroadcastReceiver.NetEvent event;
    private int netState;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        event = this;
        inspectNet();
    }

    public boolean inspectNet(){
        this.netState = ConnectionDetector.getNetworkState(NetEventActivity.this);
        return isNetConnect();
    }
    public void onNetChange(int netState){
        this.netState = netState;
        isNetConnect();
    }
    public boolean isNetConnect(){
        if(netState == 1)
            return true;
        else if(netState ==0)
            return true;
        else if(netState ==-1)
            return false;
        return false;
    }
}
