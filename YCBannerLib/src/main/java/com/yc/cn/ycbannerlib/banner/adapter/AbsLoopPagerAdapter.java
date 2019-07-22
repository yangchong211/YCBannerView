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
    /**
     * 用来存放View的集合
     */
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

    /**
     * 刷新全部
     */
    @Override
    public void notifyDataSetChanged() {
        mViewList.clear();
        initPosition();
        super.notifyDataSetChanged();
    }

    /**
     * 获取item索引
     * @param object                        objcet
     * @return
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 注册数据观察者监听
     * @param observer                      observer
     */
    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initPosition();
    }

    private void initPosition(){
        if (getRealCount()>1){
            if (mViewPager.getViewPager().getCurrentItem() == 0&&getRealCount()>0){
                int half = Integer.MAX_VALUE/2;
                int start = half - half%getRealCount();
                setCurrent(start);
            }
        }
    }

    /**
     * 设置位置，利用反射实现
     * @param index                         索引
     */
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

    /**
     * 如果页面不是当前显示的页面也不是要缓存的页面，会调用这个方法，将页面销毁。
     * @param container                     container
     * @param position                      索引
     * @param object                        object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     *  要显示的页面或需要缓存的页面，会调用这个方法进行布局的初始化。
     * @param container                     container
     * @param position                      索引
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }

    /**
     * 这个是避免重复创建，如果集合中有，则取集合中的
     * @param container                     container
     * @param position                      索引
     * @return
     */
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
        //设置最大轮播图数量 ，如果是1那么就是1，不轮播；如果大于1则设置一个最大值，可以轮播
        //return getRealCount();
        return getRealCount()<=1?getRealCount(): Integer.MAX_VALUE;
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
