package com.example.administrator.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.base.BaseFragment;
import com.example.administrator.beijingnews.utils.LogUtil;

/**
 * Created by Administrator on 2017/9/6.
 */

/**
 * 正文Fragment
 */
public class ContentFragment extends BaseFragment {

    private ViewPager vpMainContent;
    private RadioGroup rgMainBottom;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.content_fragment, null);
        vpMainContent = (ViewPager) view.findViewById(R.id.vp_main_content);
        rgMainBottom = (RadioGroup) view.findViewById(R.id.rg_main_bottom);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了！");
        //设置默认选中首页
        rgMainBottom.check(R.id.rb_home);
    }
}
