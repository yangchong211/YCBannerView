package com.yc.cn.ycbanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yc.cn.ycbanner.test.GalleryLayoutManager;
import com.yc.cn.ycbanner.test.GalleryRecyclerView;
import com.yc.cn.ycbanner.test.GalleryScaleTransformer;

import java.util.ArrayList;

public class EightActivity extends AppCompatActivity {

    private GalleryRecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private ArrayList<String> data = new ArrayList<>();
    int a= 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eight);
        mRecyclerView = (GalleryRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        initRecyclerView();
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a++;
                //mRecyclerView.smoothScrollToPosition(a);
                mRecyclerView.scrollToPosition(a);
                Log.e("onClick",a+"");
            }
        });
        //initRecyclerView2();
    }

    private void initRecyclerView() {
        GalleryLayoutManager manager = new GalleryLayoutManager(LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(mRecyclerView, 2);
        //设置缩放比例因子，在0到1.0之间即可
        GalleryScaleTransformer.scaleDivisor = 0.2f;
        manager.setItemTransformer(new GalleryScaleTransformer());
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                Log.e("onItemSelected-----",position+"");
            }
        });
        mRecyclerView.setLayoutManager(manager);
        Snap2Adapter adapter = new Snap2Adapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.addAll(getData());
        mRecyclerView.setInit(2000,getData().size());
    }

    private void initRecyclerView2() {

    }


    private void initRecyclerView3() {
    }


    private void initRecyclerView4() {
    }



    private ArrayList<String> getData(){
        for (int a=0 ; a<20 ; a++){
            data.add("测试数据"+a);
        }
        return data;
    }
}
