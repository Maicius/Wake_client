package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.maicius.wake.alarmClock.MainActivity;
import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.WebService;

public class GetUpHistory extends Activity {

    private ListView m_list;
    private String m_info;
    private ArrayList<HashMap<String, Object>> m_items = new ArrayList<HashMap<String, Object>>();
    private static Handler m_handler = new Handler();
    private ProgressDialog m_proDialog;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_up_history);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        mInitUI();
    }

    private void mInitUI() {
        m_list = (ListView) findViewById(R.id.timeList);
        m_proDialog = new ProgressDialog(GetUpHistory.this);
        m_proDialog.setTitle("提示");
        m_proDialog.setMessage("正在获取历史信息，请稍后...");
        m_proDialog.setCancelable(false);
        m_proDialog.show();
        //创建子线程
        new Thread(new MyThread()).start();
    }

    private void mInitList() {
        StringTokenizer st = new StringTokenizer(m_info, "#");
        int id = 0;
        while (st.hasMoreTokens()) {
            id++;
            String tmp = st.nextToken();
            if (!tmp.equals("")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", R.drawable.ic_dialog_time);
                map.put("ItemTitle", tmp);
                map.put("ItemID", "记录" + id + ": ");
                m_items.add(map);
            }
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, m_items,
                R.layout.time_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemImage", "ItemTitle", "ItemID"},
                new int[]{R.id.imageItem, R.id.textItem, R.id.idItem}
        );

        m_list.setAdapter(mSimpleAdapter);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            //m_info = WebService.executeHttpGet(MainActivity.s_userName,WebService.State.GetTimeList);
            m_info = WebService.executeHttpGet(username, WebService.State.GetTimeList);
            Log.v("sss", "login:" + m_info);
            m_handler.post(new Runnable() {
                @Override
                public void run() {

                    m_proDialog.dismiss();

                    if (m_info.equals("failed")) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetUpHistory.this);
                        alertDialog.setTitle("提示").setMessage("获取历史信息失败！");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();

                    } else {
                        Log.v("sss", m_info);
                        mInitList();
                    }
                }
            });
        }
    }

}
