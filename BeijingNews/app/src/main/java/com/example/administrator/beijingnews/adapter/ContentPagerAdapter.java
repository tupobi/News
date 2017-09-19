package com.example.administrator.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.beijingnews.base.BasePager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/10.
 */

public class ContentPagerAdapter extends PagerAdapter {

    private ArrayList<BasePager> pagers;

    public ContentPagerAdapter(ArrayList<BasePager> pagers) {
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager = pagers.get(position);
        View rootView = pager.rootView;//rootView为各个子页面视图
        //初始化各个子页面数据
//            pager.initData(); 防止预加载，在这里先不直接调用
        container.addView(rootView);
        return rootView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public ContentPagerAdapter() {
        super();
    }

    /**
     * @param container 页面容器
     * @param position
     * @param object    要销毁的页面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
