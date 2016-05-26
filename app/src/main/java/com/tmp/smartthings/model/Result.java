package com.tmp.smartthings.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phapli on 21/05/2016.
 */
public class Result {
    public int status;
    public Map<String, Object> datas = new HashMap<>();
    public String message;

    public Result() {
        this.status = 0;
    }

    public Result(int status, String message, Map<String, Object> datas) {
        this.status = status;
        this.datas = datas;
        this.message = message;
    }

    public Result(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setData(String key, Object value) {
        datas.put(key, value);
    }

    public Object getData(String key) {
        return datas.get(key);
    }
}
