package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.maicius.wake.alarmClock.R;
import com.maicius.wake.chart.IChart;
import com.maicius.wake.chart.MBarChart;
import com.maicius.wake.chart.MLineChart;
import com.maicius.wake.chart.MPieChart;
import com.maicius.wake.web.WebService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GetUpHistory extends Activity {

    static public ArrayList<String> times = new ArrayList<String>();

    private static Handler m_handler = new Handler();
    private SimpleAdapter m_simpleAdapter;
    private ListView m_listView;
    private Spinner m_spinner;
    private ProgressDialog m_proDialog;
    private ArrayList<HashMap<String, Object>> m_listViewStrings = new ArrayList<HashMap<String, Object>>();
    private ArrayList<String> m_spinnerListStrings = new ArrayList<String>();
    private String m_responseInfo;
    private String m_username;
    private IChart m_timeChart = new MLineChart();
    private IChart m_barChart = new MBarChart();
    private IChart m_pieChart = new MPieChart();
    private TimeFilter m_timeFilterID;

    private enum TimeFilter {
        NO_LIMIT, LAST_WEEK, LAST_MONTH, LAST_YEAR, USER_DEFINED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_up_history);

        mInit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, Menu.FIRST, 0, R.string.getupHistory_share);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String strTime = m_listViewStrings.get(info.position).get("ItemTitle").toString();
        String strUserName = m_username;
        String strShared = String.format(getResources().getString(R.string.getupHistory_shareContent), strUserName, strUserName, strTime);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, strShared);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.getupHistory_shareTitle)));

        return super.onContextItemSelected(item);
    }

    private void mInit() {
        m_proDialog = new ProgressDialog(GetUpHistory.this);
        m_proDialog.setTitle("提示");
        m_proDialog.setMessage("正在获取历史信息，请稍后...");
        m_proDialog.setCancelable(false);
        m_proDialog.show();

        m_username = this.getIntent().getStringExtra("username");
        m_timeFilterID = TimeFilter.NO_LIMIT;
        m_listView = (ListView) findViewById(R.id.timeList);

        Resources res = getResources();
        m_spinner = (Spinner) findViewById(R.id.timeSpinner);
        m_spinnerListStrings.add(res.getString(R.string.getupHistory_spinner_str1));
        m_spinnerListStrings.add(res.getString(R.string.getupHistory_spinner_str2));
        m_spinnerListStrings.add(res.getString(R.string.getupHistory_spinner_str3));
        m_spinnerListStrings.add(res.getString(R.string.getupHistory_spinner_str4));
        m_spinnerListStrings.add(res.getString(R.string.getupHistory_spinner_str5));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_spinnerListStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner.setAdapter(adapter);
        m_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        m_timeFilterID = TimeFilter.NO_LIMIT;
                        break;
                    case 1:
                        m_timeFilterID = TimeFilter.LAST_WEEK;
                        break;
                    case 2:
                        m_timeFilterID = TimeFilter.LAST_MONTH;
                        break;
                    case 3:
                        m_timeFilterID = TimeFilter.LAST_YEAR;
                        break;
                    case 4:
                        m_timeFilterID = TimeFilter.USER_DEFINED;
                        break;
                    default:
                        m_timeFilterID = TimeFilter.NO_LIMIT;
                        break;
                }
                mUpdateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView image_curve = (ImageView) findViewById(R.id.curve);
        image_curve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = m_timeChart.execute(GetUpHistory.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


        ImageView image_curve_1 = (ImageView) findViewById(R.id.curve_1);
        image_curve_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = m_pieChart.execute(GetUpHistory.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


        ImageView image_curve_2 = (ImageView) findViewById(R.id.curve_2);
        image_curve_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = m_barChart.execute(GetUpHistory.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        registerForContextMenu(m_listView);

        //创建子线程
        new Thread(new ThreadGetHistory()).start();
    }

    private void mInitList() {
        times.clear();
        m_listViewStrings.clear();
        StringTokenizer st = new StringTokenizer(m_responseInfo, "#");
        int id = 0;
        while (st.hasMoreTokens()) {
            id++;
            String tmp = st.nextToken();
            String strWords[] = tmp.split(" ");
            String strDates[] = strWords[0].split("-");
            String strTimes[] = strWords[1].split(":");
            if (!tmp.equals("")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", R.drawable.ic_dialog_time);
                map.put("ItemTitle", "\n日期：" + strDates[0] + "年" + strDates[1] + "月" + strDates[2] + "日" +
                        "\n时间：" + strTimes[0] + "时" + strTimes[1] + "分" + strTimes[2] + "秒\n");
                map.put("ItemID", "记录" + id);
                m_listViewStrings.add(map);
                times.add(tmp);
            }
        }
        m_simpleAdapter = new SimpleAdapter(this, m_listViewStrings,
                R.layout.time_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemImage", "ItemTitle", "ItemID"},
                new int[]{R.id.imageItem, R.id.textItem, R.id.idItem}
        );
        m_listView.setAdapter(m_simpleAdapter);
    }

    private void mUpdateList() {
        times.clear();
        m_listViewStrings.clear();
        StringTokenizer st = new StringTokenizer(m_responseInfo, "#");
        int id = 0;
        while (st.hasMoreTokens()) {
            id++;
            String tmp = st.nextToken();
            if (!mCheckTime(tmp)) {
                continue;
            }
            String strWords[] = tmp.split(" ");
            String strDates[] = strWords[0].split("-");
            String strTimes[] = strWords[1].split(":");
            if (!tmp.equals("")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", R.drawable.ic_dialog_time);
                map.put("ItemTitle", "\n日期：" + strDates[0] + "年" + strDates[1] + "月" + strDates[2] + "日" +
                        "\n时间：" + strTimes[0] + "时" + strTimes[1] + "分" + strTimes[2] + "秒\n");
                map.put("ItemID", "记录" + id);
                m_listViewStrings.add(map);
                times.add(tmp);
            }
        }
        m_simpleAdapter.notifyDataSetChanged();
    }

    private Boolean mCheckTime(String strTime) {
        if (strTime.equals("")) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        Date getupDate;
        try {
            getupDate = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        long diffMillis = curDate.getTime() - getupDate.getTime();
        double diffDays = diffMillis / (1000.0 * 3600 * 24);
        double longDays = 0;
        switch (m_timeFilterID) {
            case NO_LIMIT:
                longDays = Double.MAX_VALUE;
                break;
            case LAST_WEEK:
                longDays = 7;
                break;
            case LAST_MONTH:
                longDays = 30;
                break;
            case LAST_YEAR:
                longDays = 365;
                break;
            case USER_DEFINED:
                longDays = Double.MAX_VALUE;
                break;
        }
        if (diffDays > longDays) {
            return false;
        }
        return true;
    }

    public class ThreadGetHistory implements Runnable {
        @Override
        public void run() {
            m_responseInfo = WebService.executeHttpGet(m_username, WebService.State.GetTimeList);
            Log.v("ikuto", "login:" + m_responseInfo);
            m_handler.post(new Runnable() {
                @Override
                public void run() {

                    m_proDialog.dismiss();

                    if (m_responseInfo.equals("failed")) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetUpHistory.this);
                        alertDialog.setTitle("提示").setMessage("获取历史信息失败！");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create().show();

                    } else {
                        Log.v("ikuto", m_responseInfo);
                        mInitList();
                    }
                }
            });
        }
    }
}

