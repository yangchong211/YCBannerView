package com.yc.cn.ycbannerlib.gallery;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/4/17
 *     desc  : 自定义RecyclerView，轮播图，采用builder模式
 *     revise:
 * </pre>
 */
public class GalleryRecyclerView extends RecyclerView {

    /**
     * 播放延迟
     */
    private int delay;
    /**
     * 触摸轮播图时间戳
     */
    private long mRecentTouchTime;
    /**
     * timer
     */
    private Timer timer;
    /**
     * 轮播图数量
     */
    private int size;
    /**
     * 滑动速度
     */
    private int mFlingSpeed = 1000;
    private RecyclerView.Adapter adapter;
    private boolean mCallbackInFling = false;
    private int mSelectedPosition;
    private LinearSnapHelper mSnapHelper;


    public GalleryRecyclerView(Context context) {
        this(context,null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        //异常情况保存重要信息。暂不操作
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        // 轮播图用在fragment中，如果是横竖屏切换（Fragment销毁），不应该走smoothScrollToPosition(0)
        // 因为这个方法会导致ScrollManager的onHorizontalScroll不断执行，而ScrollManager.mConsumeX已经重置，会导致这个值紊乱
        // 而如果走scrollToPosition(0)方法，则不会导致ScrollManager的onHorizontalScroll执行，
        // 所以ScrollManager.mConsumeX这个值不会错误
        // 从索引0处开始轮播
        smoothScrollToPosition(0);
        // 但是因为不走ScrollManager的onHorizontalScroll，所以不会执行切换动画，
        // 所以就调用smoothScrollBy(int dx, int dy)，让item轻微滑动，触发动画
        smoothScrollBy(10, 0);
        smoothScrollBy(0, 0);
        startPlay();
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = balanceVelocity(velocityX);
        velocityY = balanceVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    /**
     * 返回滑动速度值
     */
    private int balanceVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, mFlingSpeed);
        } else {
            return Math.max(velocity, -mFlingSpeed);
        }
    }

    /**
     * 开始播放
     * 仅当view正在显示 且 触摸等待时间过后 播放
     */
    private void startPlay(){
        if(delay<=0||size<=1){
            return;
        }
        if (timer!=null){
            timer.cancel();
        }
        timer = new Timer();
        //用一个timer定时设置当前项为下一项
        timer.schedule(new WeakTimerTask(this), delay, delay);
    }


    /**
     * 停止轮播
     */
    private void stopPlay(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }


    public void release(){
        stopPlay();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        clearOnScrollListeners();
    }

    /**
     * 用静态内部类来防止持有外部类的隐性引用，避免之前总是内存泄漏
     * https://github.com/yangchong211
     */

    private TimeTaskHandler mHandler = new TimeTaskHandler(this);

    private final static class TimeTaskHandler extends Handler {
        private WeakReference<GalleryRecyclerView> mGalleryRecyclerView;
        TimeTaskHandler(GalleryRecyclerView rollPagerView) {
            this.mGalleryRecyclerView = new WeakReference<>(rollPagerView);
        }

        @Override
        public void handleMessage(Message msg) {
            GalleryRecyclerView recyclerView = mGalleryRecyclerView.get();
            //注意这个地方需要添加非空判断
            if (recyclerView!=null){
                //Log.e("handleMessage----",curSelectedPosition+"");
                //如果cur大于或等于轮播图数量，那么播放到最后一张后时，接着轮播便是索引为0的图片
                int cur = GalleryLayoutManager.getPosition()+1;
                Log.e("handleMessage----------",cur+"---" + recyclerView.size);
                if (cur<0 || cur>=recyclerView.size){
                    cur=0;
                }
                Log.e("handleMessage----",cur+"");
                recyclerView.smoothScrollToPosition(cur);
                //假如说轮播图只有一张，那么就停止轮播
                if (recyclerView.size<=1){
                    recyclerView.stopPlay();
                }
            }
        }
    }


    private static class WeakTimerTask extends TimerTask {
        private WeakReference<GalleryRecyclerView> mGalleryRecyclerView;
        WeakTimerTask(GalleryRecyclerView recyclerView) {
            this.mGalleryRecyclerView = new WeakReference<>(recyclerView);
        }

        @Override
        public void run() {
            GalleryRecyclerView recyclerView = mGalleryRecyclerView.get();
            if (recyclerView!=null){
                if(recyclerView.isShown() && System.currentTimeMillis()-
                        recyclerView.mRecentTouchTime>recyclerView.delay){
                    recyclerView.mHandler.sendEmptyMessage(0);
                }
            }else{
                cancel();
            }
        }
    }


    /**
     * 为了实现触摸时和过后一定时间内不滑动,这里拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mRecentTouchTime = System.currentTimeMillis();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            startPlay();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stopPlay();
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 设置滑动速度（像素/s）
     */
    public GalleryRecyclerView setFlingSpeed(@IntRange(from = 0) int speed) {
        this.mFlingSpeed = speed;
        return this;
    }

    public GalleryRecyclerView setDataAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    /**
     * 播放间隔时间 ms
     */
    public GalleryRecyclerView setDelayTime(@IntRange(from = 0) int interval) {
        this.delay = interval;
        return this;
    }

    /**
     * 轮播图数量
     */
    public GalleryRecyclerView setSize(@IntRange(from = 0) int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置是否
     */
    public GalleryRecyclerView setCallbackInFling(boolean callbackInFling) {
        this.mCallbackInFling = callbackInFling;
        return this;
    }

    public GalleryRecyclerView setSelectedPosition(int position){
        this.mSelectedPosition = position;
        return this;
    }

    public GalleryRecyclerView setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
        return this;
    }

    /**
     * 装载
     * 注意要点：recyclerView轮播要是无限轮播，必须设置两点
     * 第一处是getItemCount() 返回的是Integer.MAX_VALUE。这是因为广告轮播图是无限轮播，getItemCount()
     * 返回的是Adapter中的总项目数，这样才能使RecyclerView能一直滚动。
     *
     * 第二处是onBindViewHolder()中的 position%list.size() ，表示position对图片列表list取余，
     * 这样list.get(position%list.size())才能按顺序循环展示图片。
     */
    public void setUp() {
        setAdapter(adapter);
        if (getAdapter().getItemCount() <= 0) {
            return;
        }
        GalleryLayoutManager manager = new GalleryLayoutManager(LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(mSelectedPosition);
        //设置缩放比例因子，在0到1.0之间即可
        manager.setItemTransformer(new GalleryScaleTransformer( 0.2f));
        setLayoutManager(manager);
        mSnapHelper = new GalleryLinearSnapHelper(this);
        mSnapHelper.attachToRecyclerView(this);
        addOnScrollListener(new InnerScrollListener());
        startPlay();
    }

    public int getCurrentItem() {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        View snapView = mSnapHelper.findSnapView(layoutManager);
        if (snapView != null) {
            return layoutManager.getPosition(snapView);
        } else {
            return mSelectedPosition;
        }
    }

    private class InnerScrollListener extends RecyclerView.OnScrollListener {
        int mState;
        boolean mCallbackOnIdle;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View snap = mSnapHelper.findSnapView(recyclerView.getLayoutManager());
            if (snap != null) {
                int selectedPosition = recyclerView.getLayoutManager().getPosition(snap);
                if (selectedPosition != GalleryLayoutManager.getPosition()) {
                    GalleryLayoutManager.setPosition(selectedPosition);
                    if (!mCallbackInFling && mState != RecyclerView.SCROLL_STATE_IDLE) {
                        mCallbackOnIdle = true;
                        return;
                    }
                    if (mOnItemSelectedListener != null) {
                        mOnItemSelectedListener.onItemSelected(recyclerView, snap, GalleryLayoutManager.getPosition());
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mState = newState;
            if (mState == RecyclerView.SCROLL_STATE_IDLE) {
                View snap = mSnapHelper.findSnapView(recyclerView.getLayoutManager());
                if (snap != null) {
                    int selectedPosition = recyclerView.getLayoutManager().getPosition(snap);
                    if (selectedPosition != GalleryLayoutManager.getPosition()) {
                        GalleryLayoutManager.setPosition(selectedPosition);
                        if (mOnItemSelectedListener != null) {
                            mOnItemSelectedListener.onItemSelected(recyclerView, snap, GalleryLayoutManager.getPosition());
                        }
                    } else if (!mCallbackInFling && mOnItemSelectedListener != null && mCallbackOnIdle) {
                        mCallbackOnIdle = false;
                        mOnItemSelectedListener.onItemSelected(recyclerView, snap, GalleryLayoutManager.getPosition());
                    }
                }
            }
        }
    }

    private OnItemSelectedListener mOnItemSelectedListener;
    public interface OnItemSelectedListener {
        void onItemSelected(RecyclerView recyclerView, View item, int position);
    }

}
