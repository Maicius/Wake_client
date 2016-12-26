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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FriendsList extends Activity {
    private ListView friendsListView;
    private ProgressDialog dialog;
    private static Handler handler = new Handler();
    private String returnInfo;
    private List<Map<String, Object>> listItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        dialog = new ProgressDialog(FriendsList.this);
        dialog.setTitle("提示");
        dialog.setMessage("正在获取好友列表，请稍后...");
        dialog.setCancelable(false);
        dialog.show();

        listItems = new ArrayList<Map<String, Object>>();

        Button addFriendBtn = (Button) findViewById(R.id.addFriendBtn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsList.this, AddFriend.class));
            }
        });

        //创建一个新的线程，用来获取好友信息
        new Thread(new MyThread()).start();
    }

    private void initList() {

        StringTokenizer st = new StringTokenizer(returnInfo, "#");
        int tokenNum = st.countTokens();
        //Toast.makeText(FriendsList.this, "" + tokenNum, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < tokenNum / 3; i++)
        {
            String userName = st.nextToken();
            String nickName = st.nextToken();
            String signature = st.nextToken();
            //Toast.makeText(FriendsList.this, "" + userName + nickName + signature, Toast.LENGTH_SHORT).show();

            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("portrait", R.drawable.ic_portrait);
            listItem.put("nickName", nickName);
            listItem.put("phoneNum", userName);
            listItem.put("signature", signature);
            listItems.add(listItem);
        }

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,                         //填充的Items列表
                R.layout.item_friendheader,                                                      //填充的界面
                new String[] {"portrait", "nickName", "phoneNum", "signature"},                  //哪些key对应的value来生成列表项
                new int[] {R.id.portraitImageView, R.id.nickNameTextView, R.id.phoneNumTextView, //决定填充哪些界面中的组件
                        R.id.signatureTextView});

        friendsListView = (ListView) findViewById(R.id.friendsListView);
        friendsListView.setAdapter(simpleAdapter);
        //添加Item点击事件
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("Test", view.findViewById(R.id.nickNameTextView).toString());
                HashMap<String, Object> clickedItem = (HashMap<String, Object>)friendsListView.getItemAtPosition(i);
                String nickName = clickedItem.get("nickName").toString();
                String phoneNum = clickedItem.get("phoneNum").toString();
                String signature = clickedItem.get("signature").toString();
                //Toast.makeText(FriendsList.this, nickName + " " + phoneNum + " " + signature, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("nickName", nickName);
                bundle.putString("phoneNum", phoneNum);
                bundle.putString("signature", signature);

                Intent intent = new Intent();
                intent.setClass(FriendsList.this, FriendInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            returnInfo = WebService.executeHttpGet(MainActivity.s_userName, WebService.State.GetFriendsList);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();//关闭进度框
                    if (returnInfo.equals("failed")) {         //返回错误信息
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FriendsList.this);
                        alertDialog.setTitle("错误信息").setMessage("获取数据失败，请检查网络连接或重新登录!");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    } else {
                        //Toast.makeText(FriendsList.this, returnInfo, Toast.LENGTH_SHORT).show();
                        initList();
                    }
                }
            });
        }
    }
}
