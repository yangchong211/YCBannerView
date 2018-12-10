package com.yc.cn.ycbannerlib;


import android.content.Context;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/4/17
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public class LibUtils {

    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
