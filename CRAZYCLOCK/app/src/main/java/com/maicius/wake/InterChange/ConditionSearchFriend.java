package com.maicius.wake.InterChange;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maicius.wake.alarmClock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConditionSearchFriend extends Fragment {

    public ConditionSearchFriend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_condition_search_friend, container, false);
        return rootView;
    }
}
