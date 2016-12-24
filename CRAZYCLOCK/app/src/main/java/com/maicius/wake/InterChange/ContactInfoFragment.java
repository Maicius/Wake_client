package com.maicius.wake.InterChange;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                    new int[]{R.id.portraitImageView, R.id.nickNameTextView, R.id.phoneNumTextView});

        } else {
            //contactsInfo = new HashMap<String, String>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_info, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.contactListView);
        listView.setAdapter(this.adapter);
        return rootView;
    }

}
