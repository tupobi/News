package com.example.administrator.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/10.
 */

public class NoScrollViewPager extends ViewPager {

    /**
     * 通常在代码中实例化的时候使用该方法
     * @param context
     */
    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类的时候，实例化该类用该构造方法，这个方法不能少，否则会崩溃
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写触摸事件，消费掉ViewPager的滑动事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 重写拦截触摸事件，解决冲突问题
     * @param ev
     * @return false:自己不处理，交给后面的控件处理触摸事件 true:自己处理，后面的控件触摸事件将不再有用
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
