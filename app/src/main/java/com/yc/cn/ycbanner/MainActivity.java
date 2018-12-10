package com.yc.cn.ycbanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.cn.ycbannerlib.marquee.MarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MarqueeView marqueeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);
        marqueeView = findViewById(R.id.marqueeView);
        initMarqueeView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initBitmap();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 图片复用，这个属性必须设置；
        options.inMutable = true;
        // 手动设置缩放比例，使其取整数，方便计算、观察数据；
        options.inDensity = 320;
        options.inTargetDensity = 320;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_autumn_tree_min, options);
        // 对象内存地址；
        Log.i("ycBitmap", "bitmap = " + bitmap);
        Log.i("ycBitmap", "ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
        // 使用inBitmap属性，这个属性必须设置；
        options.inBitmap = bitmap; options.inDensity = 320;
        // 设置缩放宽高为原始宽高一半；
        options.inTargetDensity = 160;
        options.inMutable = true;
        Bitmap bitmapReuse = BitmapFactory.decodeResource(getResources(), R.drawable.bg_kites_min, options);
        // 复用对象的内存地址；
        Log.i("ycBitmap", "bitmapReuse = " + bitmapReuse);
        Log.i("ycBitmap", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
        Log.i("ycBitmap", "bitmapReuse：ByteCount = " + bitmapReuse.getByteCount() + ":::bitmapReuse：AllocationByteCount = " + bitmapReuse.getAllocationByteCount());

        //11-26 18:24:07.971 15470-15470/com.yc.cn.ycbanner I/ycBitmap: bitmap = android.graphics.Bitmap@9739bff
        //11-26 18:24:07.972 15470-15470/com.yc.cn.ycbanner I/ycBitmap: bitmap：ByteCount = 4346880:::bitmap：AllocationByteCount = 4346880
        //11-26 18:24:07.994 15470-15470/com.yc.cn.ycbanner I/ycBitmap: bitmapReuse = android.graphics.Bitmap@9739bff
        //11-26 18:24:07.994 15470-15470/com.yc.cn.ycbanner I/ycBitmap: bitmap：ByteCount = 1228800:::bitmap：AllocationByteCount = 4346880
        //11-26 18:24:07.994 15470-15470/com.yc.cn.ycbanner I/ycBitmap: bitmapReuse：ByteCount = 1228800:::bitmapReuse：AllocationByteCount = 4346880
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this, FirstActivity.class));
                break;
            case R.id.tv_2:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            case R.id.tv_3:
                startActivity(new Intent(this, ThirdActivity.class));
                break;
            case R.id.tv_4:
                startActivity(new Intent(this, FourActivity.class));
                break;
            case R.id.tv_5:
                startActivity(new Intent(this, SplashActivity.class));
                break;
            case R.id.tv_6:
                startActivity(new Intent(this, SixActivity.class));
                break;
            case R.id.tv_7:
                startActivity(new Intent(this,SevenActivity.class));
                break;
            case R.id.tv_8:
                startActivity(new Intent(this,EightActivity.class));
                break;
            case R.id.tv_9:
                startActivity(new Intent(this,NightActivity.class));
                break;
            default:
                break;
        }
    }


    private void initMarqueeView() {
        if (marqueeView == null) {
            return;
        }
        ArrayList<String> list = getMarqueeTitle();
        //根据公告字符串列表启动轮播
        marqueeView.startWithList(list);
        //设置点击事件
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {

            }
        });
    }



    public ArrayList<String> getMarqueeTitle() {
        ArrayList<String> list = new ArrayList<>();
        String[] title = getResources().getStringArray(R.array.main_marquee_title);
        SpannableString ss1 = new SpannableString(title[0]);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss1.toString());
        SpannableString ss2 = new SpannableString(title[1]);
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss2.toString());
        SpannableString ss3 = new SpannableString(title[2]);
        ss3.setSpan(new URLSpan("http://www.ximalaya.com/zhubo/71989305/"), 2, title[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss3.toString());
        return list;
    }


}
