package com.example.administrator.beijingnews.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.activity.MainActivity;
import com.example.administrator.beijingnews.adapter.ContentPagerAdapter;
import com.example.administrator.beijingnews.base.BaseFragment;
import com.example.administrator.beijingnews.base.BasePager;
import com.example.administrator.beijingnews.pager.GovaffairPager;
import com.example.administrator.beijingnews.pager.HomePager;
import com.example.administrator.beijingnews.pager.NewsCenterPager;
import com.example.administrator.beijingnews.pager.SettingPager;
import com.example.administrator.beijingnews.pager.SmartServicePager;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.example.administrator.beijingnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/6.
 */

/**
 * 正文Fragment
 */
public class ContentFragment extends BaseFragment {

    //2.使用xUtils注解初始化控件
    @ViewInject(R.id.vp_main_content)
    private NoScrollViewPager vpMainContent;
    @ViewInject(R.id.rg_main_bottom)
    private RadioGroup rgMainBottom;

    private ArrayList<BasePager> pagers;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.content_fragment, null);
        //1.使用xUtils第一步，将ContentFragment视图注入到xUtils框架中，使得View关联起来可以注解。
        x.view().inject(ContentFragment.this, view);
//        vpMainContent = (ViewPager) view.findViewById(R.id.vp_main_content);
//        rgMainBottom = (RadioGroup) view.findViewById(R.id.rg_main_bottom);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了！");
        //设置默认选中首页

        //初始化五个页面，并且放入集合中
        pagers = new ArrayList<>();
        pagers.add(new HomePager(mContext));
        pagers.add(new NewsCenterPager(mContext));
        pagers.add(new SmartServicePager(mContext));
        pagers.add(new GovaffairPager(mContext));
        pagers.add(new SettingPager(mContext));

        //设置vpMainContent的适配器
        vpMainContent.setAdapter(new ContentPagerAdapter(pagers));
        rgMainBottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //设置ViewPager页面改变监听
        vpMainContent.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置首页默认参数
        rgMainBottom.check(R.id.rb_home);
        pagers.get(0).initData();//不加该方法首页数据可能一开始没加载
        isMenuEnableSliding(false);
        //设置首页默认参数

    }

    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) pagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调该方法
         *
         * @param position：被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            //防止预加载，在页面被选中时才初始化数据initData()，但是下一个页面仍然被初始化，只是没有加载数据
            pagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i) {
                case R.id.rb_home:
                    vpMainContent.setCurrentItem(0, false);//加上false没有切换的动画效果
                    isMenuEnableSliding(false);
                    break;
                case R.id.rb_news:
                    vpMainContent.setCurrentItem(1, false);
                    isMenuEnableSliding(true);
                    break;
                case R.id.rb_smartservice:
                    vpMainContent.setCurrentItem(2, false);
                    isMenuEnableSliding(false);
                    break;
                case R.id.rb_government:
                    vpMainContent.setCurrentItem(3, false);
                    isMenuEnableSliding(false);
                    break;
                case R.id.rb_setting:
                    vpMainContent.setCurrentItem(4, false);
                    isMenuEnableSliding(false);
                    break;
            }
        }
    }

    /**
     * 设置SlidingMenu可否滑动
     *
     * @param isEnableSliding flag
     */
    private void isMenuEnableSliding(boolean isEnableSliding) {
        MainActivity mainActivity = (MainActivity) mContext;
        if (isEnableSliding) {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

}
