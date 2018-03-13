package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

public class FrameLoadingLayout extends LoadingLayout {

    private AnimationDrawable anim;

    /**
     * @param context
     * @param mode
     * @param scrollDirection
     * @param attrs
     */
    public FrameLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                              PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mHeaderImage.setImageResource(R.drawable.loading);
        anim = (AnimationDrawable) mHeaderImage.getDrawable();
        anim.start();

    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.dropdown_loading_00;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }


    @Override
    protected void onPullImpl(float scaleOfLayout) {


    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
        anim.start();
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderImage.clearAnimation();

    }


}