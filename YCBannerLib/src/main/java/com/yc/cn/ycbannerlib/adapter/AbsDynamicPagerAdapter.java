package com.yc.cn.ycbannerlib.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : 动态管理的Adapter。概念参照{@link android.support.v4.app.FragmentPagerAdapter}
 *             每次都会创建新view，销毁旧View。节省内存消耗性能
 *     revise:
 * </pre>
 */
public abstract class AbsDynamicPagerAdapter extends PagerAdapter {

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = getView(container,position);
		container.addView(itemView);
		return itemView;
	}

	public abstract View getView(ViewGroup container, int position);

}
