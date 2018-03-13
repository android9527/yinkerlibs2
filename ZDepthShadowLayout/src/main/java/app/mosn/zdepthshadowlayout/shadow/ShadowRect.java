package app.mosn.zdepthshadowlayout.shadow;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import app.mosn.zdepthshadowlayout.ZDepthParam;

public class ShadowRect implements Shadow {

    private ShapeDrawable mTopShadow;
    private ShapeDrawable mBottomShadow;

    private Rect mRectTopShadow;
    private Rect mRectBottomShadow;

    public ShadowRect() {
        mRectTopShadow = new Rect();
        mRectBottomShadow = new Rect();
        mTopShadow = new ShapeDrawable(new RectShape());
        mBottomShadow = new ShapeDrawable(new RectShape());
    }

    @Override
    public void setParameter(ZDepthParam param, int left, int top, int right, int bottom) {
        mRectTopShadow.left = (int) (left - param.mOffsetYTopShadowPx);
        mRectTopShadow.top = (int) (top - param.mOffsetYTopShadowPx);
        mRectTopShadow.right = (int) (right + param.mOffsetYTopShadowPx);
        mRectTopShadow.bottom = (int) (bottom + param.mOffsetYTopShadowPx);

        mRectBottomShadow.left = (int) (left - param.mOffsetYBottomShadowPx);
        mRectBottomShadow.top = (int) (top - param.mOffsetYBottomShadowPx);
        mRectBottomShadow.right = (int) (right + param.mOffsetYBottomShadowPx);
        mRectBottomShadow.bottom = (int) (bottom + param.mOffsetYBottomShadowPx);

        mTopShadow.getPaint().setColor(Color.argb(param.mAlphaTopShadow, 0, 0, 0));
        if (0 < param.mBlurTopShadowPx) {
            mTopShadow.getPaint().setMaskFilter(new BlurMaskFilter(param.mBlurTopShadowPx, BlurMaskFilter.Blur.NORMAL));
        } else {
            mTopShadow.getPaint().setMaskFilter(null);
        }

        mBottomShadow.getPaint().setColor(Color.argb(param.mAlphaBottomShadow, 0, 0, 0));
//        mBottomShadow.getPaint().setColor(Color.argb(param.mAlphaBottomShadow, 255, 0, 0));
        if (0 < param.mBlurBottomShadowPx) {
            mBottomShadow.getPaint().setMaskFilter(new BlurMaskFilter(param.mBlurBottomShadowPx, BlurMaskFilter.Blur.NORMAL));
        } else {
            mBottomShadow.getPaint().setMaskFilter(null);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mRectBottomShadow, mBottomShadow.getPaint());
        canvas.drawRect(mRectTopShadow, mTopShadow.getPaint());
    }
}
