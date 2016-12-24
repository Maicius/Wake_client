/**
 A detector of web
 */
package com.maicius.wake.web;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector{
    public static final int NETWORK_NONE = -1;
    public static final int NETWORK_MOBILE = 0;
    public static final int NETWORK_WIFI = 1;

    public static int getNetworkState(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            if(activeNetworkInfo.getType()==(ConnectivityManager.TYPE_MOBILE)){
                return NETWORK_MOBILE;
            }
            else if(activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)){
                return NETWORK_WIFI;
            }
        }else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}

