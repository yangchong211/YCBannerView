package com.yc.cn.ycbannerlib.gallery;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.yc.cn.ycbannerlib.BuildConfig;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/3/18
 *     desc  : 自定义LayoutManager
 *     revise: 这也也是间接参考LinearLayoutManager源码
 * </pre>
 */
public class GalleryLayoutManager extends LinearLayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {

    private static final String TAG = "GalleryLayoutManager";
    private final static int LAYOUT_START = -1;
    private final static int LAYOUT_END = 1;
    private static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    private static final int VERTICAL = OrientationHelper.VERTICAL;
    private int mFirstVisiblePosition = 0;
    private int mLastVisiblePos = 0;
    private static int mCurSelectedPosition = 0;
    private State mState;
    private int mOrientation = HORIZONTAL;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;
    private ItemTransformer mItemTransformer;


    public GalleryLayoutManager(Context context, int orientation) {
        super(context);
        mOrientation = orientation;
    }

    public void attach(int selectedPosition) {
        mCurSelectedPosition = Math.max(0, selectedPosition);
    }

    public void attach(RecyclerView recyclerView, int selectedPosition) {
        if (recyclerView == null) {
            throw new IllegalArgumentException("The attach RecycleView must not null!!");
        }
        GalleryLinearSnapHelper mSnapHelper = new GalleryLinearSnapHelper(recyclerView);
        mCurSelectedPosition = Math.max(0, selectedPosition);
        recyclerView.setLayoutManager(this);
        mSnapHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == VERTICAL) {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            reset();
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (state.isPreLayout() || (state.getItemCount() != 0 && !state.didStructureChange())) {
            return;
        }
        if (getChildCount() == 0 || state.didStructureChange()) {
            reset();
        }
        //去初始化选中索引。注意如果是mInitialSelectedPosition>getItemCount()-1，则取值为getItemCount()-1
        int max = Math.max(0, mCurSelectedPosition);
        //注意这个地方取值
        mCurSelectedPosition = Math.min(max, getItemCount() - 1);
        logger("position+++++" + mCurSelectedPosition);
        detachAndScrapAttachedViews(recycler);
        firstFillCover(recycler, state, 0);
    }


    @Override
    public boolean canScrollHorizontally() {
        return mOrientation == HORIZONTAL;
    }


    @Override
    public boolean canScrollVertically() {
        return mOrientation == VERTICAL;
    }


