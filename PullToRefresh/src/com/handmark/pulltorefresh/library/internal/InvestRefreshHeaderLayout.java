package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

/**
 * Created by chenfeiyue on 17/3/31.
 *
 */
public class InvestRefreshHeaderLayout extends LoadingLayout {

    private AnimationDrawable mAnimDrawable;
    private LinearLayout myInnerLayout;

    private ImageView mPullToRefreshHeader;

    private TextView mRefreshState;

    public InvestRefreshHeaderLayout(Context context, final PullToRefreshBase.Mode mode, final PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

        mInnerLayout.setVisibility(GONE);

        LayoutInflater.from(context).inflate(R.layout.header_loading_layout, this);
        myInnerLayout = (LinearLayout) findViewById(R.id.fl_inner_header);
        mPullToRefreshHeader = (ImageView) myInnerLayout.findViewById(R.id.pull_to_refresh_header);
        LayoutParams lp = (LayoutParams) myInnerLayout.getLayoutParams();
        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
                break;
            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
                break;
        }
        TextView refreshTitle = (TextView) myInnerLayout.findViewById(R.id.tv_title);
        mRefreshState = (TextView) myInnerLayout.findViewById(R.id.tv_state);
        refreshTitle.setText(R.string.me_refresh_title);
        refreshTitle.setTextColor(Color.parseColor("#666666"));
        mRefreshState.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public int getContentSize() {
        switch (mScrollDirection) {
            case HORIZONTAL:
                return myInnerLayout.getWidth();
            case VERTICAL:
            default:
                return myInnerLayout.getHeight();
        }
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.pull_down_01;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    // 下拉拖动时的回调
    @Override
    protected void onPullImpl(float scaleOfLayout) {

        if (scaleOfLayout >= 0 && scaleOfLayout < 0.15) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_01);
        } else if (scaleOfLayout >= 0.15 && scaleOfLayout < 0.20) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_02);
        } else if (scaleOfLayout >= 0.20 && scaleOfLayout < 0.25) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_03);
        } else if (scaleOfLayout >= 0.25 && scaleOfLayout < 0.30) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_04);
        } else if (scaleOfLayout >= 0.30 && scaleOfLayout < 0.35) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_05);
        } else if (scaleOfLayout >= 0.35 && scaleOfLayout < 0.40) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_06);
        } else if (scaleOfLayout >= 0.40 && scaleOfLayout < 0.45) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_07);
        } else if (scaleOfLayout >= 0.45 && scaleOfLayout < 0.50) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_08);
        } else if (scaleOfLayout >= 0.50 && scaleOfLayout < 0.55) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_09);
        } else if (scaleOfLayout >= 0.55 && scaleOfLayout < 0.60) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_10);
        } else if (scaleOfLayout >= 0.60 && scaleOfLayout < 0.65) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_11);
        } else if (scaleOfLayout >= 0.65 && scaleOfLayout < 0.70) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_12);
        } else if (scaleOfLayout >= 0.70 && scaleOfLayout < 0.75) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_13);
        } else if (scaleOfLayout >= 0.75 && scaleOfLayout < 0.80) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_14);
        } else if (scaleOfLayout >= 0.80 && scaleOfLayout < 0.85) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_15);
        } else if (scaleOfLayout >= 0.85 && scaleOfLayout < 0.90) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_16);
        } else if (scaleOfLayout >= 0.90 && scaleOfLayout < 0.95) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_17);
        } else {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_down_18);
        }

//        mRefreshState.setText(R.string.pull_to_refresh_pull_label);

    }

    // 开始下拉时的回调
    @Override
    protected void pullToRefreshImpl() {
        if (mRefreshState != null) {
            mRefreshState.setText(R.string.pull_to_refresh_pull_label);
        }
    }

    // 释放后刷新时的回调
    @Override
    protected void refreshingImpl() {
        if (mRefreshState != null) {
            mRefreshState.setText(R.string.pull_to_refresh_refreshing_label);
        }

        if (mPullToRefreshHeader != null) {
            mPullToRefreshHeader.setImageResource(R.drawable.pull_to_refresh_rotate);
            mAnimDrawable = (AnimationDrawable) mPullToRefreshHeader.getDrawable();
            mAnimDrawable.start();
        }
    }

    // "加载头部"完全显示时的回调, 释放刷新
    @Override
    protected void releaseToRefreshImpl() {
        if (mRefreshState != null) {
            mRefreshState.setText(R.string.pull_to_refresh_release_label);
        }
    }

    /**
     * 刷新成功
     */
    @Override
    public void onHeadRefreshSuccess() {
        if (mRefreshState != null) {
            mRefreshState.setText(R.string.pull_to_refresh_refresh_success);
        }
        stopAnimation();
        if (mPullToRefreshHeader != null) {
            mPullToRefreshHeader.setImageResource(R.drawable.rotate_17);
        }
    }

    @Override
    protected void resetImpl() {
        stopAnimation();
    }

    private void stopAnimation() {
        if (mAnimDrawable != null) {
            mAnimDrawable.stop();
            mAnimDrawable = null;
        }
    }
}
