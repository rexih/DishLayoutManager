package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


/**
 * @author huangwr
 * @version 3.8.2
 * @package com.sanweidu.TddPay.view.widget.banner
 * @file BaseBannerAdapter
 * @brief
 * @date 2017/1/20
 * @since 3.8.2
 */

public abstract class BaseBannerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends BaseItemAdapter<T, VH> {
    private boolean isInfiniteLoop = true;

    public BaseBannerAdapter(Context context) {
        super(context);
    }


    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    public BaseBannerAdapter setInfiniteLoop(boolean infiniteLoop) {
        isInfiniteLoop = infiniteLoop;
        return this;
    }

    /**
     * 获取轮播模版item的个数
     * @return
     */
    @Override
    public int getItemCount() {
        int pageCount = getPageCount();
        return (1 < pageCount && isInfiniteLoop()) ?
                Integer.MAX_VALUE :
                pageCount;
    }

}
