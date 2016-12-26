package com.maicius.wake.Friends;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 19843 on 2016/12/24.
 */

public class SerializableMap implements Serializable {
    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public int getMapSize() {
        return map.size();
    }
}
