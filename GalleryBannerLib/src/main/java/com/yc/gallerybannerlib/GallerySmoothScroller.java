package com.yc.gallerybannerlib;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/4/17
 *     desc  : 自定义LinearSmoothScroller
 *     revise:
 * </pre>
 */
public class GallerySmoothScroller extends LinearSmoothScroller {

    private static final float MILLISECONDS_PER_INCH = 100f;

    GallerySmoothScroller(Context context) {
        super(context);
    }

    //This returns the milliseconds it takes to
    //scroll one pixel.
    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        // 返回：滑过1px时经历的时间(ms)。
        //return MILLISECONDS_PER_INCH / displayMetrics.density;
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
        //返回滑动一个pixel需要多少毫秒
    }


    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        final int dx = calculateDxToMakeCentral(targetView);
        final int dy = calculateDyToMakeCentral(targetView);
        final int distance = (int) Math.sqrt(dx * dx + dy * dy);
        final int time = calculateTimeForDeceleration(distance);
        if (time > 0) {
            action.update(-dx, -dy, time, mDecelerateInterpolator);
        }
    }


    private int calculateDxToMakeCentral(View view) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
            return 0;
        }
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int left = layoutManager.getDecoratedLeft(view) - params.leftMargin;
        final int right = layoutManager.getDecoratedRight(view) + params.rightMargin;
        final int start = layoutManager.getPaddingLeft();
        final int end = layoutManager.getWidth() - layoutManager.getPaddingRight();
        final int childCenter = left + (int) ((right - left) / 2.0f);
        final int containerCenter = (int) ((end - start) / 2.f);
        return containerCenter - childCenter;
    }

    private int calculateDyToMakeCentral(View view) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollVertically()) {
            return 0;
        }
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int top = layoutManager.getDecoratedTop(view) - params.topMargin;
        final int bottom = layoutManager.getDecoratedBottom(view) + params.bottomMargin;
        final int start = layoutManager.getPaddingTop();
        final int end = layoutManager.getHeight() - layoutManager.getPaddingBottom();
        final int childCenter = top + (int) ((bottom - top) / 2.0f);
        final int containerCenter = (int) ((end - start) / 2.f);
        return containerCenter - childCenter;
    }


}
