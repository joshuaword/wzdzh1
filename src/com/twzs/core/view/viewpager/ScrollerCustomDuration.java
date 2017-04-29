package com.twzs.core.view.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 *@author fucheng
 *@date 2013-4-16
 *@comment
 */
public class ScrollerCustomDuration extends Scroller {

    private double mScrollDuration = 1;

    public ScrollerCustomDuration(Context context) {
        super(context);
    }

    public ScrollerCustomDuration(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }


    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDuration(long scrollDuration) {
    	mScrollDuration = scrollDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) mScrollDuration);
    }

}