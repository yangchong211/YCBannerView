package com.yc.cn.ycbanner;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.yc.ycutilslib.blurView.blur.CustomBlur;
import com.yc.gallerybannerlib.GalleryLayoutManager;
import com.yc.gallerybannerlib.GalleryLinearSnapHelper;
import com.yc.gallerybannerlib.GalleryRecyclerView;
import com.yc.gallerybannerlib.GalleryScaleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

public class NightActivity extends AppCompatActivity {

    private GalleryRecyclerView mRecyclerView;
    private GalleryRecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private ArrayList<Integer> data = new ArrayList<>();
    private FrameLayout fl_container;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.release();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);
        StateAppBar.translucentStatusBar(this,true);
        mRecyclerView = findViewById(R.id.recyclerView);
        fl_container = findViewById(R.id.fl_container);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        initRecyclerView();
        initRecyclerView2();
        initRecyclerView3();
    }

    private void initRecyclerView() {
        Snap3Adapter adapter = new Snap3Adapter(this);
        adapter.setData(getData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFlingSpeed(15000);
        mRecyclerView.setOnItemSelectedListener(new GalleryRecyclerView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                Log.e("onItemSelected-----", position + "");
                //设置高斯模糊背景
                setBlurImage(true);
            }
        });
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(100);
        //设置缩放比例因子，在0到1.0之间即可
        manager.setItemTransformer(new GalleryScaleTransformer( 0.2f,30));
        mRecyclerView.setLayoutManager(manager);
        GalleryLinearSnapHelper mSnapHelper = new GalleryLinearSnapHelper(mRecyclerView);
        mSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.onStart();
    }

    private void initRecyclerView2() {
        Snap3Adapter adapter = new Snap3Adapter(this);
        adapter.setData(getData());
        recyclerView2.setAdapter(adapter);
        recyclerView2.setFlingSpeed(8000);
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(100);
        //设置缩放比例因子，在0到1.0之间即可
        manager.setItemTransformer(new GalleryScaleTransformer( 0.0f,30));
        recyclerView2.setLayoutManager(manager);
        GalleryLinearSnapHelper mSnapHelper = new GalleryLinearSnapHelper(recyclerView2);
        mSnapHelper.attachToRecyclerView(recyclerView2);
        recyclerView2.onStart();
    }


    private void initRecyclerView3() {
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        manager.attach(recyclerView3,100);
        manager.setItemTransformer(new GalleryScaleTransformer( 0.0f,30));
        recyclerView3.setLayoutManager(manager);
        Snap3Adapter adapter = new Snap3Adapter(this);
        adapter.setData(getData());
        recyclerView3.setAdapter(adapter);
    }


    private ArrayList<Integer> getData(){
        data.clear();
        TypedArray bannerImage = getResources().obtainTypedArray(R.array.banner_image);
        for (int i = 0; i < 12 ; i++) {
            int image = bannerImage.getResourceId(i, R.drawable.beauty1);
            data.add(image);
        }
        bannerImage.recycle();
        return data;
    }


    /**
     * 获取虚化背景的位置
     */
    private int mLastDraPosition = -1;
    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    /**
     * 设置背景高斯模糊
     */
    public void setBlurImage(boolean forceUpdate) {
        final Snap3Adapter adapter = (Snap3Adapter) mRecyclerView.getAdapter();
        final int mCurViewPosition = mRecyclerView.getCurrentItem();

        boolean isSamePosAndNotUpdate = (mCurViewPosition == mLastDraPosition) && !forceUpdate;

        if (adapter == null || mRecyclerView == null || isSamePosAndNotUpdate) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                // 获取当前位置的图片资源ID
                int resourceId = adapter.getData().get(mCurViewPosition%adapter.getData().size());
                // 将该资源图片转为Bitmap
                Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
                // 将该Bitmap高斯模糊后返回到resBlurBmp
                Bitmap resBlurBmp = CustomBlur.apply(mRecyclerView.getContext(), resBmp, 10);
                // 再将resBlurBmp转为Drawable
                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
                // 获取前一页的Drawable
                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

                /* 以下为淡入淡出效果 */
                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                fl_container.setBackgroundDrawable(transitionDrawable);
                transitionDrawable.startTransition(500);

                // 存入到cache中
                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
                // 记录上一次高斯模糊的位置
                mLastDraPosition = mCurViewPosition;
            }
        });
    }



}
