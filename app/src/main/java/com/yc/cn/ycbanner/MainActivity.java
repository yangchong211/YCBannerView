package com.yc.cn.ycbanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.yc.cn.ycbannerlib.marquee.MarqueeView;

import java.util.ArrayList;
import java.util.List;

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
        marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        initMarqueeView();
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
            default:
                break;
        }
    }


    private void initMarqueeView() {
        if (marqueeView == null) {
            return;
        }
        List<CharSequence> list = getMarqueeTitle();
        //根据公告字符串列表启动轮播
        marqueeView.startWithList(list);
        //设置点击事件
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {

            }
        });
    }



    public List<CharSequence> getMarqueeTitle() {
        List<CharSequence> list = new ArrayList<>();
        String[] title = getResources().getStringArray(R.array.main_marquee_title);
        SpannableString ss1 = new SpannableString(title[0]);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss1);
        SpannableString ss2 = new SpannableString(title[1]);
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss2);
        SpannableString ss3 = new SpannableString(title[2]);
        ss3.setSpan(new URLSpan("http://www.ximalaya.com/zhubo/71989305/"), 2, title[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss3);
        return list;
    }


}
