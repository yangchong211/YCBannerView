package com.yc.cn.ycbannerlib.first;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.yc.cn.ycbannerlib.R;
import com.yc.cn.ycbannerlib.first.adapter.LoopPagerAdapter;
import com.yc.cn.ycbannerlib.first.hintview.BaseHintView;
import com.yc.cn.ycbannerlib.first.hintview.ColorPointHintView;
import com.yc.cn.ycbannerlib.first.util.SizeUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/3/18
 * 描    述：支持轮播和提示的的viewpager
 * 修订历史：
 *          v1.0 16年3月18日
 *          v1.1 17年4月7日
 *          v1.2 17年10月3日修改
 * ================================================
 */
public class BannerView extends RelativeLayout {

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private OnBannerClickListener mOnItemClickListener;
    private GestureDetector mGestureDetector;

	private long mRecentTouchTime;
	private int delay;                          //播放延迟
	private int gravity;                        //hint位置
	private int color;                          //hint颜色
	private int alpha;                          //hint透明度
	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;
	private View mHintView;
	private Timer timer;

	public interface HintViewDelegate{
        void setCurrentPosition(int position, BaseHintView hintView);
        void initView(int length, int gravity, BaseHintView hintView);
    }

    private HintViewDelegate mHintViewDelegate = new HintViewDelegate() {
        @Override
        public void setCurrentPosition(int position,BaseHintView hintView) {
            if(hintView!=null){
                hintView.setCurrent(position);
            }
        }

        @Override
        public void initView(int length, int gravity,BaseHintView hintView) {
            if (hintView!=null){
                hintView.initView(length,gravity);
            }
        }
    };

    /**
     * 让外界在代码中new对象时调用
     * @param context           上下文
     */
	public BannerView(Context context){
		this(context,null);
	}

    /**
     * 在布局文字中配置控件时调用
     * @param context           上下文
     * @param attrs             属性
     */
	public BannerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

