package com.yc.cn.ycbanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.yc.cn.ycbannerlib.banner.view.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsDynamicPagerAdapter;



/**
 * Created by yc on 2018/4/10.
 */

public class SplashActivity extends AppCompatActivity {

    private int[] imgs = {
            R.drawable.bg_kites_min,
            R.drawable.bg_autumn_tree_min,
            R.drawable.bg_lake_min,
            R.drawable.bg_leaves_min,
            R.drawable.bg_magnolia_trees_min,
    };
    private BannerView banner;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        initBanner();
        initListener();
    }

    private void initListener() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }


    private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        banner.setPlayDelay(0);
        banner.setAdapter(new ImageNormalAdapter());
        banner.setHintGravity(1);
        banner.setHintPadding(20,0, 20,20);
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        banner.setOnPageListener(new BannerView.OnPageListener() {
            @Override
            public void onPageChange(int position) {
                if(position==imgs.length-1){
                    btn.setVisibility(View.VISIBLE);
                }else {
                    btn.setVisibility(View.GONE);
                }
            }
        });
    }



    private class ImageNormalAdapter extends AbsDynamicPagerAdapter {

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }


}
