package com.example.administrator.beijingnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.utils.CacheUtils;
import com.example.administrator.beijingnews.utils.DensityUtil;
import com.example.administrator.beijingnews.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/5.
 */

public class GuideActivity extends Activity {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    private ViewPager vpGuide;
    private Button btnStartMain;
    private LinearLayout llGuidePointGroup;
    private ArrayList<ImageView> guideIvs;//引导页面的三个背景图片
    private ImageView ivRedPoint;
    private int distanceOfTwoPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        setWidget();

    }

    private void setWidget() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        btnStartMain = (Button) findViewById(R.id.btn_start_main);
        llGuidePointGroup = (LinearLayout) findViewById(R.id.ll_guide_point_group);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);

        int guideIvIds[] = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };

        guideIvs = new ArrayList<>();
        for (int i = 0; i < guideIvIds.length; i++) {
            //设置引导页面背景
            ImageView guideIv = new ImageView(this);
            guideIv.setBackgroundResource(guideIvIds[i]);
            //添加到集合中去
            guideIvs.add(guideIv);

            //创建点，并设置参数
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));//单位是像素 需要适配
            if (i != 0) {
                //不包括第一个点，距离左边有10像素，需要适配
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }
            point.setLayoutParams(params);
            llGuidePointGroup.addView(point);
        }

        //设置ViewPager的适配器
        vpGuide.setAdapter(new MyPagerAdapter());

//        //根据View的生命周期，监听红点的视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        vpGuide.addOnPageChangeListener(new MyOnPageChangeListener());

        btnStartMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1、保存参数，进入过主页面
//                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.HAS_STARTED_MAIN_ATY, true);
                SpUtils.getInstance().save(SplashActivity.HAS_STARTED_MAIN_ATY, true);
                //2、跳转到主页面
                MainActivity.actionStart(GuideActivity.this);
                //3、关闭引导页面
                finish();
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 页面滚动，回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int leftMargin = (int) (position * distanceOfTwoPoint + (positionOffset * distanceOfTwoPoint));
            //红点移动的距离 = 当前页面 * 两点之间的距离 + 屏幕滑动百分比 * 两点之间距离

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            layoutParams.leftMargin = leftMargin;//设置红点位置
            ivRedPoint.setLayoutParams(layoutParams);
        }

        /**
         * 当页面被选中的时候回调这个方法
         *
         * @param position 被选中页面对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == guideIvs.size() - 1) {
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                btnStartMain.setVisibility(View.GONE);
            }
        }

        /**
         * 当页面滑动状态发生变化时回调，三种状态：拖拽，静止，惯性
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            //执行不止一次，需要移除
            ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //两点之间间距（红点在两点间最大滑动距离） = 第一个点距离左边的距离 - 第0个点距离左边的距离
            distanceOfTwoPoint = llGuidePointGroup.getChildAt(1).getLeft() - llGuidePointGroup.getChildAt(0).getLeft();

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 返回数据的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
            return guideIvs.size();
        }

        /**
         * 作用：添加页面,实例化当前页面
         *
         * @param container 容器viewPager
         * @param position  要创建页面的位置
         * @return 返回和创建当前页面有关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到当前页面
            ImageView imageView = guideIvs.get(position);
            //将页面添加到容器中
            container.addView(imageView);
            //返回当前页面位置
            //return position;

            //返回页面
//            if (position == 0) {
//                llGuidePointGroup.getChildAt(0).setBackgroundResource(R.drawable.point_red);
//            } else if (position == 1) {
//                llGuidePointGroup.getChildAt(1).setBackgroundResource(R.drawable.point_red);
//            } else if (position == 2) {
//                llGuidePointGroup.getChildAt(2).setBackgroundResource(R.drawable.point_red);
//            }  若红点不动，则设置灰点变红，有Bug
            return imageView;
        }

        /**
         * 判断
         *
         * @param view   当前创建的视图
         * @param object 上面instantiateItem返回的结果
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//            页面位置比较
//            return view == guideIvs.get(Integer.parseInt((String) object));

            //页面比较
            return view == object;
        }

        /**
         * 销毁页面
         *
         * @param container viewPager容器
         * @param position  要销毁页面的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

    }
}
