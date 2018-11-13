package com.yc.cn.ycbanner;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.cn.ycbaseadapterlib.itemType.BaseMViewHolder;
import com.yc.cn.ycbaseadapterlib.itemType.RecyclerArrayAdapter;

public class Snap2Adapter extends RecyclerArrayAdapter<String>{




    public Snap2Adapter(Context context) {
        super(context);
    }

    @Override
    public BaseMViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    private class MyViewHolder extends BaseMViewHolder<String> {

        private final ImageView imageView;
        private final TextView textView;

        MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_snap2);
            imageView = getView(R.id.iv_image);
            textView = getView(R.id.tv_title);
        }

        @Override
        public void setData(String data) {
            super.setData(data);
            imageView.setBackgroundResource(R.drawable.bg_kites_min);
            textView.setText(data);
        }
    }
}
