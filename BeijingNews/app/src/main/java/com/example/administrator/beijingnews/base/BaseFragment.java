package com.example.administrator.beijingnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/6.
 */

/**
 * 基类的Fragment。LeftMenuFragment,ContentFragment将继承它
 */
public abstract class BaseFragment extends Fragment {

    public Context mContext;    //MainActivity

    /**
     * 抽象方法，让孩子实现自己的视图，达到自己特有的效果
     * @return 返回自己得视图
     */
    public abstract View initView();

    /**
     * 当Fragment被创建时回调
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    /**
     * 当视图被创建时回调
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 当Activity被创建后回调
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 如果子页面没有数据，联网请求数据，并且绑定到initView初始化的视图上
     * 让子页面实现该方法,非抽象方法，不强制孩子实现。
     */
    public void initData() {

    }
}
