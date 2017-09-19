package com.example.administrator.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class NewsMenuDetailPagerAdapter extends PagerAdapter {
    private ArrayList<TabDetailPager> tabDetailPagers;
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> tabDetailData;

    public NewsMenuDetailPagerAdapter(ArrayList<TabDetailPager> tabDetailPagers, List<NewsCenterPagerBean.DataBean.ChildrenBean> tabDetailData) {
        this.tabDetailPagers = tabDetailPagers;
        this.tabDetailData = tabDetailData;
    }

    @Override
    public int getCount() {
        return tabDetailPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TabDetailPager tabDetailPager = tabDetailPagers.get(position);
        View rootView = tabDetailPager.rootView;
        container.addView(rootView);
        tabDetailPager.initData();//初始化数据
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabDetailData.get(position).getTitle();
    }
}
