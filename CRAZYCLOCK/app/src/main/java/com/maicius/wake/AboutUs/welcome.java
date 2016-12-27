package com.maicius.wake.AboutUs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;

/**
 * Created by Maicius on 2016/12/27.
 */

public class welcome extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setClass(welcome.this, MainActivity.class);
                startActivity(intent);
                int VERSION = Integer.parseInt(android.os.Build.VERSION.SDK);
                if (VERSION >= 5) {
                    welcome.this.overridePendingTransition(
                            R.layout.alpha_in, R.layout.alpha_out);
                }
                finish();
            }
        }.start();
    }

}
