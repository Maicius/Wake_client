package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.alarmClock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Maicius on 2016/12/27.
 */

public class ScreenOffList extends Activity{
    private String returnInfo;
   // private static Handler handler = new Handler();
    private ListView m_list;
    private ImageView clear_button;
    private List<Map<String, Object>> listItems;
    private DBManager dbManager;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_off);
        dbManager = new DBManager(this);
        m_list = (ListView) findViewById(R.id.screen_off_list);
        listItems = new ArrayList<Map<String, Object>>();
        clear_button = (ImageView)findViewById(R.id.clear_button);

        initList();
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.deleteAppUser("screenOffTime");
                initList();
            }
        });

    }
    private void initList() {
        returnInfo="";
        Cursor c = dbManager.query("screenOffTime");
        c.moveToFirst();
        while(c.moveToNext()){
            returnInfo +=c.getString(2)+"#";
        }

        StringTokenizer st = new StringTokenizer(returnInfo, "#");
        int id = 0;
        while (st.hasMoreTokens()) {
            id++;
            String tmp = st.nextToken();
            if (!tmp.equals("")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", R.drawable.ic_dialog_time);
                map.put("ItemTitle", tmp);
                map.put("ItemID", "锁屏时间" + id + ": ");
                listItems.add(map);
            }
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.time_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemImage", "ItemTitle", "ItemID"},
                new int[]{R.id.imageItem, R.id.textItem, R.id.idItem}
        );

        m_list.setAdapter(mSimpleAdapter);
    }
}
