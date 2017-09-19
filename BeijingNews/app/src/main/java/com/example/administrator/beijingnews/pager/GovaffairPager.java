package com.example.administrator.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.beijingnews.base.BasePager;
import com.example.administrator.beijingnews.utils.LogUtil;

/**
 * Created by Administrator on 2017/9/9.
 */

public class GovaffairPager extends BasePager {
    public GovaffairPager(Context mContext) {
        super(mContext);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要页面数据加载成功！");
        //1.设置标题
        tvTitle.setText("政要");
        //2.联网请求得到数据，创建和加载视图
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(32);
        //3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        //3.绑定数据
        textView.setText("政要页面内容");
    }
}
