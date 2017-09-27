package com.example.administrator.beijingnews.menudetailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.activity.MainActivity;
import com.example.administrator.beijingnews.adapter.NewsMenuDetailPagerAdapter;
import com.example.administrator.beijingnews.adapter.TopicNewsMenuDetailPagerAdapter;
import com.example.administrator.beijingnews.base.MenuDetailBasePager;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;
import com.example.administrator.beijingnews.menudetailpager.tabdetailpager.TopicTabDetailPager;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

/**
 * 专题详情页面
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {
    /**
     * 页签页面数据集合
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> tabDetailData;
    /**
     * 页签页面的集合
     */
    private ArrayList<TopicTabDetailPager> tabDetailPagers;

    @ViewInject(R.id.tl_detailPager)
    private TabLayout tlDetailPager;

    @ViewInject(R.id.vp_newsmenu_detail)
    private ViewPager vpNewsmenuDetail;

    @ViewInject(R.id.ib_next_tabpager)
    private ImageButton ibNextTabPager;

    public TopicMenuDetailPager(Context mContext, NewsCenterPagerBean.DataBean dataBean) {
        super(mContext);
        tabDetailData = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.topicmenu_detail_pager, null);
        x.view().inject(TopicMenuDetailPager.this, view);

        ibNextTabPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpNewsmenuDetail.setCurrentItem(vpNewsmenuDetail.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题详情页面数据被初始化了！");

        //准备新闻页签页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < tabDetailData.size(); i++) {
            tabDetailPagers.add(new TopicTabDetailPager(mContext, tabDetailData.get(i)));
        }
        //设置ViewPager适配器
        vpNewsmenuDetail.setAdapter(new TopicNewsMenuDetailPagerAdapter(tabDetailPagers, tabDetailData));

        //ViewPager和TabPagerIndicator关联，必须在setAdapter之后
//        tpiDetailPager.setViewPager(vpNewsmenuDetail);
        //改为TabLayout
        tlDetailPager.setupWithViewPager(vpNewsmenuDetail);
        //主页以后监听页面变化，就用TabPagerIndicator来监听
        vpNewsmenuDetail.addOnPageChangeListener(new TopicMenuDetailPager.TabDetailPagerOnPageChangeListener());
//        tlDetailPager.setTabMode(TabLayout.MODE_FIXED);//导致标题无法显示（标题太密集了）
        tlDetailPager.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    class TabDetailPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                isMenuEnableSliding(true);
            } else {
                isMenuEnableSliding(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void isMenuEnableSliding(boolean isEnableSliding) {
        MainActivity mainActivity = (MainActivity) mContext;
        if (isEnableSliding) {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