    /**
     * 该方法作用是启动到适配器位置的平滑滚动，必须重写，否则无法实现滚动
     * @param recyclerView                  recyclerView
     * @param state                         state状态
     * @param position                      position索引
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        GallerySmoothScroller linearSmoothScroller = new GallerySmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
        mCurSelectedPosition = position;
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        final int direction = calculateScrollDirectionForPosition(targetPosition);
        PointF outVector = new PointF();
        if (direction == 0) {
            return null;
        }
        if (mOrientation == HORIZONTAL) {
            outVector.x = direction;
            outVector.y = 0;
        } else {
            outVector.x = 0;
            outVector.y = direction;
        }
        return outVector;
    }

    private void reset() {
        if (mState != null) {
            mState.mItemsFrames.clear();
        }
        mCurSelectedPosition = Math.min(Math.max(0, mCurSelectedPosition), getItemCount() - 1);
        mFirstVisiblePosition = mCurSelectedPosition;
        mLastVisiblePos = mCurSelectedPosition;
    }


    private void firstFillCover(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDelta) {
        //首先判断是横向还是竖向的
        if (mOrientation == HORIZONTAL) {
            firstFillWithHorizontal(recycler, state);
        } else {
            firstFillWithVertical(recycler, state);
        }
        logger("firstFillCover finish:first: " + mFirstVisiblePosition + ",last:" + mLastVisiblePos);
        if (mItemTransformer != null) {
            View child;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                mItemTransformer.transformItem(this, child, calculateToCenterFraction(child, scrollDelta));
            }
        }
    }


    private void firstFillWithHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int leftEdge = getOrientationHelper().getStartAfterPadding();
        int rightEdge = getOrientationHelper().getEndAfterPadding();
        int startPosition = mCurSelectedPosition;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int height = getVerticalSpace();
        int topOffset;
        //layout the init position view
        View scrap = recycler.getViewForPosition(mCurSelectedPosition);
        addView(scrap, 0);
        measureChildWithMargins(scrap, 0, 0);
        scrapWidth = getDecoratedMeasuredWidth(scrap);
        scrapHeight = getDecoratedMeasuredHeight(scrap);
        topOffset = (int) (getPaddingTop() + (height - scrapHeight) / 2.0f);
        int left = (int) (getPaddingLeft() + (getHorizontalSpace() - scrapWidth) / 2.f);
        scrapRect.set(left, topOffset, left + scrapWidth, topOffset + scrapHeight);
        layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
        if (getState().mItemsFrames.get(startPosition) == null) {
            getState().mItemsFrames.put(startPosition, scrapRect);
        } else {
            getState().mItemsFrames.get(startPosition).set(scrapRect);
        }
        mFirstVisiblePosition = mLastVisiblePos = startPosition;
        //fill left of center
        fillLeft(recycler, mCurSelectedPosition - 1, getDecoratedLeft(scrap), leftEdge);
        //fill right of center
        fillRight(recycler, mCurSelectedPosition + 1, getDecoratedRight(scrap), rightEdge);
    }


    private void firstFillWithVertical(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int topEdge = getOrientationHelper().getStartAfterPadding();
        int bottomEdge = getOrientationHelper().getEndAfterPadding();
        int startPosition = mCurSelectedPosition;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int width = getHorizontalSpace();
        int leftOffset;
        //layout the init position view
        View scrap = recycler.getViewForPosition(mCurSelectedPosition);
        addView(scrap, 0);
        measureChildWithMargins(scrap, 0, 0);
        scrapWidth = getDecoratedMeasuredWidth(scrap);
        scrapHeight = getDecoratedMeasuredHeight(scrap);
        leftOffset = (int) (getPaddingLeft() + (width - scrapWidth) / 2.0f);
        int top = (int) (getPaddingTop() + (getVerticalSpace() - scrapHeight) / 2.f);
        scrapRect.set(leftOffset, top, leftOffset + scrapWidth, top + scrapHeight);
        layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
        if (getState().mItemsFrames.get(startPosition) == null) {
            getState().mItemsFrames.put(startPosition, scrapRect);
        } else {
            getState().mItemsFrames.get(startPosition).set(scrapRect);
        }
        mFirstVisiblePosition = mLastVisiblePos = startPosition;
        //fill left of center
        fillTop(recycler, mCurSelectedPosition - 1, getDecoratedTop(scrap), topEdge);
        //fill right of center
        fillBottom(recycler, mCurSelectedPosition + 1, getDecoratedBottom(scrap), bottomEdge);
    }


    private void fillLeft(RecyclerView.Recycler recycler, int startPosition, int startOffset, int leftEdge) {
        View scrap;
        int topOffset;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int height = getVerticalSpace();
        for (int i = startPosition; i >= 0 && startOffset > leftEdge; i--) {
            scrap = recycler.getViewForPosition(i);
            addView(scrap, 0);
            measureChildWithMargins(scrap, 0, 0);
            scrapWidth = getDecoratedMeasuredWidth(scrap);
            scrapHeight = getDecoratedMeasuredHeight(scrap);
            topOffset = (int) (getPaddingTop() + (height - scrapHeight) / 2.0f);
            scrapRect.set(startOffset - scrapWidth, topOffset, startOffset, topOffset + scrapHeight);
            layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
            startOffset = scrapRect.left;
            mFirstVisiblePosition = i;
            if (getState().mItemsFrames.get(i) == null) {
                getState().mItemsFrames.put(i, scrapRect);
            } else {
                getState().mItemsFrames.get(i).set(scrapRect);
            }
        }
    }


    private void fillRight(RecyclerView.Recycler recycler, int startPosition, int startOffset, int rightEdge) {
        View scrap;
        int topOffset;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int height = getVerticalSpace();
        for (int i = startPosition; i < getItemCount() && startOffset < rightEdge; i++) {
            scrap = recycler.getViewForPosition(i);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            scrapWidth = getDecoratedMeasuredWidth(scrap);
            scrapHeight = getDecoratedMeasuredHeight(scrap);
            topOffset = (int) (getPaddingTop() + (height - scrapHeight) / 2.0f);
            scrapRect.set(startOffset, topOffset, startOffset + scrapWidth, topOffset + scrapHeight);
            layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
            startOffset = scrapRect.right;
            mLastVisiblePos = i;
            if (getState().mItemsFrames.get(i) == null) {
                getState().mItemsFrames.put(i, scrapRect);
            } else {
                getState().mItemsFrames.get(i).set(scrapRect);
            }
        }
    }


    private void fillTop(RecyclerView.Recycler recycler, int startPosition, int startOffset, int topEdge) {
        View scrap;
        int leftOffset;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int width = getHorizontalSpace();
        for (int i = startPosition; i >= 0 && startOffset > topEdge; i--) {
            scrap = recycler.getViewForPosition(i);
            addView(scrap, 0);
            measureChildWithMargins(scrap, 0, 0);
            scrapWidth = getDecoratedMeasuredWidth(scrap);
            scrapHeight = getDecoratedMeasuredHeight(scrap);
            leftOffset = (int) (getPaddingLeft() + (width - scrapWidth) / 2.0f);
            scrapRect.set(leftOffset, startOffset - scrapHeight, leftOffset + scrapWidth, startOffset);
            layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
            startOffset = scrapRect.top;
            mFirstVisiblePosition = i;
            if (getState().mItemsFrames.get(i) == null) {
                getState().mItemsFrames.put(i, scrapRect);
            } else {
                getState().mItemsFrames.get(i).set(scrapRect);
            }
        }
    }


    private void fillBottom(RecyclerView.Recycler recycler, int startPosition, int startOffset, int bottomEdge) {
        View scrap;
        int leftOffset;
        int scrapWidth, scrapHeight;
        Rect scrapRect = new Rect();
        int width = getHorizontalSpace();
        for (int i = startPosition; i < getItemCount() && startOffset < bottomEdge; i++) {
            scrap = recycler.getViewForPosition(i);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            scrapWidth = getDecoratedMeasuredWidth(scrap);
            scrapHeight = getDecoratedMeasuredHeight(scrap);
            leftOffset = (int) (getPaddingLeft() + (width - scrapWidth) / 2.0f);
            scrapRect.set(leftOffset, startOffset, leftOffset + scrapWidth, startOffset + scrapHeight);
            layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
            startOffset = scrapRect.bottom;
            mLastVisiblePos = i;
            if (getState().mItemsFrames.get(i) == null) {
                getState().mItemsFrames.put(i, scrapRect);
            } else {
                getState().mItemsFrames.get(i).set(scrapRect);
            }
        }
    }


    private void fillCover(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDelta) {
        if (getItemCount() == 0) {
            return;
        }
        if (mOrientation == HORIZONTAL) {
            fillWithHorizontal(recycler, state, scrollDelta);
        } else {
            fillWithVertical(recycler, state, scrollDelta);
        }
        if (mItemTransformer != null) {
            View child;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                mItemTransformer.transformItem(this, child, calculateToCenterFraction(child, scrollDelta));
            }
        }
    }


    private float calculateToCenterFraction(View child, float pendingOffset) {
        int distance = calculateDistanceCenter(child, pendingOffset);
        int childLength = mOrientation == GalleryLayoutManager.HORIZONTAL ? child.getWidth() : child.getHeight();
        logger("calculateToCenterFraction: distance:" + distance + ",childLength:" + childLength);
        return Math.max(-1.f, Math.min(1.f, distance * 1.f / childLength));
    }

    /**
     * @param child
     * @param pendingOffset child view will scroll by
     * @return
     */
    private int calculateDistanceCenter(View child, float pendingOffset) {
        OrientationHelper orientationHelper = getOrientationHelper();
        int parentCenter = (orientationHelper.getEndAfterPadding() - orientationHelper.getStartAfterPadding()) / 2 + orientationHelper.getStartAfterPadding();
        if (mOrientation == GalleryLayoutManager.HORIZONTAL) {
            return (int) (child.getWidth() / 2 - pendingOffset + child.getLeft() - parentCenter);
        } else {
            return (int) (child.getHeight() / 2 - pendingOffset + child.getTop() - parentCenter);
        }

    }

