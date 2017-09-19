package com.example.administrator.beijingnews;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * Created by Administrator on 2017/9/8.
 */

public class MyApplication extends Application {
    private static Context mContext;

    public static Context getGlobalApplication() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
