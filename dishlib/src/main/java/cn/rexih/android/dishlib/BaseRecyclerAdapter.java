package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @author huangwr
 * @version 0.0.1
 * @package com.td.huwang.adapter
 * @file BaseRecyclerAdapter
 * @brief RecyclerView 的adapter的基类
 * @date 2016/10/14
 * @since 0.0.1
 */

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter {
    protected Context context;

    protected List<T> dataSet = new ArrayList<T>(0);
    protected OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerAdapter(Context context, List<T> dataSet) {
        this.context = context;
        set(dataSet);
    }

    public void set(List<T> dataSet) {
        this.dataSet.clear();
        add(dataSet);
    }

    public void add(List<T> dataSet) {
        if (null == dataSet) {
            return;
        }
//        int oldDataSize = this.dataSet.size();
        this.dataSet.addAll(dataSet);
        //PRESET: [huangwr][2017/1/14][reason:局部刷新]
        // 为了更好的用户体验提价（不刷新全部），在原来的数据后面添加新数据即可--by yangjh 2018/01/04
//        notifyItemRangeChanged(oldDataSize, dataSet.size());
        notifyDataSetChanged();
    }

    protected View inflateRoot(ViewGroup parent, int layoutID) {
        View root = LayoutInflater.from(context)
                .inflate(layoutID, parent, false);
        return root;
    }

    protected <T> void setOnItemClick(final View root, final T data, final int
            position) {
        if (null != onItemClickListener) {
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(root, data, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View root, T data, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