    /**
     * 使用样式时调用
     * @param context           上下文
     * @param attrs             属性
     * @param defStyle          样式
     */
	public BannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(attrs);
	}

	/**
	 * 读取提示形式  和   提示位置   和    播放延迟
	 */
	private void initView(AttributeSet attrs){
        //这里要做移除，有时在使用中没有手动销毁，所以初始化时要remove
		if(mViewPager!=null){
			removeView(mViewPager);
		}

		TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
		gravity = type.getInteger(R.styleable.BannerView_hint_gravity, 1);
		delay = type.getInt(R.styleable.BannerView_play_delay, 0);
		color = type.getColor(R.styleable.BannerView_hint_color, Color.BLACK);
		alpha = type.getInt(R.styleable.BannerView_hint_alpha, 0);
		paddingLeft = (int) type.getDimension(R.styleable.BannerView_hint_paddingLeft, 0);
		paddingRight = (int) type.getDimension(R.styleable.BannerView_hint_paddingRight, 0);
		paddingTop = (int) type.getDimension(R.styleable.BannerView_hint_paddingTop, 0);
		paddingBottom = (int) type.getDimension(R.styleable.BannerView_hint_paddingBottom
                , SizeUtil.dip2px(getContext(),4));

		mViewPager = new ViewPager(getContext());
		mViewPager.setId(R.id.banner_inner);
		mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT));
		addView(mViewPager);
		type.recycle();
		initHint(new ColorPointHintView(getContext(), Color.parseColor("#E3AC42")
                , Color.parseColor("#88ffffff")));
        initGestureDetector();
	}


    private void initGestureDetector() {
        //手势处理
        mGestureDetector = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener!=null){
                    if (mAdapter instanceof LoopPagerAdapter){
                        int i = mViewPager.getCurrentItem() % ((LoopPagerAdapter) mAdapter)
                                .getRealCount();
                        mOnItemClickListener.onItemClick(i);
                    }else {
                        mOnItemClickListener.onItemClick(mViewPager.getCurrentItem());
                    }
                }
                return super.onSingleTapUp(e);
            }
        });
    }


    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     * 11月18日，当不设置轮播图布局具体宽高的时候，则轮播图全屏展示
     * 需求：1.当即使不设置具体宽高值，就展现默认值宽高，默认宽是MATCH_PARENT，高是200dp
     *      2.能够根据图片自动展示轮播图宽高。这个认为没必要
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //记录如果是wrap_content是设置的宽和高
        int width ;
        int height;

        View childView = getChildAt(0);
        int cWidth = childView.getMeasuredWidth();
        int cHeight = childView.getMeasuredHeight();
        MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
        //设置位置
        cParams.setMargins(0,0,0,0);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        if (widthMode == MeasureSpec.EXACTLY) {
            width = cWidth;
        } else {
            //为ViewPager设置宽度
            /*ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            mViewPager.setLayoutParams(params);*/
            width = LinearLayout.LayoutParams.MATCH_PARENT;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = cHeight;
        } else {
            //为ViewPager设置高度
            /*ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = SizeUtil.dip2px(getContext(),200);
            mViewPager.setLayoutParams(params);*/
            height = SizeUtil.dip2px(getContext(),200f);
        }
        setMeasuredDimension(width, height);
    }


    //用静态内部类来防止持有外部类的隐性引用，避免之前总是内存泄漏
    //https://github.com/yangchong211
    private final static class TimeTaskHandler extends Handler {
        private WeakReference<BannerView> mRollPagerViewWeakReference;

        TimeTaskHandler(BannerView rollPagerView) {
            this.mRollPagerViewWeakReference = new WeakReference<>(rollPagerView);
        }

        @Override
        public void handleMessage(Message msg) {
            BannerView rollPagerView = mRollPagerViewWeakReference.get();
            int cur = rollPagerView.getViewPager().getCurrentItem()+1;
            if(cur>=rollPagerView.mAdapter.getCount()){
                cur=0;
            }
            rollPagerView.getViewPager().setCurrentItem(cur);
            rollPagerView.mHintViewDelegate.setCurrentPosition(cur,
                    (BaseHintView) rollPagerView.mHintView);
			if (rollPagerView.mAdapter.getCount()<=1){
                rollPagerView.stopPlay();
            }
        }
    }


    private TimeTaskHandler mHandler = new TimeTaskHandler(this);
    private static class WeakTimerTask extends TimerTask {
        private WeakReference<BannerView> mRollPagerViewWeakReference;
        WeakTimerTask(BannerView mRollPagerView) {
            this.mRollPagerViewWeakReference = new WeakReference<>(mRollPagerView);
        }

        @Override
        public void run() {
            BannerView rollPagerView = mRollPagerViewWeakReference.get();
            if (rollPagerView!=null){
                if(rollPagerView.isShown() && System.currentTimeMillis()-rollPagerView
                        .mRecentTouchTime>rollPagerView.delay){
                    rollPagerView.mHandler.sendEmptyMessage(0);
                }
            }else{
                cancel();
            }
        }
    }


	/**
	 * 开始播放
	 * 仅当view正在显示 且 触摸等待时间过后 播放
	 */
	private void startPlay(){
		if(delay<=0||mAdapter==null||mAdapter.getCount()<=1){
			return;
		}
		if (timer!=null){
			timer.cancel();
		}
		timer = new Timer();
		//用一个timer定时设置当前项为下一项
		timer.schedule(new WeakTimerTask(this), delay, delay);
	}


    private void stopPlay(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }


    public void setHintViewDelegate(HintViewDelegate delegate){
        this.mHintViewDelegate = delegate;
    }


	private void initHint(BaseHintView hintView){
		if(mHintView!=null){
			removeView(mHintView);
		}
		if(hintView == null){
			return;
		}
		mHintView = (View) hintView;
		loadHintView();
	}

	/**
	 * 加载hintView的容器
	 */
	private void loadHintView(){
		addView(mHintView);
		mHintView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		((View) mHintView).setLayoutParams(lp);

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setAlpha(alpha);
		mHintView.setBackgroundDrawable(gd);

        mHintViewDelegate.initView(mAdapter == null ? 0 : mAdapter.getCount(),
                gravity, (BaseHintView) mHintView);
	}


	/**
	 * 设置viewPager滑动动画持续时间
     * API>19
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
    public void setAnimationDuration(final int during){
		try {
			// viePager平移动画事件
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			Scroller mScroller = new Scroller(getContext(),
					// 动画效果与ViewPager的一致
                    new Interpolator() {
                        public float getInterpolation(float t) {
                            t -= 1.0f;
                            return t * t * t * t * t + 1.0f;
                        }
                    }) {

                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    // 如果手工滚动,则加速滚动
                    if (System.currentTimeMillis() - mRecentTouchTime > delay) {
                        duration = during;
                    } else {
                        duration /= 2;
                    }
                    super.startScroll(startX, startY, dx, dy, duration);
                }

				@Override
				public void startScroll(int startX, int startY, int dx, int dy) {
					super.startScroll(startX, startY, dx, dy,during);
				}
			};
			mField.set(mViewPager, mScroller);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
    }

    /**
     * 停止轮播
     * 在onPause中调用
     */
    public void pause(){
        stopPlay();
    }

    /**
     * 开始轮播
     * 在onResume中调用
     */
    public void resume(){
        startPlay();
    }

    /**
     * 判断轮播是否进行
     */
    public boolean isPlaying(){
        return timer!=null;
    }

	/**
	 * 取真正的Viewpager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	/**
	 * 设置Adapter
	 */
	public void setAdapter(PagerAdapter adapter){
		adapter.registerDataSetObserver(new JPagerObserver());
		mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position>=0){
                    mHintViewDelegate.setCurrentPosition(position, (BaseHintView) mHintView);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
		mAdapter = adapter;
		dataSetChanged();
    }

	/**
	 * 用来实现adapter的notifyDataSetChanged通知HintView变化
	 */
	private class JPagerObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			dataSetChanged();
		}

		@Override
		public void onInvalidated() {
			dataSetChanged();
		}
	}

	private void dataSetChanged(){
		if(mHintView!=null) {
			mHintViewDelegate.initView(mAdapter.getCount(), gravity,
                    (BaseHintView) mHintView);
			mHintViewDelegate.setCurrentPosition(mViewPager.getCurrentItem(),
                    (BaseHintView) mHintView);
		}
        startPlay();
    }


	/**
	 * 为了实现触摸时和过后一定时间内不滑动,这里拦截
	 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		mRecentTouchTime = System.currentTimeMillis();
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 轮播图点击事件
     */
    public void setOnBannerClickListener(OnBannerClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnBannerClickListener {
        void onItemClick(int position);
    }

    /**-------------------------------------设置相关属性---------------------------------------*/

    /**
     * 设置轮播时间
     */
    public void setPlayDelay(int delay){
        this.delay = delay;
        startPlay();
    }

    /**
     * 设置颜色
     * @param c             色值
     */
    public void setHintColor(int c){
        this.color = c;
        mHintView.setBackgroundColor(c);
    }

    /**
     * 设置位置
     * @param g             位置
     */
    public void setHintGravity(int g){
        this.gravity = g;
        //loadHintView();
        mHintViewDelegate.initView(mAdapter == null ? 0 : mAdapter.getCount(),
                gravity, (BaseHintView) mHintView);
    }

    /**
     * 设置位置
     * @param mode          样式
     */
    /*public void setHintMode(int mode){
        this.mode = mode;
    }*/


    /**
     * 设置提示view的位置
     */
    public void setHintPadding(int left,int top,int right,int bottom){
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
        mHintView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * 设置提示view的透明度
     * @param alpha 0为全透明  255为实心
     */
    public void setHintAlpha(int alpha){
        this.alpha = alpha;
        initHint((BaseHintView)mHintView);
    }

    /**
     * 支持自定义hintView
     * 只需new一个实现HintView的View传进来
     * 会自动将你的view添加到本View里面。重新设置LayoutParams。
     */
    public void setHintView(BaseHintView hintView){
        if (mHintView != null) {
            removeView(mHintView);
        }
        this.mHintView = (View) hintView;
        if (hintView!=null){
            initHint(hintView);
        }
    }


}
