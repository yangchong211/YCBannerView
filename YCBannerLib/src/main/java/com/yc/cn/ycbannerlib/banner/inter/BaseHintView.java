package com.yc.cn.ycbannerlib.banner.inter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : 所有指示器的接口
 *     revise:
 * </pre>
 */
public interface BaseHintView {

	void initView(int length, int gravity);

	void setCurrent(int current);
}

