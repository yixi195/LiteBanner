package com.ysl.litebanner.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.ysl.litebanner.banner.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 封装adapter
 */
public abstract class ListBaseAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = true; //开启动画

    protected List<T> mDataList = new ArrayList<>();

    public ListBaseAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getLayoutId(), parent, false);
        return new SuperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, final int position) {
        onBindItemHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, position);
            }
        });

//        addAnimate(holder,position);
        /*
         * 设置水波纹背景
         */
//        if (holder.itemView.getBackground() == null) {
//            TypedValue typedValue = new TypedValue();
//            Resources.Theme theme = holder.itemView.getContext().getTheme();
//            int top = holder.itemView.getPaddingTop();
//            int bottom = holder.itemView.getPaddingBottom();
//            int left = holder.itemView.getPaddingLeft();
//            int right = holder.itemView.getPaddingRight();
//            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
//                holder.itemView.setBackgroundResource(typedValue.resourceId);
//            }
//            holder.itemView.setPadding(left, top, right, bottom);
//        }
    }

    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            onBindItemHolder(holder, position, payloads);
        }
    }

    //动画开关
    public void setOpenAnimationEnable(boolean enabled) {
        this.mOpenAnimationEnable = enabled;
    }

    //添加动画
    private void addAnimate(SuperViewHolder holder, int postion) {
        if (mOpenAnimationEnable && mLastPosition < postion) {
            holder.itemView.setAlpha(0);
            holder.itemView.animate().alpha(1).start();
            mLastPosition = postion;
        }
    }

    public abstract int getLayoutId();

    public abstract void onBindItemHolder(SuperViewHolder holder, int position);

    public void onBindItemHolder(SuperViewHolder holder, int position, List<Object> payloads) {
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 获取List列表数据
     *
     * @return
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setDataList(Collection<T> list) {
        mLastPosition = -1;
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addAll(Collection<T> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    /**
     * 添加单项数据
     * @param item
     */
    public void add(T item){
        int lastIndex = this.mDataList.size();
        if (this.mDataList.add(item)) {
            notifyItemRangeInserted(lastIndex,1);
        }
    }

    /**
     * 移除某条数据
     *
     * @param position
     */
    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);

        if (position != (getDataList().size())) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.mDataList.size() - position);
        }
    }

    /**
     * 清空列表
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}
