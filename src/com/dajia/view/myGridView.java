package com.dajia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class myGridView extends GridView {
	private boolean haveScrollbar = true; 
	 public myGridView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	    } 

	    public myGridView(Context context) { 
	        super(context); 
	    } 

	    public myGridView(Context context, AttributeSet attrs, int defStyle) { 
	        super(context, attrs, defStyle); 
	    } 
	    /**  
	     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true  
	     *   
	     * @param haveScrollbars  
	     */   
	    public void setHaveScrollbar(boolean haveScrollbar) {   
	        this.haveScrollbar = haveScrollbar;   
	    } 
	    @Override 
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	    	 if (haveScrollbar == false) {   
	             int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
	             super.onMeasure(widthMeasureSpec, expandSpec);   
	         } else {   
	             super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
	         }   
	    } 
}
