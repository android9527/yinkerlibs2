package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

import pl.droidsonroids.gif.GifDrawable;

public class GifLoadingLayout extends LoadingLayout {

    private GifDrawable gifDrawable;

    /**
     * @param context
     * @param mode
     * @param scrollDirection
     * @param attrs
     */
    public GifLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                            PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mHeaderImage.setImageResource(R.drawable.loading);
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.gif);

            // NullPointException
            mHeaderImage.setImageDrawable(gifDrawable);
            gifDrawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.gif;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }


    @Override
    protected void onPullImpl(float scaleOfLayout) {
        gifDrawable.start();
    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {
//        mHeaderImage.setVisibility(View.VISIBLE);
//        mHeaderImage.clearAnimation();
        if(gifDrawable != null){
            gifDrawable.stop();
            gifDrawable.reset();
        }
    }


}