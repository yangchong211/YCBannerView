package com.yc.cn.ycbannerlib.banner.hintview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import com.yc.cn.ycbannerlib.LibUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : icon
 *     revise:
 * </pre>
 */
public class IconHintView extends ShapeHintView {

    private int focusResId;
    private int normalResId;
    private int size;


    public IconHintView(Context context, @DrawableRes int focusResId, @DrawableRes int normalResId) {
        this(context, focusResId, normalResId, LibUtils.dip2px(context,32));
    }

    public IconHintView(Context context, @DrawableRes int focusResId,
                        @DrawableRes int normalResId, int size) {
        super(context);
        this.focusResId = focusResId;
        this.normalResId = normalResId;
        this.size = size;
    }

    @Override
    public Drawable makeFocusDrawable() {
        Drawable drawable = getContext().getResources().getDrawable(focusResId);
        if (size>0){
            drawable = zoomDrawable(drawable,size,size);
        }
        return drawable;
    }

    @Override
    public Drawable makeNormalDrawable() {
        Drawable drawable = getContext().getResources().getDrawable(normalResId);
        if (size>0){
            drawable = zoomDrawable(drawable,size,size);
        }
        return drawable;
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldBmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(oldBmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(null, newBmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
