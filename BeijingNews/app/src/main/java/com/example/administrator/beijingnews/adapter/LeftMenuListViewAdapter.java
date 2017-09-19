package com.example.administrator.beijingnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.domain.NewsCenterPagerBean;
import com.example.administrator.beijingnews.fragment.LeftMenuFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class LeftMenuListViewAdapter extends BaseAdapter {
    List<NewsCenterPagerBean.DataBean> mData;
    Context mContext;

    public LeftMenuListViewAdapter(Context context, List<NewsCenterPagerBean.DataBean> data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.left_menu_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_leftmenu_title);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(mData.get(i).getTitle());
        //设置点击后变成红色(与当前点击的ListView选项做判断)
        viewHolder.tvTitle.setEnabled(LeftMenuFragment.curPosition == i);
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
    }
}