    /**
     * @param recycler
     * @param state
     * @param dy
     */
    private void fillWithVertical(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        logger("fillWithVertical: dy:" + dy);
        int topEdge = getOrientationHelper().getStartAfterPadding();
        int bottomEdge = getOrientationHelper().getEndAfterPadding();

        //1.remove and recycle the view that disappear in screen
        View child;
        if (getChildCount() > 0) {
            if (dy >= 0) {
                //remove and recycle the top off screen view
                int fixIndex = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    child = getChildAt(i + fixIndex);
                    if (getDecoratedBottom(child) - dy < topEdge) {
                        logger("fillWithVertical: removeAndRecycleView:" + getPosition(child) + ",bottom:" + getDecoratedBottom(child));
                        removeAndRecycleView(child, recycler);
                        mFirstVisiblePosition++;
                        fixIndex--;
                    } else {
                        logger("fillWithVertical: break:" + getPosition(child) + ",bottom:" + getDecoratedBottom(child));
                        break;
                    }
                }
            } else { //dy<0
                //remove and recycle the bottom off screen view
                for (int i = getChildCount() - 1; i >= 0; i--) {
                    child = getChildAt(i);
                    if (getDecoratedTop(child) - dy > bottomEdge) {
                        logger( "fillWithVertical: removeAndRecycleView:" + getPosition(child));
                        removeAndRecycleView(child, recycler);
                        mLastVisiblePos--;
                    } else {
                        break;
                    }
                }
            }

        }
        int startPosition = mFirstVisiblePosition;
        int startOffset = -1;
        int scrapWidth, scrapHeight;
        Rect scrapRect;
        int width = getHorizontalSpace();
        int leftOffset;
        View scrap;
        //2.Add or reattach item view to fill screen
        if (dy >= 0) {
            if (getChildCount() != 0) {
                View lastView = getChildAt(getChildCount() - 1);
                startPosition = getPosition(lastView) + 1;
                startOffset = getDecoratedBottom(lastView);
            }
            for (int i = startPosition; i < getItemCount() && startOffset < bottomEdge + dy; i++) {
                scrapRect = getState().mItemsFrames.get(i);
                scrap = recycler.getViewForPosition(i);
                addView(scrap);
                if (scrapRect == null) {
                    scrapRect = new Rect();
                    getState().mItemsFrames.put(i, scrapRect);
                }
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                leftOffset = (int) (getPaddingLeft() + (width - scrapWidth) / 2.0f);
                if (startOffset == -1 && startPosition == 0) {
                    //layout the first position item in center
                    int top = (int) (getPaddingTop() + (getVerticalSpace() - scrapHeight) / 2.f);
                    scrapRect.set(leftOffset, top, leftOffset + scrapWidth, top + scrapHeight);
                } else {
                    scrapRect.set(leftOffset, startOffset, leftOffset + scrapWidth, startOffset + scrapHeight);
                }
                layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
                startOffset = scrapRect.bottom;
                mLastVisiblePos = i;
                logger("fillWithVertical: add view:" + i + ",startOffset:" + startOffset + ",mLastVisiblePos:" + mLastVisiblePos + ",bottomEdge" + bottomEdge);
            }
        } else {
            //dy<0
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                startPosition = getPosition(firstView) - 1; //前一个View的position
                startOffset = getDecoratedTop(firstView);
            }
            for (int i = startPosition; i >= 0 && startOffset > topEdge + dy; i--) {
                scrapRect = getState().mItemsFrames.get(i);
                scrap = recycler.getViewForPosition(i);
                addView(scrap, 0);
                if (scrapRect == null) {
                    scrapRect = new Rect();
                    getState().mItemsFrames.put(i, scrapRect);
                }
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                leftOffset = (int) (getPaddingLeft() + (width - scrapWidth) / 2.0f);
                scrapRect.set(leftOffset, startOffset - scrapHeight, leftOffset + scrapWidth, startOffset);
                layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
                startOffset = scrapRect.top;
                mFirstVisiblePosition = i;
            }
        }
    }


    /**
     * @param recycler
     * @param state
     */
    private void fillWithHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        int leftEdge = getOrientationHelper().getStartAfterPadding();
        int rightEdge = getOrientationHelper().getEndAfterPadding();
        logger("fillWithHorizontal() called with: dx = [" + dx + "],leftEdge:" + leftEdge + ",rightEdge:" + rightEdge);
        //1.remove and recycle the view that disappear in screen
        View child;
        if (getChildCount() > 0) {
            if (dx >= 0) {
                //remove and recycle the left off screen view
                int fixIndex = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    child = getChildAt(i + fixIndex);
                    if (getDecoratedRight(child) - dx < leftEdge) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisiblePosition++;
                        fixIndex--;
                        logger("fillWithHorizontal:removeAndRecycleView:" + getPosition(child) + " mFirstVisiblePosition change to:" + mFirstVisiblePosition);
                    } else {
                        break;
                    }
                }
            } else { //dx<0
                //remove and recycle the right off screen view
                for (int i = getChildCount() - 1; i >= 0; i--) {
                    child = getChildAt(i);
                    if (getDecoratedLeft(child) - dx > rightEdge) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiblePos--;
                        logger("fillWithHorizontal:removeAndRecycleView:" + getPosition(child) + "mLastVisiblePos change to:" + mLastVisiblePos);
                    }
                }
            }

        }
        //2.Add or reattach item view to fill screen
        int startPosition = mFirstVisiblePosition;
        int startOffset = -1;
        int scrapWidth, scrapHeight;
        Rect scrapRect;
        int height = getVerticalSpace();
        int topOffset;
        View scrap;
        if (dx >= 0) {
            if (getChildCount() != 0) {
                View lastView = getChildAt(getChildCount() - 1);
                startPosition = getPosition(lastView) + 1; //start layout from next position item
                startOffset = getDecoratedRight(lastView);
                logger("fillWithHorizontal:to right startPosition:" + startPosition + ",startOffset:" + startOffset + ",rightEdge:" + rightEdge);
            }
            for (int i = startPosition; i < getItemCount() && startOffset < rightEdge + dx; i++) {
                scrapRect = getState().mItemsFrames.get(i);
                scrap = recycler.getViewForPosition(i);
                addView(scrap);
                if (scrapRect == null) {
                    scrapRect = new Rect();
                    getState().mItemsFrames.put(i, scrapRect);
                }
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                topOffset = (int) (getPaddingTop() + (height - scrapHeight) / 2.0f);
                if (startOffset == -1 && startPosition == 0) {
                    // layout the first position item in center
                    int left = (int) (getPaddingLeft() + (getHorizontalSpace() - scrapWidth) / 2.f);
                    scrapRect.set(left, topOffset, left + scrapWidth, topOffset + scrapHeight);
                } else {
                    scrapRect.set(startOffset, topOffset, startOffset + scrapWidth, topOffset + scrapHeight);
                }
                layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
                startOffset = scrapRect.right;
                mLastVisiblePos = i;
                logger("fillWithHorizontal,layout:mLastVisiblePos: " + mLastVisiblePos);
            }
        } else {
            //dx<0
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                startPosition = getPosition(firstView) - 1; //start layout from previous position item
                startOffset = getDecoratedLeft(firstView);
                logger("fillWithHorizontal:to left startPosition:" + startPosition + ",startOffset:" + startOffset + ",leftEdge:" + leftEdge + ",child count:" + getChildCount());
            }
            for (int i = startPosition; i >= 0 && startOffset > leftEdge + dx; i--) {
                scrapRect = getState().mItemsFrames.get(i);
                scrap = recycler.getViewForPosition(i);
                addView(scrap, 0);
                if (scrapRect == null) {
                    scrapRect = new Rect();
                    getState().mItemsFrames.put(i, scrapRect);
                }
                measureChildWithMargins(scrap, 0, 0);
                scrapWidth = getDecoratedMeasuredWidth(scrap);
                scrapHeight = getDecoratedMeasuredHeight(scrap);
                topOffset = (int) (getPaddingTop() + (height - scrapHeight) / 2.0f);
                scrapRect.set(startOffset - scrapWidth, topOffset, startOffset, topOffset + scrapHeight);
                layoutDecorated(scrap, scrapRect.left, scrapRect.top, scrapRect.right, scrapRect.bottom);
                startOffset = scrapRect.left;
                mFirstVisiblePosition = i;
            }
        }
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }



    private int calculateScrollDirectionForPosition(int position) {
        if (getChildCount() == 0) {
            return LAYOUT_START;
        }
        final int firstChildPos = mFirstVisiblePosition;
        return position < firstChildPos ? LAYOUT_START : LAYOUT_END;
    }


    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // When dx is positive，finger fling from right to left(←)，scrollX+
        if (getChildCount() == 0 || dx == 0) {
            return 0;
        }
        int delta = -dx;
        int parentCenter = (getOrientationHelper().getEndAfterPadding() - getOrientationHelper().getStartAfterPadding()) / 2 + getOrientationHelper().getStartAfterPadding();
        View child;
        if (dx > 0) {
            //If we've reached the last item, enforce limits
            if (getPosition(getChildAt(getChildCount() - 1)) == getItemCount() - 1) {
                child = getChildAt(getChildCount() - 1);
                delta = -Math.max(0, Math.min(dx, (child.getRight() - child.getLeft()) / 2 + child.getLeft() - parentCenter));
            }
        } else {
            //If we've reached the first item, enforce limits
            if (mFirstVisiblePosition == 0) {
                child = getChildAt(0);
                delta = -Math.min(0, Math.max(dx, ((child.getRight() - child.getLeft()) / 2 + child.getLeft()) - parentCenter));
            }
        }
        logger("scrollHorizontallyBy: dx:" + dx + ",fixed:" + delta);
        getState().mScrollDelta = -delta;
        fillCover(recycler, state, -delta);
        offsetChildrenHorizontal(delta);
        return -delta;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        int delta = -dy;
        int parentCenter = (getOrientationHelper().getEndAfterPadding() - getOrientationHelper().getStartAfterPadding()) / 2 + getOrientationHelper().getStartAfterPadding();
        View child;
        if (dy > 0) {
            //If we've reached the last item, enforce limits
            if (getPosition(getChildAt(getChildCount() - 1)) == getItemCount() - 1) {
                child = getChildAt(getChildCount() - 1);
                delta = -Math.max(0, Math.min(dy, (getDecoratedBottom(child) - getDecoratedTop(child)) / 2 + getDecoratedTop(child) - parentCenter));
            }
        } else {
            //If we've reached the first item, enforce limits
            if (mFirstVisiblePosition == 0) {
                child = getChildAt(0);
                delta = -Math.min(0, Math.max(dy, (getDecoratedBottom(child) - getDecoratedTop(child)) / 2 + getDecoratedTop(child) - parentCenter));
            }
        }
        logger("scrollVerticallyBy: dy:" + dy + ",fixed:" + delta);
        getState().mScrollDelta = -delta;
        fillCover(recycler, state, -delta);
        offsetChildrenVertical(delta);
        return -delta;
    }

    private OrientationHelper getOrientationHelper() {
        if (mOrientation == HORIZONTAL) {
            if (mHorizontalHelper == null) {
                mHorizontalHelper = OrientationHelper.createHorizontalHelper(this);
            }
            return mHorizontalHelper;
        } else {
            if (mVerticalHelper == null) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(this);
            }
            return mVerticalHelper;
        }
    }


    public void setItemTransformer(ItemTransformer itemTransformer) {
        mItemTransformer = itemTransformer;
    }
    public interface ItemTransformer {
        void transformItem(GalleryLayoutManager layoutManager, View item, float fraction);
    }

    class State {
        SparseArray<Rect> mItemsFrames;
        int mScrollDelta;
        State() {
            mItemsFrames = new SparseArray<>();
            mScrollDelta = 0;
        }
    }
    private State getState() {
        if (mState == null) {
            mState = new State();
        }
        return mState;
    }

    static int getPosition(){
        logger("position-----"+mCurSelectedPosition);
        return mCurSelectedPosition;
    }

    static void setPosition(int position){
        mCurSelectedPosition = position;
    }

    private static void logger(String log){
        if (BuildConfig.DEBUG){
            Log.e(TAG,log);
        }
    }

}
