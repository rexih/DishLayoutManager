package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.dishlib
 * @file DishLayoutManager
 * @date 2019/1/1
 */
public class DishLayoutManager extends RecyclerView.LayoutManager {


    private final int screenWidth;
    private int itemWidth;
    private int itemHeight;

    public int maxStackCount = 5;
    public int initialStackCount = 3;

    private Context context;
    private int casadeSpace;
    private int topViewLeft;

    public DishLayoutManager(Context context, int maxStackCount) {
        this.context = context;
        this.maxStackCount = maxStackCount;

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // TODO

        int itemCount = state.getItemCount();
        if (itemCount == 0) {
            //没有Item可布局，就回收全部临时缓存 (参考自带的LinearLayoutManager)
            //这里的没有Item，是指Adapter里面的数据集，
            //可能临时被清空了，但不确定何时还会继续添加回来
            removeAndRecycleAllViews(recycler);
            return;
        }

        // 计算item的大小，onLayoutChildren并不是每次都调用，而是布局初始化或者数据集合发生改变，或者requestLayout时才调用
        // 这些场景都有重新计算一次item宽高的必要性
        View anchorView = recycler.getViewForPosition(0);
        addView(anchorView);
        measureChildWithMargins(anchorView, 0, 0);
        itemWidth = getDecoratedMeasuredWidth(anchorView);
        itemHeight = getDecoratedMeasuredHeight(anchorView);
        int casadeSize = itemCount;
        if (initialStackCount > itemCount) {
            casadeSize = initialStackCount;
        } else if (maxStackCount < itemCount) {
            casadeSize = maxStackCount;
        }
        topViewLeft = (screenWidth - itemWidth) >> 1;
        casadeSpace = topViewLeft / (casadeSize-1);


        // 暂时将所有view回收，等待处理
        detachAndScrapAttachedViews(recycler);

        int startIndex = Math.min(maxStackCount, itemCount) - 1;
        int endIndex = 0;
        for (int i = startIndex; i >= endIndex; i--) {
            View viewForPosition = recycler.getViewForPosition(i);
            addView(viewForPosition);
            measureChildWithMargins(viewForPosition, 0, 0);
            int left = topViewLeft - i * casadeSpace;
            int top = 0;
            int right = left + getDecoratedMeasuredWidth(viewForPosition);
            int bottom = top + getDecoratedMeasuredHeight(viewForPosition);
            layoutDecoratedWithMargins(viewForPosition, left, top, right, bottom);
            viewForPosition.setAlpha(1 - 0.25f * i);
        }


    }

//    @Override
//    public boolean canScrollHorizontally() {
//        return true;
//    }
//
//    @Override
//    public boolean canScrollVertically() {
//        return false;
//    }
//
//    @Override
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        return super.scrollHorizontallyBy(dx, recycler, state);
//
//    }
}
