package com.yc.cn.ycbanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yc.cn.ycbannerlib.BannerView;
import com.yc.cn.ycbannerlib.adapter.AbsLoopPagerAdapter;
import com.yc.cn.ycbannerlib.inter.OnBannerClickListener;
import com.yc.cn.ycbannerlib.util.SizeUtil;

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
        banner.setHintPadding(0, SizeUtil.dip2px(this,10f),0,SizeUtil.dip2px(this,10f));
        banner.setPlayDelay(2000);
        banner.setAdapter(new ImageNormalAdapter(banner));
        banner.setOnBannerClickListener(new OnBannerClickListener() {
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
            view.setImageResource(imgs[position]);
            return view;
        }


        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }

}
