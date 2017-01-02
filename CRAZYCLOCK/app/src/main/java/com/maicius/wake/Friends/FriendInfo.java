package com.maicius.wake.Friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.maicius.wake.InterChange.GetUpHistory;
import com.maicius.wake.InterChange.SleepHistory;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendInfo extends Activity {
    private ListView listView;
    private List<Map<String, Object>> listItems;
    private String nickName;
    private String phoneNum;
    private String signature;
    private AlertDialog.Builder warningDialog;
    private String returnInfo;
    private String tip;
    private ProgressDialog progressDialog;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        progressDialog = new ProgressDialog(FriendInfo.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在设置好友起床提示，请稍后...");
        progressDialog.setCancelable(false);
        //获取传来的数据
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickName");
        phoneNum = intent.getStringExtra("phoneNum");
        signature = intent.getStringExtra("signature");
        //Toast.makeText(FriendInfo.this, nickName + " " + phoneNum + " " + signature, Toast.LENGTH_SHORT).show();
        //显示好友信息
        TextView nickNameTextView = (TextView) findViewById(R.id.nickNameTextView);
        TextView telNumTextView = (TextView) findViewById(R.id.telNumTextView);
        TextView signatureTextView = (TextView) findViewById(R.id.signatureTextView);
        nickNameTextView.setText(nickName);
        telNumTextView.setText(phoneNum);
        signatureTextView.setText(signature);

        listItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> listItem = new HashMap<String, Object>();
        listItem.put("icon", R.drawable.ic_clock_alarm_on);
        listItem.put("operateName", "查看他的起床时间");
        listItems.add(listItem);
        Map<String, Object> listItem2 = new HashMap<String, Object>();
        listItem2.put("icon", R.drawable.ic_night);
        listItem2.put("operateName", "查看他的睡眠时间");
        listItems.add(listItem2);
        Map<String, Object> listItem3 = new HashMap<String, Object>();
        listItem3.put("icon", R.drawable.ic_getup_tip);
        listItem3.put("operateName", "设置他的起床提示语");
        listItems.add(listItem3);


        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.item_operate_list_item,
                new String[] {"icon", "operateName"},
                new int[] {R.id.iconImageView, R.id.operateNameTextView});
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)        //查看好友起床信息
                {
                    Intent intent = new Intent();
                    intent.setClass(FriendInfo.this, GetUpHistory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", phoneNum);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(i ==1){
                    Intent intent = new Intent();
                    intent.setClass(FriendInfo.this, SleepHistory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", phoneNum);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (i == 2) {  //设置好友起床提示语
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FriendInfo.this);
                    final EditText editText = new EditText(FriendInfo.this);
                    alertDialog.setTitle("输入提示语");
                    alertDialog.setView(editText);
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tip = editText.getText().toString();
//                            Log.v("Eric", "Tip input:" + tip);
//                            Toast.makeText(FriendInfo.this, tip, Toast.LENGTH_SHORT).show();
                            if (tip.equals("")) {
                                Toast.makeText(FriendInfo.this, "不能为空哦", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            progressDialog.show();
                            new Thread(new SetTipThread()).start();
                        }
                    });
                    alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.create().show();
                }
            }
        });

        //创建警告框
        warningDialog = new AlertDialog.Builder(this);
        warningDialog.setTitle("警告")
                .setIcon(R.drawable.ic_warning)
                .setMessage("删除好友后不能查看好友信息\n确定删除？");
        warningDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new MyThread()).start();
            }
        });
        warningDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        warningDialog.create();

        Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningDialog.show();
            }
        });
    }

    private class MyThread implements Runnable {
        @Override
        public void run() {
            returnInfo = WebService.friendOperation(MainActivity.s_userName, phoneNum, WebService.State.DeleteFriend);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (returnInfo.equals("success")) {
                        Toast.makeText(FriendInfo.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FriendInfo.this, FriendsList.class));
                        return;
                    } else if (returnInfo.equals("failed")) {
                        Toast.makeText(FriendInfo.this, "删除失败，请重试！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(FriendInfo.this, "返回值为:" + returnInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private class SetTipThread implements Runnable {
        @Override
        public void run() {
            returnInfo = WebService.setGetUpTip(MainActivity.s_userName, phoneNum, tip, WebService.State.SetGetUpTip);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if (returnInfo.equals("success")) {
                        Toast.makeText(FriendInfo.this, "设置成功！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (returnInfo.equals("failed")) {
                        Toast.makeText(FriendInfo.this, "设置失败，请重试！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(FriendInfo.this, "错误返回值为:" + returnInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
