package com.yc.cn.ycbanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.cn.ycbannerlib.banner.view.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsLoopPagerAdapter;

/**
 * Created by PC on 2017/11/21.
 * 作者：PC
 */

public class ThirdActivity extends AppCompatActivity {

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
            banner.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(banner!=null){
            banner.resume();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        initBanner();
    }

    private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        banner.setHintColor(Color.GRAY);
        banner.setHintGravity(Gravity.RIGHT);
        banner.setAnimationDuration(1000);
        banner.setHintPadding(0, 20,0,20);
        banner.setPlayDelay(2000);
        banner.setAdapter(new ImageNormalAdapter(banner));
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ThirdActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class ImageNormalAdapter extends AbsLoopPagerAdapter {

        ImageNormalAdapter(BannerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[position]);
            Bitmap bitmap1 = ImageBitmapUtils.compressByQuality1(bitmap, 10240,false);
            view.setImageBitmap(bitmap1);
            return view;
        }


        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }

}
