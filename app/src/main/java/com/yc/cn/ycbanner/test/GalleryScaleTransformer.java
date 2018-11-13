package com.yc.cn.ycbanner.test;


import android.view.View;

public class GalleryScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    //设置缩放比例因子
    public static float scaleDivisor = 0.2f;

    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight()/2.0f);
        if (scaleDivisor==0.0f){
            item.setPadding(30,0,30,0);
        }else {
            float scale = 1 - scaleDivisor * Math.abs(fraction);
            //可以在这里对view设置动画效果
            item.setScaleX(scale);
            item.setScaleY(scale);
        }
    }

}
