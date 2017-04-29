package com.dajia.activity;

import java.util.ArrayList;


import com.squareup.picasso.Picasso;

import net.k76.wzd.R;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

//司机详情图片放大显示
public class ScaleimgActivity extends Activity {

	/**
	 * 图片id数组
	 */
	private ArrayList<String> imglist;
	private ArrayList<View> listViews = new ArrayList<View>();
	/**
	 * 当前选中的图片id序号
	 */
	private int currentPosition;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private String path;
	private TextView pager_count;
	private ProgressBar load_progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scaleimg_layout);

		imglist = getIntent().getStringArrayListExtra("imglist");
		currentPosition = getIntent().getIntExtra("selectposition", 0);
		
		initdata();
		initViewpager();
	}

	@SuppressWarnings("deprecation")
	public void initdata() {
		// TODO Auto-generated method stub
		for (int i = 0; i < imglist.size(); i++) {
			path = imglist.get(i);
			// 过滤“s_”
			if (path.contains("s_")) {
				int a = path.indexOf("s_");
				String aa = path.substring(a + 2);
				String bb = path.substring(0, a);
				path = bb + aa;
			}
			Uri url = Uri.parse(path);
			ImageView img = new ImageView(ScaleimgActivity.this);
			img.setBackgroundColor(0xff000000);
			img.setPadding(10, 10,10, 10);
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			Picasso.with(ScaleimgActivity.this).load(url).fit().centerCrop().into(img);
			listViews.add(img);
		}
	}

	public void initViewpager() {
		pager = (ViewPager) findViewById(R.id.scale_viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		pager_count = (TextView) findViewById(R.id.scale_pagercount);
		load_progressBar = (ProgressBar) findViewById(R.id.scale_loading);
		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器
		pager.setCurrentItem(currentPosition);

	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			// count = arg0;
			pager_count.setText(arg0+1+"/"+imglist.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};


	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
			load_progressBar.setVisibility(View.GONE);
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
				load_progressBar.setVisibility(View.VISIBLE);
			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}
