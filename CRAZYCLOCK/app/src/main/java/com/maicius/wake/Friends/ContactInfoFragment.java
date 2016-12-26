package com.maicius.wake.Friends;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.maicius.wake.alarmClock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactInfoFragment extends Fragment {
    Map<String, String> contactsInfo;
    private List<Map<String, Object>> listItems;
    private SimpleAdapter adapter;
    private ListView listView;
    private String addFriendPhone;
    //定义一个回调接口，该gragment所在的activity需要实现这个接口
    private CallBackValue callBackValue;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        listItems = new ArrayList<Map<String, Object>>();

        if (bundle.containsKey("contacts")) {
            SerializableMap tmp = (SerializableMap) bundle.get("contacts");
            contactsInfo = tmp.getMap();

            //遍历map
            String name, telnum;
            for (Map.Entry<String, String> entry:contactsInfo.entrySet()) {
                name = entry.getKey();
                telnum = entry.getValue();
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("portrait", R.drawable.ic_portrait);
                item.put("name", name);
                item.put("phone", telnum);
                listItems.add(item);
            }

            adapter = new SimpleAdapter(getActivity(), listItems,
                    R.layout.item_search_friend_result,
                    new String[]{"portrait", "name", "phone"},
                    new int[]{R.id.portraitImageView, R.id.nickNameTextView, R.id.phoneNumTextView,});

        } else {
            //contactsInfo = new HashMap<String, String>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_info, container, false);
        listView = (ListView)rootView.findViewById(R.id.contactListView);
        listView.setAdapter(this.adapter);
//        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//                contextMenu.add(0,0,0,"添加为好友");
//                int position = ((AdapterView.AdapterContextMenuInfo)contextMenuInfo).position;
//                addFriendPhone = ((TextView)listView.getChildAt(position).findViewById(R.id.phoneNumTextView)).getText().toString();
//            }
//        });
        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String id = String.valueOf(info.id);
        //点击的item在listview中的位置
        //Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case 0:                 //添加好友
                callBackValue.SendMessageValue(this.addFriendPhone);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        //把该activity当成CallBackValue对象
        callBackValue = (CallBackValue)getActivity();
    }

    //Fragment通过这个接口实现和所在的activity通信
    public interface CallBackValue {
        void SendMessageValue(String value);
    }
}
