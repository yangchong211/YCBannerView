package com.yc.cn.ycbanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.cn.ycbannerlib.banner.view.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;
import com.yc.cn.ycbannerlib.banner.hintview.IconHintView;

/**
 * Created by PC on 2017/11/21.
 * 作者：PC
 */

public class SixActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_four);

        initBanner();
    }

    private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        banner.setAnimationDuration(1000);
        banner.setHintGravity(1);
        banner.setHintPadding(0, 20,0,20);
        banner.setPlayDelay(2000);
        banner.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        banner.setAdapter(new ImageNormalAdapter());
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(SixActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class ImageNormalAdapter extends AbsStaticPagerAdapter {

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[position]);
            Bitmap bitmap1 = ImageBitmapUtils.compressByScale(bitmap,2.0f,2.0f,false);
            view.setImageBitmap(bitmap1);
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }

}
