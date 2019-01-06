package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


/**
 * getPageSize用于设置一页使用多少个dataSet集合里的数据；
 * 设置viewType需要通过getPageType来设置；
 * bind告诉子类当前真实的item序列数
 * @author huangwr
 * @version 3.8.3
 * @package com.sanweidu.android.widget.banner
 * @file BaseBannerAdapter
 * @brief 将一组数据当作一个item的data的adapter
 * @date 2017/2/8
 * @since 3.8.3
 */

public abstract class BaseItemAdapter<T, VH extends RecyclerView.ViewHolder>
        extends BaseRecyclerAdapter<T, VH> {
    protected boolean isPageIncompleteAllowed = false;

    public BaseItemAdapter(Context context) {
        super(context);
    }

    /**
     * 是否允许模版资源不完整，例如模版的一页有5个资源，实际上只为该页配置4个资源
     * @param isAllowed true 允许 false 不允许
     * @return
     */
    public BaseItemAdapter allowPageIncomplete(boolean isAllowed) {
        this.isPageIncompleteAllowed = isAllowed;
        return this;
    }

    /**
     * 绑定模版数据
     * @param holder
     * @param index
     */
    protected abstract void bind(RecyclerView.ViewHolder holder, int index);

    /**
     * 模版每页的资源数
     * @return
     */
    protected int getPageSize() {
        return 1;
    }

    /**
     * 返回模版的页数
     * @return
     */
    public int getPageCount() {
        int pageCount = dataSet.size() / getPageSize();//算分页
        int i = dataSet.size() % getPageSize();//是否有余数
        return (isPageIncompleteAllowed && 0 != i) ?
                pageCount + 1 :
                pageCount;
    }

    /**
     * 返回模版item个数
     * @return
     */
    @Override
    public int getItemCount() {
        return getPageCount();
    }

    /**
     * 返回模版的item类型
     * @param index
     * @return
     */
    protected int getPageType(int index) {
        return super.getItemViewType(index);
    }

    /**
     * 返回模版的item类型
     * @param position
     * @return
     */
    @Override
    final public int getItemViewType(int position) {
        //如果需要通过getItemViewType设置不同类型,请使用getPageType
        return getPageType(getIndex(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (dataSet.size() != 0) {//FIXME 是否需要
            bind(holder, getIndex(position));
        }
    }

    /**
     * 根据position获取itemViewType
     * @param position
     * @return
     */
    public int getIndex(int position) {
        return 0 == getPageCount() ? 0:position % getPageCount();
    }

}
