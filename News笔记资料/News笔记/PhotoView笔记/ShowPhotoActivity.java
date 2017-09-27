package com.example.administrator.beijingnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class ShowPhotoActivity extends Activity {

    private DisplayImageOptions options;
    private ProgressBar pb_loading_photo;
    private TextView tv_loading_photo;

    public static void actionStart(Context context, String imageUrl) {
        Intent intent = new Intent(context, ShowPhotoActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        final PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        pb_loading_photo = (ProgressBar) findViewById(R.id.pb_loading_photo);
        tv_loading_photo = (TextView) findViewById(R.id.tv_loading_photo);

        String imageUrl = getIntent().getStringExtra("imageUrl");

//        Glide.with(this)
//                .load(imageUrl)
//                .into(photoView);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_scroll_default)
                .showImageForEmptyUri(R.drawable.home_scroll_default)
                .showImageOnFail(R.drawable.home_scroll_default)
//                .cacheInMemory(true) //启用内存缓存
//                .cacheOnDisk(true)  //启用外存缓存
                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)  //16位
                .bitmapConfig(Bitmap.Config.ARGB_8888)//32位
//                .displayer(new RoundedBitmapDisplayer(10))//PhotoView不支持ImageLoader的圆角处理
                .build();

        ImageLoader.getInstance()
                .displayImage(imageUrl, photoView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pb_loading_photo.setVisibility(View.VISIBLE);
                        photoView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        pb_loading_photo.setVisibility(View.GONE);
                        tv_loading_photo.setVisibility(View.GONE);
                        photoView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        pb_loading_photo.setVisibility(View.GONE);
                        tv_loading_photo.setVisibility(View.GONE);
                        photoView.setVisibility(View.VISIBLE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        SystemClock.sleep(2000);
                        tv_loading_photo.setText(Math.round(100.0f * current / total) + "%");
                    }
                });
    }
}
