package com.example.administrator.beijingnews.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/17.
 */

public class RefreshListView extends ListView {
    private LinearLayout llHeaderView;

    @ViewInject(R.id.ll_pull_down_refresh)
    private LinearLayout ll_pull_down_refresh;

    @ViewInject(R.id.iv_pull_down_refresh_arraw)
    private ImageView iv_pull_down_refresh_arraw;

    @ViewInject(R.id.pb_refresh)
    private ProgressBar pb_refresh;

    @ViewInject(R.id.tv_refresh_status)
    private TextView tv_refresh_status;

    @ViewInject(R.id.tv_refresh_date)
    private TextView tv_refresh_date;

    @ViewInject(R.id.ll_footer)
    private LinearLayout llPullUpRefreshFooter;

    /**
     * 下拉控件的高
     */
    private int pullDownRefreshHeaderHeight;
    private int llPullUpRefreshFooterHeight;
    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;

    /**
     * 释放刷新
     */
    public static final int RELEASE_UP_REFRESH = 1;

    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;

    /**
     * 当前刷新状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    private LinearLayout footerView;

    /**
     * 是否加载更多
     */
    private boolean isLoadMore;
    /**
     * 顶部轮播图部分
     */
    private View topNewsView;

    /**
     * ListView在Y轴上的坐标，不会有变化
     */
    private int listViewOnScreenY = -1;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footerView = (LinearLayout) View.inflate(context, R.layout.pull_up_refresh_footer, null);
        x.view().inject(RefreshListView.this, footerView);

        llPullUpRefreshFooter.measure(0, 0);
        llPullUpRefreshFooterHeight = llPullUpRefreshFooter.getMeasuredHeight();
        footerView.setPadding(0, -llPullUpRefreshFooterHeight, 0, 0);
        addFooterView(footerView);

        setOnScrollListener(new MyOnScrollListener());
    }

    /**
     * 添加顶部轮播图
     *
     * @param headView 顶部轮播图
     */
    public void addTopNewsView(View headView) {
        if (headView != null) {
            this.topNewsView = headView;
            llHeaderView.addView(topNewsView);
        }
    }

    class MyOnScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            //静止或者惯性滚动时
            if (i == OnScrollListener.SCROLL_STATE_IDLE || i == OnScrollListener.SCROLL_STATE_FLING) {
                //并且是最后一条可见
                if (getLastVisiblePosition() >= getCount() - 1) {
                    //1.显示加载更多的布局
                    footerView.setPadding(8, 8, 8, 8);
                    //2.状态改变
                    isLoadMore = true;
                    //3.回调加载更多接口
                    if (mOnRefreshListener != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mOnRefreshListener.onLoadingMore();
                            }
                        }, 1500);
                    }
                }
            }

        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }


    private Animation upAnimation;
    private Animation downAnimation;

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        llHeaderView = (LinearLayout) View.inflate(context, R.layout.pull_down_refresh_header, null);
        x.view().inject(RefreshListView.this, llHeaderView);

        //1、测量
        ll_pull_down_refresh.measure(0, 0);
//        2、得到refresh控件的高
        pullDownRefreshHeaderHeight = ll_pull_down_refresh.getMeasuredHeight();
        ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeaderHeight, 0, 0);
        //添加头
        addHeaderView(llHeaderView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (startY == -1) {
                    startY = ev.getY();
                }

                boolean isTopNewsDisplay = isTopNewsDisPlay();
                if (!isTopNewsDisplay) {
                    //加载更多的情形
                    break;
                }

                if (currentStatus == REFRESHING) {
                    break;
                }
                float endY = ev.getY();
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    //下拉刷新
                    //-控件高为完全隐藏，控件靠滑动动态显示
//                    if (distanceY > pullDownRefreshHeaderHeight + DensityUtil.dip2px(20)) {
//                        distanceY = pullDownRefreshHeaderHeight + + DensityUtil.dip2px(20);
//                    }
                    int paddingTop = (int) (-pullDownRefreshHeaderHeight + distanceY);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        currentStatus = PULL_DOWN_REFRESH;
                        setViewStatus();
                    } else if (paddingTop >= 0 && currentStatus != RELEASE_UP_REFRESH) {
                        currentStatus = RELEASE_UP_REFRESH;
                        setViewStatus();
                    }
                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeaderHeight, 0, 0);

                } else if (currentStatus == RELEASE_UP_REFRESH) {
                    currentStatus = REFRESHING;
                    setViewStatus();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    //回调刷新接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否完全显示顶部轮播图
     * 当LsitView在屏幕上的Y轴坐标小于或者等于顶部轮播图在Y轴上的坐标的时候，顶部轮播图完全显示。
     *
     * @return
     */
    private boolean isTopNewsDisPlay() {
        if (topNewsView != null) {
            //1.得到ListView在屏幕上的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1) {
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }

            //2.得到顶部轮播图在屏幕上的坐标
            topNewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];

            return listViewOnScreenY <= topNewsViewOnScreenY;

        } else {
            return true;
        }
    }

    private void setViewStatus() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:
                iv_pull_down_refresh_arraw.startAnimation(downAnimation);
                tv_refresh_status.setText("下拉刷新..");
                pb_refresh.setVisibility(GONE);
                iv_pull_down_refresh_arraw.setVisibility(VISIBLE);
                break;
            case RELEASE_UP_REFRESH:
                iv_pull_down_refresh_arraw.startAnimation(upAnimation);
                tv_refresh_status.setText("松手刷新..");
                break;
            case REFRESHING:
                tv_refresh_status.setText("正在刷新..");
                pb_refresh.setVisibility(VISIBLE);
                iv_pull_down_refresh_arraw.clearAnimation();
                iv_pull_down_refresh_arraw.setVisibility(GONE);
                break;
        }
    }

    /**
     * 当刷新完成（不管联网请求成功与否）回调该方法
     *
     * @param isRefreshSuccess
     */

    public void onRefreshFinished(boolean isRefreshSuccess) {
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            footerView.setPadding(0, -llPullUpRefreshFooterHeight, 0, 0);
        } else {
            currentStatus = PULL_DOWN_REFRESH;
            iv_pull_down_refresh_arraw.clearAnimation();
            pb_refresh.setVisibility(GONE);
            iv_pull_down_refresh_arraw.setVisibility(VISIBLE);
            if (isRefreshSuccess) {
                tv_refresh_status.setText("刷新成功..");
                tv_refresh_date.setText("上次更新时间：" + getSystemTime());
            } else {
                tv_refresh_status.setText("刷新失败..");
            }
            tv_refresh_status.setText("下拉刷新..");
            //隐藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeaderHeight, 0, 0);
        }
    }

    private String getSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener {
        /**
         * 当下拉刷新的时候回调这个方法
         */
        public void onPullDownRefresh();

        public void onLoadingMore();
    }

    private OnRefreshListener mOnRefreshListener;

    /**
     * 设置刷新监听，由外界设置
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public TextView getTv_refresh_status() {
        return tv_refresh_status;
    }
}
