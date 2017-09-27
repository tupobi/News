package com.example.administrator.beijingnews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.example.administrator.beijingnews.utils.Constants;
import com.example.administrator.beijingnews.utils.PictureProcess.GlideRoundTransform;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class PhotoMenuDetailPagerAdapter extends BaseAdapter {

    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    //xUtils的图片设置
//    private ImageOptions imageOptions;
    private Context mContext;

    //ImagerLoader的图片设置
    private DisplayImageOptions options;

    public PhotoMenuDetailPagerAdapter(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news) {
//        imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
//                .setRadius(DensityUtil.dip2px(5))
////                .setCrop(true)
//                .setImageScaleType(ImageView.ScaleType.FIT_XY)
//                .setLoadingDrawableId(R.drawable.news_pic_default)
//                .setFailureDrawableId(R.drawable.news_pic_default)
//                .build();
        mContext = context;
        this.news = news;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_scroll_default)
                .showImageForEmptyUri(R.drawable.home_scroll_default)
                .showImageOnFail(R.drawable.home_scroll_default)
                .cacheInMemory(true) //启用内存缓存
                .cacheOnDisk(true)  //启用外存缓存
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)  //16位
//                .bitmapConfig(Bitmap.Config.ARGB_8888)//32位
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override

    public int getCount() {
        return news.size();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_photos_menudetail_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_loading_image = (TextView) convertView.findViewById(R.id.tv_loading_image);
            viewHolder.pb_loading_image = (ProgressBar) convertView.findViewById(R.id.pb_loading_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置得到对应的数据
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsEntity = news.get(i);
        viewHolder.tv_title.setText(newsEntity.getTitle());
        //请求图片
        String imageUrl = Constants.BASE_URL + newsEntity.getLargeimage();

        //使用xUtils请求图片
//        x.image().bind(viewHolder.iv_photo, imageUrl, imageOptions);

        //使用3.8.0的Glide请求图片
        Glide.with(mContext)
                .load(imageUrl)
                .bitmapTransform(new GlideRoundTransform(mContext, 20))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.home_scroll_default)
                .error(R.drawable.home_scroll_default)
                .into(viewHolder.iv_photo);
        viewHolder.pb_loading_image.setVisibility(View.GONE);
        viewHolder.tv_loading_image.setVisibility(View.GONE);

//        使用ImageLoader请求图片,贼几把坑，多了会卡！！！
//        ImageLoader.getInstance()
//                .displayImage(imageUrl, viewHolder.iv_photo, options, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        viewHolder.pb_loading_image.setVisibility(View.VISIBLE);
//                        viewHolder.iv_photo.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        viewHolder.pb_loading_image.setVisibility(View.GONE);
//                        viewHolder.tv_loading_image.setVisibility(View.GONE);
//                        viewHolder.iv_photo.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        viewHolder.pb_loading_image.setVisibility(View.GONE);
//                        viewHolder.tv_loading_image.setVisibility(View.GONE);
//                        viewHolder.iv_photo.setVisibility(View.VISIBLE);
//                    }
//                }, new ImageLoadingProgressListener() {
//                    @Override
//                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//                        SystemClock.sleep(2000);
//                        viewHolder.tv_loading_image.setText(Math.round(100.0f * current / total) + "%");
//                    }
//                });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_photo;
        TextView tv_title;
        TextView tv_loading_image;
        ProgressBar pb_loading_image;
    }

}
