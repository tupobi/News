package com.example.administrator.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.beijingnews.activity.MainActivity;
import com.example.administrator.beijingnews.base.BasePager;
import com.example.administrator.beijingnews.base.MenuDetailBasePager;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.fragment.LeftMenuFragment;
import com.example.administrator.beijingnews.menudetailpager.InteractMenuDetailPager;
import com.example.administrator.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.example.administrator.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.example.administrator.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.example.administrator.beijingnews.utils.Constants;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.example.administrator.beijingnews.utils.SpUtils;
import com.example.administrator.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9.
 */

public class NewsCenterPager extends BasePager {
    private List<NewsCenterPagerBean.DataBean> data;
    /**
     * 详情页面的集合
     */
    private ArrayList<MenuDetailBasePager> detailPagers;
    private String CACHED_JSON_DATA = "saved_json_data";

    public NewsCenterPager(Context mContext) {
        super(mContext);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心页面数据加载成功！");
        ibTitleMenu.setVisibility(View.VISIBLE);
        ib_switchListGridView.setVisibility(View.GONE);
        //1.设置标题
        tvTitle.setText("新闻中心");
        //2.联网请求得到数据，创建和加载视图
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(32);
        //3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        //3.绑定数据
        textView.setText("新闻中心页面内容");

        String cachedJsonData = SpUtils.getInstance().getString(CACHED_JSON_DATA, "");
        if (!TextUtils.isEmpty(cachedJsonData)) {
            processData(cachedJsonData);
        }
//        getDataFromNet();
        getDataFromNetByVolley();

    }

    /**
     * 使用volley请求数据
     */
    private void getDataFromNetByVolley() {
        //请求队列
//        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.NEWS_CENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("使用volley联网请求数据成功，result = " + s);
                SpUtils.getInstance().save(CACHED_JSON_DATA, s);
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用volley联网请求数据是啊比，result = " + volleyError.getMessage());
            }
        }){
            //处理volley请求数据乱码问题
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    //修改编码方式
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加进队列
        VolleyManager.getRequestQueue().add(stringRequest);
    }

    /**
     * 使用xUtils3联网请求数据
     */
    private void getDataFromNet() {
        final RequestParams requestParams = new RequestParams(Constants.NEWS_CENTER_PAGER_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网请求数据成功，resul = " + result);
                //缓存JSON数据
                SpUtils.getInstance().save(CACHED_JSON_DATA, result);

                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网请求失败！" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网请求取消" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("联网请求完成");
            }
        });
    }

    private void processData(String jsonData) {
        NewsCenterPagerBean bean = parsedJsonData(jsonData);
        data = bean.getData();
        //将菜单标题传递给左侧菜单
        //1、得到MainActivity
        MainActivity mainActivity = (MainActivity) mContext;
        //2、得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //添加详情页面
        detailPagers = new ArrayList<>();
        detailPagers.add(new NewsMenuDetailPager(mContext, data.get(0)));//若请求数据失败，会崩掉。应该加缓存
        detailPagers.add(new TopicMenuDetailPager(mContext, data.get(0)));
        detailPagers.add(new PhotosMenuDetailPager(mContext, data.get(2)));
        detailPagers.add(new InteractMenuDetailPager(mContext));
        //3、在左侧菜单中设置得到的标题
        leftMenuFragment.setmData(data);

    }

    private NewsCenterPagerBean parsedJsonData(String jsonData) {
        return new Gson().fromJson(jsonData, NewsCenterPagerBean.class);
    }

    /**
     * 根据位置切换详情页面
     *
     * @param position 选中的详情页面位置
     */
    public void switchDetailPager(int position) {
        //1.设置标题
        tvTitle.setText(data.get(position).getTitle());
        //2.移除页面内容,移除的是BasePager中的FrameLayout内容
        flContent.removeAllViews();
        //3.添加详情页面的新内容并初始化详情页面数据
        MenuDetailBasePager detailPager = detailPagers.get(position);
        View rootView = detailPager.rootView;
        detailPager.initData();//初始化数据
        flContent.addView(rootView);

        if (position == 2) {
            //图组详情页面
            ib_switchListGridView.setVisibility(View.VISIBLE);
            final PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager) detailPagers.get(2);
            ib_switchListGridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photosMenuDetailPager.switchListAndGridView(ib_switchListGridView);
                }
            });
        } else {
            ib_switchListGridView.setVisibility(View.GONE);
        }
    }
}
