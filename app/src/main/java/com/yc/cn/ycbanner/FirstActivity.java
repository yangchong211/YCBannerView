package com.yc.cn.ycbanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yc.cn.ycbannerlib.banner.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsDynamicPagerAdapter;


/**
 * Created by PC on 2017/11/21.
 * 作者：PC
 */

public class FirstActivity extends AppCompatActivity {


    private int[] imgs = {
            R.drawable.bg_kites_min,
            R.drawable.bg_autumn_tree_min,
            R.drawable.bg_lake_min,
            R.drawable.bg_leaves_min,
            R.drawable.bg_magnolia_trees_min,
    };
    private BannerView banner;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(banner!=null){
            //停止轮播
            banner.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(banner!=null){
            //开始轮播
            banner.resume();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initBanner();
    }

    private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        //设置轮播时间
        banner.setPlayDelay(2000);
        //设置轮播图适配器，必须
        banner.setAdapter(new ImageNormalAdapter());
        //设置位置
        banner.setHintGravity(1);
        //设置指示器样式
        banner.setHintMode(BannerView.HintMode.TEXT_HINT);
        //判断轮播是否进行
        boolean playing = banner.isPlaying();
        //轮播图点击事件
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(FirstActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
            }
        });
        //轮播图滑动事件
        banner.setOnPageListener(new BannerView.OnPageListener() {
            @Override
            public void onPageChange(int position) {

            }
        });
    }

    private class ImageNormalAdapter extends AbsDynamicPagerAdapter {

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[position]);
            Bitmap bitmap1 = ImageBitmapUtils.compressImage(bitmap, bitmap.getByteCount()/5);
            view.setImageBitmap(bitmap1);
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }

}
