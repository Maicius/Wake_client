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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.maicius.wake.alarmClock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConditionSearchFriend extends Fragment {
    private CallBackInputValue callback;
    private List<Map<String, Object>> listItems;
    private SimpleAdapter adapter;
    public ConditionSearchFriend() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null)
            return;
        Bundle bundle = getArguments();
        if (bundle.containsKey("info")) {
            Log.v("Eric", bundle.getString("info"));
            String info = bundle.getString("info");
            StringTokenizer st = new StringTokenizer(info, "#");
            listItems = new ArrayList<Map<String, Object>>();
            int tokenNum = st.countTokens();

            for (int i = 0; i < tokenNum / 2; i++)
            {
                String userName = st.nextToken();
                String nickName = st.nextToken();

                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("phoneNum", userName);
                listItem.put("nickName", nickName);
                listItems.add(listItem);
            }

            adapter = new SimpleAdapter(getActivity(), listItems,
                    R.layout.item_search_friend_result,
                    new String[]{"nickName", "phoneNum"},
                    new int[]{R.id.nickNameTextView, R.id.phoneNumTextView,});

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_condition_search_friend, container, false);
        Button searchBtn = (Button)rootView.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nickNameTextView = (EditText) rootView.findViewById(R.id.nickNameEditView);
                EditText phoneTextView = (EditText) rootView.findViewById(R.id.telNumEditText);
                String nickName = nickNameTextView.getText().toString();
                String telnumber = phoneTextView.getText().toString();
                callback.SendInputValue(nickName, telnumber);
            }
        });
        ListView listView = (ListView)rootView.findViewById(R.id.searchResultListView);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Button btn = (Button) view.findViewById(R.id.searchBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("sss", "clicked in conditionsearch.");
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (CallBackInputValue) getActivity();
    }

    public interface CallBackInputValue {
        void SendInputValue(String nickName, String phone);
    }
}
