package cn.rexih.android.dishlib;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * @author huangwr
 * @version 3.8.3
 * @package com.sanweidu.TddPay.view.widget.banner
 * @file PagerSnapHelper
 * @brief
 * @date 2017/2/16
 * @since 3.8.3
 */

public class PagerSnapHelper extends LinearSnapHelper {

    private static final String TAG = "PagerSnapHelper";
    private int tarPosition = -1;
    private OrientationHelper          mHorizontalHelper;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int
            velocityX, int velocityY) {
        tarPosition = super.findTargetSnapPosition(layoutManager, velocityX,
                velocityY);
        final View currentView = findSnapView(layoutManager);
        if (tarPosition != RecyclerView.NO_POSITION && currentView != null) {
            int curPos = layoutManager.getPosition(currentView);
            int first = ((LinearLayoutManager) layoutManager)
                    .findFirstVisibleItemPosition();
            int last = ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();
            curPos = tarPosition < curPos ?
                    // 目标位置比当前小
                    last :
                    //把当前位置改为last
                    (tarPosition > curPos ?
                            // 目标位置比当前大
                            first :
                            //把当前位置改为first
                            curPos);
            tarPosition = tarPosition < curPos ?
                    curPos - 1 :
                    (tarPosition > curPos ?
                            curPos + 1 :
                            curPos);
        }
        Log.i(TAG,"tarPosition:"+ tarPosition);
        return tarPosition;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView
            .LayoutManager layoutManager, @NonNull View targetView) {
        if (tarPosition == 0) {
            int decoratedStart = getHorizontalHelper(layoutManager)
                    .getDecoratedStart(targetView);
            return new int[]{ decoratedStart, 0 };
        } else {
            return super.calculateDistanceToFinalSnap(layoutManager, targetView);
        }
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView
            .LayoutManager layoutManager) {
        // 反射方式调用方法速度太慢会有卡顿
//            try {
//                Method getHorizontalHelper = this
//                        .getClass()
//                        .getDeclaredMethod("getHorizontalHelper", LayoutManager
//                                .class);
//                getHorizontalHelper.setAccessible(true);
//                mHorizontalHelper = (OrientationHelper) getHorizontalHelper.invoke
//                        (this, layoutManager);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
        // 如果catch了异常再用替代方案
        if (mHorizontalHelper == null || mLayoutManager != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper
                    (layoutManager);
            mLayoutManager = layoutManager;
        }
        return mHorizontalHelper;
    }
}
