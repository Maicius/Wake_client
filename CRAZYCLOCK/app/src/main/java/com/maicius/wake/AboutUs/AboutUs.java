package com.maicius.wake.AboutUs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.ScreenListener;

/**
 * Created by Maicius on 2016/12/27.
 */

public class AboutUs extends Activity{
    private Button sendButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("sss", "******************enter about us!");
        setContentView(R.layout.about_us);
        sendButton = (Button) findViewById(R.id.sendMsg);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sendMsg = Uri.parse("smsto:18030848367");
                Intent intent = new Intent(Intent.ACTION_SENDTO, sendMsg);
                startActivity(intent);
            }
        });
    }
}
