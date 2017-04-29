package com.dajia.activity;

import net.tsz.afinal.FinalBitmap;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import net.k76.wzd.R;
import com.dajia.Bean.PicBean;

public class PicShowActivity extends BaseActivity implements OnPageChangeListener{
	
	private PicBean picBean;
	  /** 
     * ViewPager 
     */  
    private ViewPager viewPager;  
    
    /** 
     * 装点点的ImageView数组 
     */  
    private ImageView[] tips;  
      
    /** 
     * 装ImageView数组 
     */  
    private ImageView[] mImageViews;  
      
    private FinalBitmap finalBitMap;
    
    ViewGroup group;
    
    int picNum;
    
    String path;
    
    Boolean misScrolled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_show_layout);
		
		//if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	     
		finalBitMap=FinalBitmap.create(PicShowActivity.this);
		group = (ViewGroup)findViewById(R.id.viewGroup);  
	     viewPager = (ViewPager) findViewById(R.id.viewPager);  
	     viewPager.setOffscreenPageLimit(1);
	     
	     path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
	     
	     picNum = getIntent().getIntExtra("picNum", 0);
	     
				 //将点点加入到ViewGroup中  
		        tips = new ImageView[picNum];  
		        for(int i=0; i<picNum; i++){  
		            ImageView imageView = new ImageView(PicShowActivity.this);  
		            imageView.setLayoutParams(new LayoutParams(10,10));  
		            tips[i] = imageView;  
		            if(i == 0){  
		                tips[i].setBackgroundResource(R.drawable.dot_current);  
		            }else{  
		                tips[i].setBackgroundResource(R.drawable.dot_normal);  
		            }  
		              
		            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,    
		                    LayoutParams.WRAP_CONTENT));  
		            layoutParams.leftMargin = 10;
			    	layoutParams.rightMargin = 10;
		            group.addView(imageView, layoutParams);  
		        }  
		          
		          
		        //将图片装载到数组中  
		        mImageViews = new ImageView[picNum];  
		        for(int i=0; i<picNum; i++){  
		            ImageView imageView = new ImageView(PicShowActivity.this);  
		            mImageViews[i] = imageView;  
		            imageView.setScaleType(ScaleType.FIT_XY);
		            
		            finalBitMap.display(imageView, path +"/"+ (i+1) +".jpg");
		            
		            //imageView.setBackgroundResource(imgIdArray[i]);  
		        }  
		     
		     //设置Adapter  
		        viewPager.setAdapter(new MyAdapter());  
		        //设置监听，主要是设置点点的背景  
		        viewPager.setOnPageChangeListener(PicShowActivity.this);  
		        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动  
		        viewPager.setCurrentItem(0);  
		
		
	}
	
    public class MyAdapter extends PagerAdapter{  
  
        @Override  
        public int getCount() {  
            return mImageViews.length;  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        /** 
         * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁 
         */  
        @Override  
        public void destroyItem(ViewGroup view, int position, Object object) {  
      
            view.removeView(mImageViews[position]);  
      
        }  
      
        /** 
         * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化， 
         * 我们将要显示的 ImageView 加入到 ViewGroup 中，然后作为返回值返回即可 
         */  
        @Override  
        public Object instantiateItem(ViewGroup view, int position) {  
      
            view.addView(mImageViews[position], 0);  
      
            return mImageViews[position];  
        }  
          
          
    }

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			misScrolled = false;
			break;
		case ViewPager.SCROLL_STATE_SETTLING:
			misScrolled = true;
			break;
		case ViewPager.SCROLL_STATE_IDLE:
			if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
				Intent intent = new Intent(PicShowActivity.this,HomepageActivity.class);
				startActivity(intent);
				finish();
			}
			misScrolled = true;
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % mImageViews.length);  
		if (arg0 == picNum) {
			
		}
	}  
	
	 /** 
     * 设置选中的tip的背景 
     * @param selectItems 
     */  
    private void setImageBackground(int selectItems){  
        for(int i=0; i<tips.length; i++){  
            if(i == selectItems){  
                tips[i].setBackgroundResource(R.drawable.dot_current);  
            }else{  
                tips[i].setBackgroundResource(R.drawable.dot_normal);  
            }  
        }  
    }  
}
