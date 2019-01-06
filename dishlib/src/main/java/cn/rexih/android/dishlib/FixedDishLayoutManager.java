package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.dishlib
 * @file FixedDishLayoutManager
 * @date 2019/1/6
 */
public class FixedDishLayoutManager extends RecyclerView.LayoutManager {

    private final int screenWidth;
    private int itemWidth;
    private int itemHeight;
    private Context context;
    private int mTotalOffset;
    private int initPosition;
    private int halfItemWidth;
    private int oneScreenItemCount;

    public FixedDishLayoutManager(Context context) {
        this(context,5000);
    }

    public FixedDishLayoutManager(Context context, int initPosition) {
        this.context = context;
        this.initPosition = initPosition;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void updateToInitPosition(int position) {
        initPosition = position;
        mTotalOffset = 0;
        requestLayout();
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        //TODO
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
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
        // 暂时将所有view回收，等待处理
//        detachAndScrapAttachedViews(recycler);
        halfItemWidth = itemWidth / 2;
        oneScreenItemCount = (int) Math.ceil((screenWidth - itemWidth) * 1.0f / halfItemWidth) + 1 + 1;

        fill(recycler,0);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //dx = oldX- newX => 右滑<0;左滑>0
        fill(recycler,-dx);
        return dx;
    }

    public int fill(RecyclerView.Recycler recycler, int dxReverse) {
        mTotalOffset += dxReverse;

        int currentPosition;
        int startPosition;
        int endPosition;
        int offset;

        if (mTotalOffset >= 0) {
            currentPosition = mTotalOffset / halfItemWidth + initPosition;
            startPosition = currentPosition - 1;
            endPosition = startPosition + oneScreenItemCount;
            offset = mTotalOffset % halfItemWidth;


        } else {
            currentPosition = (int) Math.floor(mTotalOffset * 1.0f / halfItemWidth) + initPosition;
            startPosition = currentPosition - 1;
            endPosition = startPosition + oneScreenItemCount;
            offset = mTotalOffset % halfItemWidth + halfItemWidth;

        }

        // 暂时将所有view回收，等待处理
        detachAndScrapAttachedViews(recycler);
        for (int j = endPosition; j > currentPosition; j--) {
            handleViewByPosition(recycler, startPosition, offset, j);
        }
        handleViewByPosition(recycler, startPosition, offset, startPosition).setAlpha(0.5f);
        handleViewByPosition(recycler, startPosition, offset, currentPosition);
        // TODO 回收scrap没使用的view




        // 根据滑动方向，右滑iw-offset;左滑-offset
        return 0;
    }

    private View handleViewByPosition(RecyclerView.Recycler recycler, int startPosition, int offset, int j) {
        System.out.println(">>>>>>>>j="+j);
        View viewForPosition = recycler.getViewForPosition(j);
        addView(viewForPosition);
        measureChildWithMargins(viewForPosition, 0, 0);
        int left = screenWidth - itemWidth - (j - startPosition) * halfItemWidth + offset;
        int top = 0;
        int right = left + getDecoratedMeasuredWidth(viewForPosition);
        int bottom = top + getDecoratedMeasuredHeight(viewForPosition);
        layoutDecoratedWithMargins(viewForPosition, left, top, right, bottom);
        viewForPosition.setAlpha(1f);
        return viewForPosition;
    }


    public static void main(String[] args) {
        double a = -4.3d;
        //-4.0
        System.out.println(Math.ceil(a));
    }

}
