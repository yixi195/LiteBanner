package com.ysl.litebanner.banner;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.ysl.litebanner.holder.ListBaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 画廊BannerView 关键
 */
public class LBannerView<T> extends RecyclerView implements Runnable {
    private static final String TAG = "LBannerView";
    private Handler mHandler = new Handler();
    private int mDelayedTime = 3000;// Banner 切换时间间隔
    private List<T> mDatas = new ArrayList<>();
    private boolean mIsCanLoop = true;// 是否轮播图片
    private boolean mIsAutoPlay = true;
    private int mCurrentItem = 0;//当前位置
    private OnItemClickListener onItemClickListener;
    private ListBaseAdapter adapter;
    private Context mContext;
    /**
     * 抛掷速度的缩放因子
     */
    private double speedCast = 1;

    /**
     * 按下的X轴坐标
     */
    private float mDownX;

    /**
     * 布局器构建者
     */
    private CoverFlowLayoutManger.Builder mManagerBuilder;

    public LBannerView(Context context) {
        this(context, null);
    }

    public LBannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LBannerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        createManageBuilder();
        setLayoutManager(mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true); //开启重新排序
        setOverScrollMode(OVER_SCROLL_NEVER);  //不显示弧形光晕

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //滑动停止了
                    mCurrentItem = getSelectedPos();
                    mIsAutoPlay = true;
                    //Logger.i(TAG,"位置====" + mCurrentItem);
                } else {
                    mIsAutoPlay = false;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 创建布局构建器
     */
    private void createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverFlowLayoutManger.Builder();
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= speedCast;
        return super.fling(velocityX, velocityY);
    }

    /**
     * 设置抛掷速度  0 ~ 1
     *
     * @param speedCast
     */
    public void setSpeedCast(double speedCast) {
        this.speedCast = speedCast;
    }

    /**
     * 设置是否为普通平面滚动
     *
     * @param isFlat true:平面滚动；false:叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat) {
        createManageBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     *
     * @param greyItem true:Item灰度渐变；false:Item灰度不变
     */
    public void setGreyItem(boolean greyItem) {
        createManageBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     *
     * @param alphaItem true:Item半透渐变；false:Item透明度不变
     */
    public void setAlphaItem(boolean alphaItem) {
        createManageBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item的间隔比例
     *
     * @param intervalRatio Item间隔比例。
     *                      即：item的宽 x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio) {
        createManageBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition(); //计算正在显示的所有Item的中间位置
        if (center < 0) center = 0;
        else if (center > childCount) center = childCount;
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger) getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mIsAutoPlay = false;
                break;
            case MotionEvent.ACTION_DOWN: //按下
                mIsAutoPlay = false;

                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE: //移动
                mIsAutoPlay = false;

                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() - 1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP: //松开
                mIsAutoPlay = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }




    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 设置数据源
     * @param list
     * @param adapter
     */
    public LBannerView setPageDatas(List<T> list,ListBaseAdapter adapter) {
        this.mDatas = list;
        this.adapter = adapter;

        setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mCurrentItem == position) { //是处于当前图， 则响应点击事件
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, mCurrentItem % getDatas().size());
                    }
                    return;
                }

                mCurrentItem = position;
                smoothScrollToPosition(position);
            }
        });
        this.adapter.setDataList(list);
        return this;
    }

    /**
     * 获取数据
     * @return
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 开始轮播
     */
    public LBannerView start() {
        // 如果Adapter为null, 说明还没有设置数据，这个时候不应该轮播Banner   //少于2张不允许滚动
        if (getCoverFlowLayout() == null || mDatas.size() < 2) {
            return this;
        }
        // getCoverFlowLayout().scrollToPosition();
        if (mIsCanLoop) {
            pause();
            mIsAutoPlay = true;
            mHandler.postDelayed(this, mDelayedTime);
        }
        return this;
    }

    /**
     * 停止轮播
     */
    public LBannerView pause() {
        mIsAutoPlay = false;
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
        return this;
    }

    /**
     * 销毁
     */
    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 点击事件
     *
     * @param onItemClickListener
     */
    public LBannerView setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }


    @Override
    public void run() {
        if (mIsAutoPlay) {
            //mCurrentItem = mViewPager.getCurrentItem();
            mCurrentItem++;
            Log.i(TAG,"mCurrentItem++===" + mCurrentItem);
            if (mCurrentItem == mDatas.size()) {
                mCurrentItem = 0;
                //滑动不带过渡效果
                scrollToPosition(mCurrentItem);
                mHandler.postDelayed(this, mDelayedTime);
            } else {
                //默认滑动带过渡效果
                smoothScrollToPosition(mCurrentItem);
                mHandler.postDelayed(this, mDelayedTime);
            }
        } else {
            mHandler.postDelayed(this, mDelayedTime);
        }
    }
}
