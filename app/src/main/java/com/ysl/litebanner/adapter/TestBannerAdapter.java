package com.ysl.litebanner.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ysl.litebanner.R;
import com.ysl.litebanner.holder.ListBaseAdapter;
import com.ysl.litebanner.holder.SuperViewHolder;
import com.ysl.litebanner.mode.Banner;
/**
 * Banner适配器
 */
public class TestBannerAdapter extends ListBaseAdapter<Banner> {

    public TestBannerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_banner;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView img = holder.getImageView(R.id.img);
        Glide.with(mContext).load(mDataList.get(position % mDataList.size()).getImg()).into(img);
    }

    @Override
    public int getItemCount() {
        //返回最大值 达到无限轮播
        return Integer.MAX_VALUE;
    }
}
