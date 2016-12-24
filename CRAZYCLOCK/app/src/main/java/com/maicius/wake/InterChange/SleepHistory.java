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
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.maicius.wake.DBmanager.DBManager;
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
    private DBManager dbManager;
    private Switch enable_record;
    private String returnInfo;
    private static Handler handler = new Handler();
    ScreenListener screenListener;
    private ProgressDialog dialog;
    private ListView m_list;
    private List<Map<String, Object>> listItems;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_history);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在获取睡眠历史信息，请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        m_list = (ListView) findViewById(R.id.sleep_history);
        listItems = new ArrayList<Map<String, Object>>();
        new Thread(new MyThread()).start();

        enable_record = (Switch)findViewById(R.id.enable_record);
        dbManager = new DBManager(this);
        screenListener = new ScreenListener(this);
        enable_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    enableSleepTime();
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SleepHistory.this);
                    alertDialog.setTitle("提醒").setMessage("关闭后将不再记录您的睡眠信息");
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            disableSleepTime();
                            dbManager.deleteAppUser("sleepTime");
                        }
                    });
                    alertDialog.create().show();
                }
            }
        });

    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            returnInfo = WebService.executeHttpGet(MainActivity.s_userName, WebService.State.GetSleepTime);
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
                        //Toast.makeText(FriendsList.this, returnInfo, Toast.LENGTH_SHORT).show();
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
                map.put("ItemID", "时间：" + id + ": ");
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
    private void enableSleepTime(){

        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {
                SimpleDateFormat format =
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date curTime = new Date(System.currentTimeMillis());
                String curSleepTime = format.format(curTime);
                ScreenUser user =
                        new ScreenUser(MainActivity.s_userName, curSleepTime);
                dbManager.insertSQL(user);
            }

            @Override
            public void onUserPresent() {
                dbManager.deleteAppUser("sleepTime");
            }
        });
    }
    private void disableSleepTime(){
        screenListener.unregisterListener();
    }

}
