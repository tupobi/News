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
 * 图组详情页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public PhotosMenuDetailPager(Context mContext) {
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
        LogUtil.e("图组详情页面数据被初始化了！");
        textView.setText("图组详情页面");
    }
}
