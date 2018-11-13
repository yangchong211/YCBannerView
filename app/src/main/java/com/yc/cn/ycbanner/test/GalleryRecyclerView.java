package com.yc.cn.ycbanner.test;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;


public class GalleryRecyclerView extends RecyclerView {

    /**
     * 播放延迟
     */
    private int delay;
    private long mRecentTouchTime;
    private Timer timer;
    private int size;
    private static int curSelectedPosition;

    public GalleryRecyclerView(Context context) {
        this(context,null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化
     */
    public void setInit(int delay , int size){
        this.delay = delay;
        this.size = size;
        startPlay();
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

    /**
     * 用静态内部类来防止持有外部类的隐性引用，避免之前总是内存泄漏
     * https://github.com/yangchong211
     */

    private TimeTaskHandler mHandler = new TimeTaskHandler(this);
    private final static class TimeTaskHandler extends Handler {
        int a = 0;
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
                int cur = a++;
                if (cur<0 || cur>=recyclerView.size){
                    cur=0;
                }
                Log.e("handleMessage----",cur+"");
                //recyclerView.scrollToPosition(cur);
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

}
