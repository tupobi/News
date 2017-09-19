package com.example.administrator.beijingnews.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.activity.MainActivity;
import com.example.administrator.beijingnews.adapter.LeftMenuListViewAdapter;
import com.example.administrator.beijingnews.base.BaseFragment;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.pager.NewsCenterPager;
import com.example.administrator.beijingnews.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

/**
 * 左侧菜单的Fragment
 */
public class LeftMenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> mData;
    private ListView lvLeftMenu;
    private LeftMenuListViewAdapter leftMenuListViewAdapter;
    public static int curPosition;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.menu_listview, null);
        lvLeftMenu = (ListView) view.findViewById(R.id.lv_leftmenu);
        //设置Item点击事件
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1、记录点击的位置，点击效果变成红色
                curPosition = i;
                leftMenuListViewAdapter.notifyDataSetChanged();//getCount-->getView()适配器的几个方法执行
                //2、把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();
                //3、切换到相应的详情页面
                getNewsCenterPagerAndSwitchDetailPager(i);
            }
        });
        return view;
    }

    private void getNewsCenterPagerAndSwitchDetailPager(int i) {
        MainActivity mainActivity = (MainActivity) mContext;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchDetailPager(i);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单被初始化了！");
    }

    /**
     * 接收到新闻中心页面传来的数据
     *
     * @param data 新闻中心的数据
     */
    public void setmData(List<NewsCenterPagerBean.DataBean> data) {
        this.mData = data;
        leftMenuListViewAdapter = new LeftMenuListViewAdapter(mContext, data);
        lvLeftMenu.setAdapter(leftMenuListViewAdapter);
        getNewsCenterPagerAndSwitchDetailPager(0);
    }


}
