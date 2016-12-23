package com.maicius.wake.InterChange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.maicius.wake.alarmClock.R;

public class FriendInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        String phoneNum = intent.getStringExtra("phoneNum");
        String signature = intent.getStringExtra("signature");

        Toast.makeText(FriendInfo.this, nickName + " " + phoneNum + " " + signature, Toast.LENGTH_SHORT).show();
    }
}
