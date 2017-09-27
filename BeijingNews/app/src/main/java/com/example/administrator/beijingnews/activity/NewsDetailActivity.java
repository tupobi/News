package com.example.administrator.beijingnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.utils.LogUtil;
import com.example.administrator.beijingnews.utils.SpUtils;

public class NewsDetailActivity extends Activity implements View.OnClickListener {
    public static final String WEBVIEW_TEXT_SIZE = "webview_text_size";
    private TextView tvTitle;
    private ImageButton ibBack;
    private ImageButton ibSetText;
    private ImageButton ibShare;
    private WebView wbNewsDetail;
    private ProgressBar pbLoading;
    private String url;
    private WebSettings wbSettings;

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        findViews();
        getData();
        LogUtil.e("url == " + url);
    }

    private void getData() {
        url = getIntent().getStringExtra("url");
        wbSettings = wbNewsDetail.getSettings();
        //设置支持javaScript
        wbSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        wbSettings.setUseWideViewPort(true);
        //设置缩放按钮
        wbSettings.setBuiltInZoomControls(true);
        //设置文字大小
//        wbSettings.setTextSize(WebSettings.TextSize.NORMAL);//过时
//        wbSettings.setTextZoom(100);
        switch (SpUtils.getInstance().getInt(WEBVIEW_TEXT_SIZE, 2)) {
            case 0:
                wbSettings.setTextZoom(200);
                break;
            case 1:
                wbSettings.setTextZoom(150);
                break;
            case 2:
                wbSettings.setTextZoom(100);
                break;
            case 3:
                wbSettings.setTextZoom(75);
                break;
            case 4:
                wbSettings.setTextZoom(50);
                break;
        }
        //不让从当前网页跳转到系统浏览器中
        wbNewsDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);

            }
        });
        wbNewsDetail.loadUrl(url);
//        wbNewsDetail.loadUrl("http://www.atguigu.com/download.shtml#jdbc");
    }

    private void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibSetText = (ImageButton) findViewById(R.id.ib_set_text);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        wbNewsDetail = (WebView) findViewById(R.id.wb_newsDetail);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("新闻详情");
        ibBack.setVisibility(View.VISIBLE);
        ibSetText.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);


        ibBack.setOnClickListener(this);
        ibSetText.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            // Handle clicks for ibBack
            finish();
        } else if (v == ibSetText) {
            // Handle clicks for ibSetText
//            Toast.makeText(this, "设置文字", Toast.LENGTH_SHORT).show();
            showChangeTextSizeDialog();
        } else if (v == ibShare) {
            // Handle clicks for ibShare
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
        }
    }

    private int tempTextSize = 2;
    private int realTextSize = tempTextSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");
        String items[] = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};

        builder.setSingleChoiceItems(items, SpUtils.getInstance().getInt(WEBVIEW_TEXT_SIZE, 2), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //这个i是选项的下标
                tempTextSize = i;
            }
        });

        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realTextSize = tempTextSize;

                //这个i和选项无关
                LogUtil.e("realTextSize == " + realTextSize + " i ==" + i);

                SpUtils.getInstance().save(WEBVIEW_TEXT_SIZE, realTextSize);
                changetextSize();
            }
        });
        builder.show();
    }

    private void changetextSize() {
        switch (SpUtils.getInstance().getInt(WEBVIEW_TEXT_SIZE, 2)) {
            case 0:
                Toast.makeText(this, "超大字体", Toast.LENGTH_SHORT).show();
                wbSettings.setTextZoom(200);
                break;
            case 1:
                Toast.makeText(this, "大字体", Toast.LENGTH_SHORT).show();
                wbSettings.setTextZoom(150);
                break;
            case 2:
                Toast.makeText(this, "正常字体", Toast.LENGTH_SHORT).show();
                wbSettings.setTextZoom(100);
                break;
            case 3:
                Toast.makeText(this, "小字体", Toast.LENGTH_SHORT).show();
                wbSettings.setTextZoom(75);
                break;
            case 4:
                Toast.makeText(this, "超小字体", Toast.LENGTH_SHORT).show();
                wbSettings.setTextZoom(50);
                break;
        }
    }
}



