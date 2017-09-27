package com.example.administrator.beijingnews.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.domain.TabDetailPagerBean;
import com.example.administrator.beijingnews.utils.Constants;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class TopNewsPagerAdapter extends PagerAdapter {
    private List<TabDetailPagerBean.DataBean.TopnewsBean> tobNewsData;
    private Context mContext;
    private Handler topNewsHandler;

    public TopNewsPagerAdapter(List<TabDetailPagerBean.DataBean.TopnewsBean> tobNewsData, Context context, Handler topNewsHandler){
        this.tobNewsData = tobNewsData;
        this.mContext = context;
        this.topNewsHandler = topNewsHandler;
    }

    @Override
    public int getCount() {
        return tobNewsData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //实例化ViewPager控件
        ImageView ivTopNews = new ImageView(mContext);
        ivTopNews.setScaleType(ImageView.ScaleType.FIT_XY);
        ivTopNews.setBackgroundResource(R.drawable.home_scroll_default);
        container.addView(ivTopNews);
        //联网请求图片
        TabDetailPagerBean.DataBean.TopnewsBean topNewsData = tobNewsData.get(position);
        String topNewsImageUrl = Constants.BASE_URL + topNewsData.getTopimage();
        x.image().bind(ivTopNews, topNewsImageUrl);

        ivTopNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (topNewsHandler != null) {
                            topNewsHandler.removeCallbacksAndMessages(null);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        topNewsHandler.removeCallbacksAndMessages(null);
                        topNewsHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                topNewsHandler.sendEmptyMessage(0);
                            }
                        }, 3500);
                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        topNewsHandler.removeCallbacksAndMessages(null);
//                        topNewsHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                topNewsHandler.sendEmptyMessage(0);
//                            }
//                        }, 3500);
//                        break;
                }
                return true;
            }
        });
        return ivTopNews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}