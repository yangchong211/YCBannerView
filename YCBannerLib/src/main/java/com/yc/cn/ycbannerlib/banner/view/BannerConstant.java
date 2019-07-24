package com.yc.cn.ycbannerlib.banner.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BannerConstant {


    /**
     * 不建议用枚举，可以用注解替代
     * 轮播图红点是0，数字是1
     * 后期还可以加入其他的
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface HintMode {
        int COLOR_POINT_HINT = 0;
        int TEXT_HINT = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HintGravity {
        int LEFT = 1;
        int CENTER = 2;
        int RIGHT = 3;
    }

}
