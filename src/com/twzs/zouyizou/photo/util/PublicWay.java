package com.twzs.zouyizou.photo.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 存放所有的list在最后退出时一起关闭
 *
 * @author fucheng
 * 2015年2月4日
 */
public class PublicWay {
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	public static int num = 6;
	
}
