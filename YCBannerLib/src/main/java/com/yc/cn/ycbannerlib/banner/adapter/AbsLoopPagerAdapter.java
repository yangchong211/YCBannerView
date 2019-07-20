package com.yc.cn.ycbannerlib.banner.adapter;

import android.annotation.TargetApi;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yc.cn.ycbannerlib.banner.BannerView;
import com.yc.cn.ycbannerlib.banner.inter.BaseHintView;
import com.yc.cn.ycbannerlib.banner.inter.HintViewDelegate;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : AbsLoopPagerAdapter
 *     revise: 如果是自动轮播图的话就用这一个
 * </pre>
 */
public abstract class AbsLoopPagerAdapter extends PagerAdapter {

    private BannerView mViewPager;

    private ArrayList<View> mViewList = new ArrayList<>();

    private class LoopHintViewDelegate implements HintViewDelegate {
        @Override
        public void setCurrentPosition(int position, BaseHintView hintView) {
            if (hintView!=null&&getRealCount()>0){
                hintView.setCurrent(position%getRealCount());
            }
        }

        @Override
        public void initView(int length, int gravity, BaseHintView hintView) {
            if (hintView!=null){
                hintView.initView(getRealCount(),gravity);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mViewList.clear();
        initPosition();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initPosition();
    }

    private void initPosition(){
        if (mViewPager.getViewPager().getCurrentItem() == 0&&getRealCount()>0){
            int half = Integer.MAX_VALUE/2;
            int start = half - half%getRealCount();
            setCurrent(start);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setCurrent(int index){
        try {
            Field field = ViewPager.class.getDeclaredField("mCurItem");
            field.setAccessible(true);
            field.set(mViewPager.getViewPager(),index);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public AbsLoopPagerAdapter(BannerView viewPager){
        this.mViewPager = viewPager;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container, int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }


    @Deprecated
    @Override
    public final int getCount() {
        //设置最大轮播图数量
        return getRealCount()<=0?getRealCount(): Integer.MAX_VALUE;
    }

    /**
     * 获取轮播图数量
     * @return                          数量
     */
    public abstract int getRealCount();

    /**
     * 创建view
     * @param container                 viewGroup
     * @param position                  索引
     * @return
     */
    public abstract View getView(ViewGroup container, int position);

}
