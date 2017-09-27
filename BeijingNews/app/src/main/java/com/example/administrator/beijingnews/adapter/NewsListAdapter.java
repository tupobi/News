package com.example.administrator.beijingnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.domain.TabDetailPagerBean;
import com.example.administrator.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;
import com.example.administrator.beijingnews.utils.Constants;
import com.example.administrator.beijingnews.utils.SpUtils;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class NewsListAdapter extends BaseAdapter {
    private List<TabDetailPagerBean.DataBean.NewsBean> newsData;
    private Context mContext;
    private ImageOptions imageOptions;

    public NewsListAdapter(List<TabDetailPagerBean.DataBean.NewsBean> newsData, Context context) {
        this.newsData = newsData;
        this.mContext = context;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
//                .setCrop(true)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }


    @Override

    public int getCount() {
        return newsData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_news_detail_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_news_detail_summary = (TextView) view.findViewById(R.id.tv_news_detail_summary);
            viewHolder.tv_news_date = (TextView) view.findViewById(R.id.tv_news_date);
            viewHolder.iv_news_icon = (ImageView) view.findViewById(R.id.iv_news_icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        //初始化控件数据
        TabDetailPagerBean.DataBean.NewsBean itemNewsData = newsData.get(i);
        String imageUrl = Constants.BASE_URL + itemNewsData.getListimage();
        x.image().bind(viewHolder.iv_news_icon, imageUrl, imageOptions);
//        //请求图片改为glide
//        Glide.with(mContext).load(imageUrl).into(viewHolder.iv_news_icon);

        viewHolder.tv_news_detail_summary.setText(itemNewsData.getTitle());
        viewHolder.tv_news_date.setText(itemNewsData.getPubdate());
        String idArray = SpUtils.getInstance().getString(TabDetailPager.READ_ARRAY_ID, "");
        if (idArray.contains(itemNewsData.getId() + "")) {
            viewHolder.tv_news_detail_summary.setTextColor(Color.GRAY);
        } else {
            viewHolder.tv_news_detail_summary.setTextColor(Color.BLACK);
        }
        return view;
    }

    class ViewHolder {
        ImageView iv_news_icon;
        TextView tv_news_detail_summary;
        TextView tv_news_date;
    }
}