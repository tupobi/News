package com.example.administrator.beijingnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.fragment.ContentFragment;
import com.example.administrator.beijingnews.fragment.LeftMenuFragment;
import com.example.administrator.beijingnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //1.设置主页面
        setContentView(R.layout.activity_main);
        //初始化左侧菜单
        initLeftSlidingMenu();
        //初始化内容页面
        initFragment();
    }

    private void initLeftSlidingMenu() {
        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);
        //3.设置菜单，包含左侧和右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置右侧菜单
//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);
        //4.设置显示的模式。1、左侧菜单+主页 2、左侧菜单+主页+右侧菜单 3、右侧菜单+主页
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5.设置滑动模式：1、滑动边缘 2、全屏滑动 3、不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
    }

    private void initFragment() {
        //1.得到FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //3.替换
        fragmentTransaction.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);
        fragmentTransaction.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);
        //4.提交事务
        fragmentTransaction.commit();
        //简写如下：可读性不强
//        fragmentManager.beginTransaction().replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG).replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG).commit();

    }

    /**
     * 通过设置的TAG得到左侧菜单Fragment
     * @return 返回左侧菜单Fragment
     */
    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    /**
     * 通过TAG得到内容Fragment
     * @return 返回内容Fragment
     */
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
