package com.example.administrator.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class MenuDetailBasePager {
    /**
     * 上下文
     */
    public final Context mContext;
    /**
     * 代表各个详情页面的视图
     */
    public View rootView;

    protected MenuDetailBasePager(Context mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    /**
     * 抽象方法，强制孩子实现该方法
     * @return  返回视图
     */
    public abstract View initView();

    public void initData(){

    }
}
