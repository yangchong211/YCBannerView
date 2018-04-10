package com.yc.cn.ycbannerlib.inter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  :
 *     revise:
 * </pre>
 */
public interface HintViewDelegate {

    /**
     * 设置索引位置
     * @param position              索引
     * @param hintView              HintView
     */
    void setCurrentPosition(int position, BaseHintView hintView);

    /**
     * 初始化view
     * @param length                length
     * @param gravity               位置
     * @param hintView              HintView
     */
    void initView(int length, int gravity, BaseHintView hintView);

}
