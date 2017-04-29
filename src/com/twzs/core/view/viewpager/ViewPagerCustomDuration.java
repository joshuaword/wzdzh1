package com.twzs.core.view.viewpager;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

/**
 *@author fucheng
 *@date 2013-4-16
 *@comment 通过反射方式，使ViewPager 支持滚动（自定义时长）操作
 *  同时支持定时滚动切换功能
 */
public class ViewPagerCustomDuration extends ViewPager implements SlidingMenuItem {
    
    //定时器，定时滚动
    private Handler handler;
    
    private long timeSpan = 3000;
    
    public ViewPagerCustomDuration(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }
    
    public ViewPagerCustomDuration(Context context) {
        super(context);
        postInitViewPager();
    }
    
    private ScrollerCustomDuration mScroller = null;
    
    /**
     * 通过反射重写ViewPager的Scroller对象，使其支持滚动时长操作
     * 
     */
    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            
            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator)interpolator.get(null));
            setScrollDuration(500);
            scroller.set(this, mScroller);
        }
        catch (Exception e) {
        }
    }
    
    /**
     * 设置每次滚动切换时长
     */
    public void setScrollDuration(long scrollDuration) {
        mScroller.setScrollDuration(scrollDuration);
    }
    
    /**
     * 启动定时器
     */
    public void startAutoFlowTimer() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (getAdapter() != null && getAdapter().getCount() != 0) {
                    int i = getCurrentItem() + 1;
                    int count = getAdapter().getCount();
                    setCurrentItem((i % count), true);
                    Message message = handler.obtainMessage(0);
                    sendMessageDelayed(message, timeSpan);
                }
                
            }
        };
        
        Message message = handler.obtainMessage(0);
        handler.sendMessageDelayed(message, timeSpan);
    }
    
    /**
     * 关闭定时器
     */
    public void stopAutoFlowTimer() {
        if (handler != null)
            handler.removeMessages(0);
        handler = null;
    }
    
    public void setTimeSpan(long timeSpan) {
        this.timeSpan = timeSpan;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        boolean flag = super.dispatchTouchEvent(ev);
        return flag;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        boolean flag = super.onInterceptTouchEvent(arg0);
        return flag;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        boolean flag = super.onTouchEvent(arg0);
        if (consumeTouchEvent) {
            return true;
        }
        else {
            return flag;
        }
        //    	if(flag){
        //    		return flag;
        //    	}
    }
    
    private boolean consumeTouchEvent = false;
    
    public void setConsumeTouchEvent(boolean consumeTouchEvent) {
        this.consumeTouchEvent = consumeTouchEvent;
    }
    
    @Override
    public boolean isCanSlidingItem(MotionEvent ev) {
        Rect rect = new Rect();
        getHitRect(rect);
        if (onComputeRect != null) {
            int[] dxy = onComputeRect.extDxy();
            rect.top = dxy[1] + rect.top;
            rect.bottom = dxy[1] + rect.bottom;
            rect.left = dxy[0] + rect.left;
            rect.right = dxy[0] + rect.right;
        }
        if (rect.contains((int)ev.getX(), (int)ev.getY())) {
            //in rect
            if (0 < getCurrentItem() && getCurrentItem() < getChildCount() - 1) {
                //ok
                return false;
            }
            else {
                //first or last
                return true;
            }
        }
        else {
            return true;
        }
    }
    
    public OnComputeRect onComputeRect;
    
    public void setOnComputRect(OnComputeRect onComputeRect) {
        this.onComputeRect = onComputeRect;
    }
    
    public interface OnComputeRect {
        public int[] extDxy();
    }
    
    @Override
    public boolean isCanSlidingNow(MotionEvent ev, float distanceX) {
        Rect rect = new Rect();
        getHitRect(rect);
        if (onComputeRect != null) {
            int[] dxy = onComputeRect.extDxy();
            rect.top = dxy[1] + rect.top;
            rect.bottom = dxy[1] + rect.bottom;
            rect.left = dxy[0] + rect.left;
            rect.right = dxy[0] + rect.right;
        }
        if (rect.contains((int)ev.getX(), (int)ev.getY())) {
            //in rect
            if (0 < getCurrentItem() && getCurrentItem() < getChildCount() - 1) {
                //ok
                return false;
            }
            else {
                //first or last
                if (distanceX > 5 && getCurrentItem() == 0) {
                    return true;
                }
                else if (distanceX < -5 && getCurrentItem() == getChildCount() - 1) {
                    return true;
                }
                return false;
            }
        }
        else {
            return true;
        }
    }
    
}
