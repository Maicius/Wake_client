package com.maicius.wake.InterChange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.maicius.wake.alarmClock.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

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
                if (i == 0)
                {
                    Intent intent = new Intent();
                    intent.setClass(FriendInfo.this, GetUpHistory.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", phoneNum);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}
