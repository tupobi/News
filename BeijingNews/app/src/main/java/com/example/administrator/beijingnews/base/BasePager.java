package com.example.administrator.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.activity.MainActivity;

/**
 * Created by Administrator on 2017/9/8.
 */

public class BasePager {
    public final Context mContext;
    public View rootView;

    public TextView tvTitle;
    //点击侧滑的菜单ImageButton
    public ImageButton ibTitleMenu;

    public FrameLayout flContent;

    public BasePager(Context mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    /**
     * 初始化公共部分视图(所以非抽象的，抽象的没有方法体)，并且初始化子页面的FrameLayout
     *
     * @return
     */
    private View initView() {
        //这个view代表基类的页面
        View view = View.inflate(mContext, R.layout.base_pager, null);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ibTitleMenu = (ImageButton) view.findViewById(R.id.ib_title_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        ibTitleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }

    /**
     * 初始化数据、绑定数据
     */
    public void initData() {

    }

}
