package com.example.administrator.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.beijingnews.MyApplication;


/**
 * Created by Administrator on 2017/6/7.
 */

public class SpUtils {
    private static SpUtils instance = new SpUtils();
    private static SharedPreferences mSp;

    private SpUtils() {

    }

    public static SpUtils getInstance() {
        if (mSp == null) {
            mSp = MyApplication.getGlobalApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
        }
        return instance;
    }

    public void save(String key, Object value) {
        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }
}
