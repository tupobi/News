package com.example.administrator.beijingnews.menudetailpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.activity.ShowPhotoActivity;
import com.example.administrator.beijingnews.adapter.PhotoMenuDetailPagerAdapter;
import com.example.administrator.beijingnews.base.MenuDetailBasePager;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.example.administrator.beijingnews.utils.Constants;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.example.administrator.beijingnews.utils.SpUtils;
import com.example.administrator.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

/**
 * 图组详情页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean dataBean;
    @ViewInject(R.id.iv_photos)
    private ListView lvPhotos;

    @ViewInject(R.id.gv_photos)
    private GridView gvPhotos;

    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private PhotoMenuDetailPagerAdapter photoMenuDetailPagerAdapter;

    public PhotosMenuDetailPager(Context mContext, NewsCenterPagerBean.DataBean dataBean) {
        super(mContext);

        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);
        x.view().inject(PhotosMenuDetailPager.this, view);

        lvPhotos.setOnItemClickListener(new MyOnItemClickListener());
        gvPhotos.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsEntity = news.get(i);
            //请求图片
            String imageUrl = Constants.BASE_URL + newsEntity.getLargeimage();
            ShowPhotoActivity.actionStart(mContext, imageUrl);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面数据被初始化了！");
        url = Constants.BASE_URL + dataBean.getUrl();

        String saveJson = SpUtils.getInstance().getString(url, "");
        if (!saveJson.isEmpty()) {
            processData(saveJson);
        }
        getDataFromNetByVolley();
    }

    private void getDataFromNetByVolley() {
        //请求队列
//        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("使用volley联网请求数据成功，result = " + s);
                SpUtils.getInstance().save(url, s);
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用volley联网请求数据是啊比，result = " + volleyError.getMessage());
            }
        }) {
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

    private void processData(String jsonData) {
        LogUtil.e(url);
        PhotosMenuDetailPagerBean bean = parsedJson(jsonData);
        news = bean.getData().getNews();

        photoMenuDetailPagerAdapter = new PhotoMenuDetailPagerAdapter(mContext, news);
        //设置适配器
        lvPhotos.setAdapter(photoMenuDetailPagerAdapter);

    }

    private boolean isListView = true;

    public void switchListAndGridView(ImageButton ib_switchListGridView) {
        isListView = !isListView;
        if (isListView) {
            photoMenuDetailPagerAdapter = new PhotoMenuDetailPagerAdapter(mContext, news);
            lvPhotos.setAdapter(photoMenuDetailPagerAdapter);
            ib_switchListGridView.setImageResource(R.drawable.icon_pic_list_type);
            lvPhotos.setVisibility(View.VISIBLE);
            gvPhotos.setVisibility(View.GONE);

        } else {
            photoMenuDetailPagerAdapter = new PhotoMenuDetailPagerAdapter(mContext, news);
            gvPhotos.setAdapter(photoMenuDetailPagerAdapter);
            ib_switchListGridView.setImageResource(R.drawable.icon_pic_grid_type);
            gvPhotos.setVisibility(View.VISIBLE);
            lvPhotos.setVisibility(View.GONE);
        }
    }

    private PhotosMenuDetailPagerBean parsedJson(String jsonData) {
        return new Gson().fromJson(jsonData, PhotosMenuDetailPagerBean.class);
    }
}
