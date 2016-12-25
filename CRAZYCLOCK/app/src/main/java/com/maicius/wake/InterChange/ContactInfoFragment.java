package com.maicius.wake.InterChange;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.maicius.wake.alarmClock.R;
import com.maicius.wake.alarmClock.ToastMaster;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("sss", "clicked");
                final HashMap<String, Object> clickedItem = (HashMap<String, Object>)listView.getItemAtPosition(i);
                final Button btn = (Button) view.findViewById(R.id.searchBtn);
                final String phone = clickedItem.get("phone").toString();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("sss", "clicked" + btn.getId());
                        callBackValue.SendMessageValue(phone);
                    }
                });

            }
        });
        return rootView;
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
