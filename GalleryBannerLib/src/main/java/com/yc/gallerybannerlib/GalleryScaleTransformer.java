package com.yc.gallerybannerlib;


import android.view.View;

import androidx.annotation.FloatRange;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/4/17
 *     desc  : 自定义ItemTransformer，用于动画缩放比例，可以灵活设置
 *     revise:
 * </pre>
 */
public class GalleryScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    //设置缩放比例因子
    private float scaleDivisor = 0.2f;
    private int padding = 30;
    public GalleryScaleTransformer(@FloatRange(from = 0.0f, to = 1.0f) float scaleSize , int padding) {
        this.scaleDivisor = scaleSize;
        this.padding = padding;
    }

    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight()/ 2.0f);
        if (scaleDivisor==0.0f){
            int measuredWidth = item.getMeasuredWidth();
            //Log.d("GalleryScaleTransformer",measuredWidth+"");
            if (padding<0 || padding>measuredWidth){
                padding = 30;
            }
            item.setPadding(padding,0,padding,0);
        }else {
            float scale = 1 - scaleDivisor * Math.abs(fraction);
            //可以在这里对view设置动画效果
            item.setScaleX(scale);
            item.setScaleY(scale);
        }
    }

}
