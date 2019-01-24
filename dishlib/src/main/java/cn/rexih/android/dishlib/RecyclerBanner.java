package cn.rexih.android.dishlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;


/**
 * @author huangwr
 * @version 3.8.2
 * @package com.sanweidu.android.widget.banner
 * @file LoopBanner
 * @brief
 * @date 2017/1/20
 * @since 3.8.2
 */

public class RecyclerBanner extends RecyclerView{
    private static final String TAG = "RecyclerBanner";
    private Subscription                     subscription;
    private IndicatorListener indicator;
    private OnPageChangeListener             onPageChangeListener;
    private float                            mDownX;
    private float                            mDownY;
    private int     initPosition  = 0;
    private int     currentIndex  = initPosition;
    private boolean isLoopEnabled = true;
    private boolean isIntercept = true;
    private DisposableSubscriber<Long> subscriber;

    public void setInitPosition(int initPosition){
        this.initPosition = initPosition;
    }
    public RecyclerBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager instanceof FixedDishLayoutManager3) {
                        ((FixedDishLayoutManager3) layoutManager).fixScroll();
                    }
                }
            }
        });
//        new PagerSnapHelper().attachToRecyclerView(this);
//        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager
//                .HORIZONTAL, false));
//        addOnScrollListener(new OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int
//                    newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int first = ((LinearLayoutManager) recyclerView.getLayoutManager()
//                ).findFirstVisibleItemPosition();
//                int last = ((LinearLayoutManager) recyclerView.getLayoutManager())
//                        .findLastVisibleItemPosition();
//                int newIndex = (first + last) / 2;
//                if (currentIndex != newIndex) {
//                    currentIndex = newIndex;
////                    if (null!=onPageChangeListener){
////                        onPageChangeListener.onPageChange(currentIndex);
////                    }
//                    updateIndicator();
//                }
//            }
//        });
    }

    private void updateIndicator() {
        if (null != indicator) {
            if (null != getAdapter()) {
                Adapter adapter = getAdapter();
                if (adapter instanceof BaseBannerAdapter) {
                    BaseBannerAdapter adapter1 = (BaseBannerAdapter) adapter;
                    indicator.update(adapter1.getPageCount(), adapter1.getIndex
                            (currentIndex));
                } else {
                    indicator.update(adapter.getItemCount(), currentIndex);
                }
            } else {
                indicator.update(0, 0);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isIntercept) {
            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    cancelLoop();

                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    getParent().requestDisallowInterceptTouchEvent(isIntercept);
                    break;
                case MotionEvent.ACTION_MOVE:
                    cancelLoop();

                    if (Math.abs(ev.getX() - mDownX) < Math.abs(ev.getY() - mDownY)) {// 纵向移动
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (ev.getX() - mDownX > 0 /*&& getCurrentItem() == 0*/) {// 向左移动
                        // 到最前面一页的时候
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);//
                        // 其他时候设置父组件不要拦截自己
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    startLoop();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    startLoop();
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
//        boolean b = super.dispatchTouchEvent(ev);
//        LogHelper.w(TAG,"rb dispatchTouchEvent:"+b+";"+ev);
//        return b;
            return super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        boolean b = super.onInterceptTouchEvent(e);
//        LogHelper.w(TAG,"rb onInterceptTouchEvent:"+b+";"+e);
//        return b;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        boolean b = super.onTouchEvent(e);
//        LogHelper.w(TAG,"rb onTouchEvent:"+b+";"+e);
//        return b;
//    }

    public void cancelLoop() {
        if (null != subscriber) {
            subscriber.dispose();
        }
    }

    public void startLoop() {
        cancelLoop();
        if (isLoopEnabled()) {

            subscriber = new DisposableSubscriber<Long>() {
                @Override
                public void onNext(Long aLong) {
//                    LogHelper.w(TAG, "定时器：" + aLong);
                    int count = getAdapter().getItemCount();
                    if (1 < count) { // 多于1个，才循环



                        LayoutManager layoutManager = getLayoutManager();
                        if (layoutManager instanceof FixedDishLayoutManager3) {
                            FixedDishLayoutManager3 lm = (FixedDishLayoutManager3) layoutManager;
                            lm.scrollToNext();
                        } else {
                            RecyclerBanner.this.scrollToPosition(++currentIndex);
                        }

                    }
                }

                @Override
                public void onError(Throwable t) {}

                @Override
                public void onComplete() {}
            };
            timerFlowable.subscribe(subscriber);
        }
    }

    public void destroy() {
        cancelLoop();
//        indicator = null;
//        onPageChangeListener = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelLoop();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == View.VISIBLE) {
            startLoop();
        } else {
            cancelLoop();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    public RecyclerBanner(Context context) {
        this(context, null);
    }

    private static Flowable<Long> timerFlowable = Flowable
            .interval(4, 4, TimeUnit.SECONDS)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    public interface OnPageChangeListener {
        void onPageChange(int currentIndex);
    }


    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface IndicatorListener {
        void update(int count, int position);
    }

    public void attachIndicator(IndicatorListener indicator) {
        this.indicator = indicator;
    }

    public void resetLoop(){
        this.currentIndex = initPosition;
//        if (null != getAdapter()) {
//            if (Integer.MAX_VALUE == getAdapter().getItemCount()) {
//                this.currentIndex = Integer.MAX_VALUE / 2;
//            }
//        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof FixedDishLayoutManager) {
            FixedDishLayoutManager lm = (FixedDishLayoutManager) layoutManager;
            lm.updateToInitPosition(currentIndex);
        } else if (layoutManager instanceof FixedDishLayoutManager2) {
            FixedDishLayoutManager2 lm = (FixedDishLayoutManager2) layoutManager;
            lm.updateToInitPosition(currentIndex);
        } else if (layoutManager instanceof FixedDishLayoutManager3) {
            FixedDishLayoutManager3 lm = (FixedDishLayoutManager3) layoutManager;
            lm.updateToInitPosition(currentIndex);
        } else {
            this.scrollToPosition(currentIndex);
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    private final BannerObserver observer = new BannerObserver();

    private class BannerObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            reset();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            reset();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            reset();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            reset();
        }
        public void reset(){
            Log.w(TAG, "RecyclerBanner.BannerObserver.reset");
            RecyclerBanner.this.resetLoop();
            startLoop();
            updateIndicator();
        }
    }

    public boolean isLoopEnabled() {
        return isLoopEnabled;
    }

    public void setLoopEnabled(boolean loopEnabled) {
        isLoopEnabled = loopEnabled;
    }

    public void enableInterceptTouchEvent(boolean isIntercept){
        this.isIntercept = isIntercept;
    }
}
