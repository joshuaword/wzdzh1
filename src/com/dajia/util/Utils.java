package com.dajia.util;

import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.view.View;

public class Utils
{
  public static boolean sDebug = true;

  public static boolean checkIsPhone(String paramString)
  {
    return Pattern.matches("^\\d{11}$", paramString);
  }
  
  public static Bitmap getBitmapFromView(View paramView)
  {
    paramView.destroyDrawingCache();
    paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    paramView.layout(0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
    paramView.setDrawingCacheEnabled(true);
    return paramView.getDrawingCache(true);
  }
 
}