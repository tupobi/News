package com.example.administrator.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/16.
 */

public class HorizontalScrollViewPager extends ViewPager {
    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 滑动还是X，Y坐标
     */
    private float startX;
    private float startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父层视图不拦截滑动事件
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = ev.getX() - startX;
                float distanceY = ev.getY() - startY;

                if (Math.abs(distanceX - distanceY) > 0) {
                    if (getCurrentItem() == 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (getCurrentItem() == getAdapter().getCount() - 1 && distanceX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
