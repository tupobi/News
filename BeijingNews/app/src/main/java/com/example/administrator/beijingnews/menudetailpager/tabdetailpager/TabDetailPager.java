package com.example.administrator.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.adapter.NewsListAdapter;
import com.example.administrator.beijingnews.adapter.TopNewsPagerAdapter;
import com.example.administrator.beijingnews.base.MenuDetailBasePager;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.domain.TabDetailPagerBean;
import com.example.administrator.beijingnews.utils.Constants;
import com.example.administrator.beijingnews.utils.DensityUtil;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.example.administrator.beijingnews.utils.SpUtils;
import com.example.administrator.beijingnews.view.HorizontalScrollViewPager;
import com.example.administrator.beijingnews.view.RefreshListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/9/13.
 */

/**
 * 页签详情页面
 */
public class TabDetailPager extends MenuDetailBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean tabDetailData;
    private String url;

    @ViewInject(R.id.vp_news_title_image)
    private HorizontalScrollViewPager vpNewsTitleImage;

    @ViewInject(R.id.tv_news_title_summarry)
    private TextView tvNewsTitleSummarry;

    @ViewInject(R.id.ll_vp_point_group)
    private LinearLayout llVpPointGroup;

    @ViewInject(R.id.lv_news_detail)
    private RefreshListView lvNewsDetail;

    private Handler handler;

    /**
     * 顶部轮播图数据
     */
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topImageNewsData;
    /**
     * 新闻列表数据集合
     */
    private List<TabDetailPagerBean.DataBean.NewsBean> newsData;

    private NewsListAdapter newsListAdapter;

    /**
     * 加载更多链接
     */
    private String urlMore;
    private boolean isLoadMore;

    public TabDetailPager(Context mContext, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        tabDetailData = childrenBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        x.view().inject(TabDetailPager.this, view);

        View headView = View.inflate(mContext, R.layout.head_news, null);
        x.view().inject(TabDetailPager.this, headView);

//        lvNewsDetail.addHeaderView(headView);
        lvNewsDetail.addTopNewsView(headView);
        lvNewsDetail.setOnRefreshListener(new MyOnRefreshListener());
        return view;
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadingMore() {
            if (TextUtils.isEmpty(urlMore)) {
                //没有更多数据
                Toast.makeText(mContext, "没有很多数据了！", Toast.LENGTH_SHORT).show();
                lvNewsDetail.onRefreshFinished(false);
            } else {
                getMoreDataFromNet();
            }
        }

    }

    private void getMoreDataFromNet() {
        RequestParams requestParams = new RequestParams(urlMore);
        requestParams.setConnectTimeout(10000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //                隐藏刷新控件，更新时间
                Handler handler = new Handler();
                isLoadMore = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //解析数据
                        lvNewsDetail.onRefreshFinished(true);
                    }
                }, 1 * 1000);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                lvNewsDetail.onRefreshFinished(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + tabDetailData.getUrl();
        LogUtil.e(tabDetailData.getTitle() + "联网地址：" + url);

        //缓存
        String savedJson = SpUtils.getInstance().getString(url, "");
        if (!TextUtils.isEmpty(savedJson)) {
            processData(savedJson);
        }
        getDataFromNet();
    }

    private int prePointPosition;

    /**
     * 解析和处理显示数据
     *
     * @param jsonData：json数据
     */
    private void processData(String jsonData) {
        TabDetailPagerBean bean = parsedJson(jsonData);
        String title = bean.getData().getNews().get(0).getTitle();

        urlMore = "";
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            urlMore = "";
        } else {
            urlMore = Constants.BASE_URL + bean.getData().getMore();
        }

        //区分默认和加载更多
        if (!isLoadMore) {
            topImageNewsData = bean.getData().getTopnews();
            vpNewsTitleImage.setAdapter(new TopNewsPagerAdapter(topImageNewsData, mContext));

            addPoint();

            tvNewsTitleSummarry.setText(topImageNewsData.get(0).getTitle());
            //监听页面变化，设置红点变化和文本变化
            vpNewsTitleImage.addOnPageChangeListener(new vpNewsTitleImageOnPageChangeListener());

            //设置ListView
            //1.先设置ListView数据集合
            newsData = bean.getData().getNews();
            //2.再设置ListView适配器
            newsListAdapter = new NewsListAdapter(newsData, mContext);
            lvNewsDetail.setAdapter(newsListAdapter);
        } else {
            //得到更多数据
            List<TabDetailPagerBean.DataBean.NewsBean> moreNews = bean.getData().getNews();
            //添加到原来集合中
            newsData.addAll(moreNews);
            //刷新适配器
            newsListAdapter.notifyDataSetChanged();
            isLoadMore = false;

        }

    }

    private void addPoint() {
        llVpPointGroup.removeAllViews();//缓存数据执行两次，移除所有的红点

        for (int i = 0; i < topImageNewsData.size(); i++) {
            ImageView ivPoint = new ImageView(mContext);
            ivPoint.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));

            if (i == 0) {
                ivPoint.setEnabled(true);
            } else {
                ivPoint.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(mContext, 5);
            }

            ivPoint.setLayoutParams(params);
            llVpPointGroup.addView(ivPoint);
        }
    }


    class vpNewsTitleImageOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            llVpPointGroup.getChildAt(prePointPosition).setEnabled(false);
            for (int i = 0; i < topImageNewsData.size(); i++) {
                if (i == position) {
                    llVpPointGroup.getChildAt(position).setEnabled(true);
                } else {
                    llVpPointGroup.getChildAt(i).setEnabled(false);
                }
            }
            prePointPosition = position;
            tvNewsTitleSummarry.setText(topImageNewsData.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private TabDetailPagerBean parsedJson(String jsonData) {
        return new Gson().fromJson(jsonData, TabDetailPagerBean.class);
    }


    private void getDataFromNet() {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(10000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("请求数据成功：" + result);
                SpUtils.getInstance().save(url, result);
                processData(result);

                //隐藏下拉刷新控件，更新时间
                handler = new Handler();


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //推迟2秒后要做的事情
                        //handler被removeCallbacksAndMessages(null)后，所有消息都不再执行。
                        lvNewsDetail.getTv_refresh_status().setText("刷新成功！");
                    }
                }, 1 * 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //推迟2秒后要做的事情
                        //handler被removeCallbacksAndMessages(null)后，所有消息都不再执行。
                        lvNewsDetail.onRefreshFinished(true);
                    }
                }, 2 * 1000);


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("请求数据失败" + ex.getMessage());
                //隐藏下拉刷新控件，不更新时间
                lvNewsDetail.onRefreshFinished(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("请求数据取消" + cex.getMessage());
                lvNewsDetail.getTv_refresh_status().setText("刷新失败！");
                lvNewsDetail.onRefreshFinished(false);
            }

            @Override
            public void onFinished() {
                LogUtil.e("请求数据完成");
            }
        });
    }
}