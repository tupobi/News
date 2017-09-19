package com.example.administrator.beijingnews.menudetailpager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.beijingnews.base.MenuDetailBasePager;
import com.example.administrator.beijingnews.utils.LogUtil;

/**
 * Created by Administrator on 2017/9/12.
 */

/**
 * 互动详情页面
 */
public class InteractMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public InteractMenuDetailPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        textView = new TextView(mContext);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面数据被初始化了！");
        textView.setText("互动详情页面");
    }
}
