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

public class TopicTopNewsPagerAdapter extends PagerAdapter {
    private List<TabDetailPagerBean.DataBean.TopnewsBean> tobNewsData;
    private Context mContext;

    public TopicTopNewsPagerAdapter(List<TabDetailPagerBean.DataBean.TopnewsBean> tobNewsData, Context context){
        this.tobNewsData = tobNewsData;
        this.mContext = context;
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

        return ivTopNews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}