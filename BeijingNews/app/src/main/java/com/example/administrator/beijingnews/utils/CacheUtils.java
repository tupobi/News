package com.example.administrator.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.beijingnews.activity.GuideActivity;

/**
 * Created by Administrator on 2017/9/5.
 */

public class CacheUtils {

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
}
