/**
 A detector of web
 */
package com.maicius.wake.web;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector{
    private Context context;
    private boolean InternetConnect;
    public ConnectionDetector(Context context){
        this.context = context;
        isConnectingToInternet();
    }
    public void isConnectingToInternet(){

        // Context context = context.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        InternetConnect= true;
                    }

        }
        else
            InternetConnect = false;
    }
    public boolean getInternetConnect(){
        return InternetConnect;
    }
}
