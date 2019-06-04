package com.ysl.litebanner.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

/**
 * ViewHolder基类
 */
public class SuperViewHolder extends RecyclerView.ViewHolder {

    private final SparseArrayCompat<View> mViews;
    private View mConvertView;

    public SuperViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArrayCompat<>();
        mConvertView = itemView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(@IdRes int viewId) {
        return getView(viewId);
    }
    public TextView getTextView(@IdRes int viewId) {
        return getView(viewId);
    }
    public SuperViewHolder setVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public SuperViewHolder setText(@IdRes int viewId, String str) {
        TextView textView = getTextView(viewId);
        textView.setText(str);
        return this;
    }

    public SuperViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public SuperViewHolder displayImage(@IdRes int viewId, String url) {
        Glide.with(mConvertView.getContext()).load(url).into(getImageView(viewId));
        return this;
    }
}


