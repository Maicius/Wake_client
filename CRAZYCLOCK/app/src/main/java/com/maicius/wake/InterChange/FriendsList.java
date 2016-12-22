package com.maicius.wake.InterChange;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.maicius.wake.alarmClock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsList extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("portrait", R.drawable.ic_portrait);
            listItem.put("nickName", "昵称");
            listItem.put("phoneNum", "158748855665");
            listItem.put("signature", "这是签名");
            listItems.add(listItem);
        }

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,                         //填充的Items列表
                R.layout.item_friendheader,                                                      //填充的界面
                new String[] {"portrait", "nickName", "phoneNum", "signature"},                  //哪些key对应的value来生成列表项
                new int[] {R.id.portraitImageView, R.id.nickNameTextView, R.id.phoneNumTextView, //决定填充哪些界面中的组件
                        R.id.signatureTextView});

        ListView friendsListView = (ListView) findViewById(R.id.friendsListView);
        friendsListView.setAdapter(simpleAdapter);
    }
}
