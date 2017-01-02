package com.maicius.wake.InterChange;
/**
 * 记录睡眠长度
 * 监测屏幕的广播信息，记录每次屏幕关闭的时间并存储在本地数据库里，
 * 每当屏幕解锁后刷新该时间
 * 并在有网络的情况下将该时间上传到云端数据库
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.maicius.wake.DBmanager.DBManager;
import com.maicius.wake.DBmanager.RecordingUser;
import com.maicius.wake.DBmanager.ScreenUser;
import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.ScreenListener;
import com.maicius.wake.web.WebService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Maicius on 2016/12/22.
 */
public class SleepHistory extends Activity {

    private String returnInfo;
    private static Handler handler = new Handler();
    private ProgressDialog dialog;
    private ListView m_list;
    private String username;
    private List<Map<String, Object>> listItems;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_history);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在获取历史信息，请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        m_list = (ListView) findViewById(R.id.sleep_history);
        listItems = new ArrayList<Map<String, Object>>();
        new Thread(new MyThread()).start();
    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            returnInfo = WebService.executeHttpGet(username, WebService.State.GetSleepTime);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();//关闭进度框
                    if (returnInfo.equals("failed")) {         //返回错误信息
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SleepHistory.this);
                        alertDialog.setTitle("提示").setMessage("未知错误，请稍后重试");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();
                    } else {
                        initList();
                    }
                }
            });
        }
    }
    private void initList() {
        StringTokenizer st = new StringTokenizer(returnInfo, "#");
        int id = 0;
        while (st.hasMoreTokens()) {
            id++;
            String tmp = st.nextToken();
            if (!tmp.equals("")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", R.drawable.ic_dialog_time);
                map.put("ItemTitle", tmp);
                map.put("ItemID", "时间" + id + ": ");
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
