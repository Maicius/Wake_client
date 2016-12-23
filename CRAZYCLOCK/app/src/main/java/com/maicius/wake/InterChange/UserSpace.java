/**
 * User space
 */
package com.maicius.wake.InterChange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.DBmanager.*;

public class UserSpace extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("sss", "******************enter user space!");
        setContentView(R.layout.user_space);

        mInitUI();

    }

    private void mInitUI() {
        TextView userspace_name = (TextView) findViewById(R.id.userspace_name);
        userspace_name.setText(MainActivity.s_nickname + "的空间");

        ImageView image_userInfo = (ImageView) findViewById(R.id.userInfo);
        ImageView image_getUpTime = (ImageView) findViewById(R.id.getUpTime);
        ImageView image_exit = (ImageView) findViewById(R.id.exit);
        ImageView image_friends = (ImageView) findViewById(R.id.friendImageView);
        ImageView image_sleepHistory = (ImageView) findViewById(R.id.sleep_history_image);
        image_userInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, UserInfo.class));
            }
        });
        image_getUpTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserSpace.this, GetUpHistory.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", MainActivity.s_userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        image_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.s_isLogged=false;
                DBManager dbManager = new DBManager(UserSpace.this);
                dbManager.deleteAppUser("sqlUser");
                UserSpace.this.finish();
            }
        });
        image_friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(UserSpace.this, FriendsList.class));
            }
        });

        image_sleepHistory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(UserSpace.this, SleepHistory.class));
            }
        });
    }

}
