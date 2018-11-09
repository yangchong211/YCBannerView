package com.yc.cn.ycbanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


public class SevenActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private RecyclerView mRecyclerView4;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.recyclerView3);
        mRecyclerView4 = (RecyclerView) findViewById(R.id.recyclerView4);
        initRecyclerView();
        initRecyclerView2();
        initRecyclerView3();
        initRecyclerView4();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        ScrollHelper snapHelper = new ScrollHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        SnapAdapter adapter = new SnapAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.addAll(getData());
    }

    private void initRecyclerView2() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(manager);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView2);
        SnapAdapter adapter = new SnapAdapter(this);
        mRecyclerView2.setAdapter(adapter);
        adapter.addAll(getData());
    }


    private void initRecyclerView3() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView3.setLayoutManager(manager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView3);
        SnapAdapter adapter = new SnapAdapter(this);
        mRecyclerView3.setAdapter(adapter);
        adapter.addAll(getData());
    }


    private void initRecyclerView4() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView4.setLayoutManager(manager);
        ScrollHelper2 snapHelper = new ScrollHelper2();
        snapHelper.attachToRecyclerView(mRecyclerView4);
        SnapAdapter adapter = new SnapAdapter(this);
        mRecyclerView4.setAdapter(adapter);
        adapter.addAll(getData());
    }



    private ArrayList<String> getData(){
        for (int a=0 ; a<20 ; a++){
            data.add("测试数据"+a);
        }
        return data;
    }


}
