package com.twzs.core.view.viewpager;

import android.view.MotionEvent;

/**
 *@Author Rick.Ping
 *@Date 2014-1-6
 *@Comment
 **/
public interface SlidingMenuItem {

	public boolean isCanSlidingItem(MotionEvent ev);
	public boolean isCanSlidingNow(MotionEvent ev,float distanceX);
}
