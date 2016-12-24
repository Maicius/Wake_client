package com.maicius.wake.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by Maicius on 2016/12/24.
 */

public class InternetBroadcastReceiver extends BroadcastReceiver {

    public NetEvent event = NetEventActivity.event;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netState = ConnectionDetector.getNetworkState(context);
            event.onNetChange(netState);
        }
    }
    public interface NetEvent{
        void onNetChange(int netState);
    }
}
