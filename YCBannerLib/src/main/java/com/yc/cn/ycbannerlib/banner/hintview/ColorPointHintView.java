package com.yc.cn.ycbannerlib.banner.hintview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.yc.cn.ycbannerlib.banner.BannerView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : color
 *     revise:
 * </pre>
 */
@SuppressLint("ViewConstructor")
public class ColorPointHintView extends ShapeHintView {

    private int focusColor;
    private int normalColor;

    public ColorPointHintView(Context context, int focusColor, int normalColor) {
        super(context);
        this.focusColor = focusColor;
        this.normalColor = normalColor;
    }

    @Override
    public Drawable makeFocusDrawable() {
        GradientDrawable dotFocus = new GradientDrawable();
        dotFocus.setColor(focusColor);
        dotFocus.setCornerRadius(BannerView.dip2px(getContext(), 4));
        dotFocus.setSize(BannerView.dip2px(getContext(), 8), BannerView.dip2px(getContext(), 8));
        return dotFocus;
    }

    @Override
    public Drawable makeNormalDrawable() {
        GradientDrawable dotNormal = new GradientDrawable();
        dotNormal.setColor(normalColor);
        dotNormal.setCornerRadius(BannerView.dip2px(getContext(), 4));
        dotNormal.setSize(BannerView.dip2px(getContext(), 8), BannerView.dip2px(getContext(), 8));
        return dotNormal;
    }

}
