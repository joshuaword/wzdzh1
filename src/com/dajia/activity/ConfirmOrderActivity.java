package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.CurrentOrderBean;
import com.dajia.Bean.Orderbean;
import com.dajia.Bean.SysTemInfo;
import com.google.gson.Gson;
import com.tony.viewpager.AsyncBitmapLoader;
import com.tony.viewpager.AsyncBitmapLoader.ImageCallBack;
import com.tony.viewpager.getPicList;

public class ConfirmOrderActivity extends Activity {
	private TextView title;
	private LinearLayout backLayout;
	private Button confirm_order;
	private TextView ordernum_tv;
	private TextView servicect_tv;
	private TextView payallmoney_tv;
	private TextView payway_tv;
	private String dingDanId;
	private String userid;
	private String baseurl;

	
	//以下为代驾图片相关
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private TextView tv_daijiaqian, tv_daijiahou;
	private LinearLayout  layout_daijiaqian,layout_daijiahou,shouphoto;
	ProgressDialog p;
	private  String  ifshowphoto="n";
	ImageView circle_imge1, circle_imge2, circle_imge3;
	private String djType = "0";
	private List<String> url = null;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;//线程池
	
	private Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				viewPager.setCurrentItem(currentItem);
				break;
			case 1:

				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmorder_layout);
		title = (TextView) findViewById(R.id.title);
		title.setText("确认服务完成");
		dingDanId=super.getIntent().getStringExtra("dingDanId");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		ordernum_tv = (TextView) findViewById(R.id.odrernum_tv);
		servicect_tv = (TextView) findViewById(R.id.servicetype_tv);
		payallmoney_tv = (TextView) findViewById(R.id.paymoney_tv);
		payway_tv = (TextView) findViewById(R.id.payway_tv);
		
		
		SharedPreferences settings = getSharedPreferences("setting", 0);
		userid = settings.getString("userid", "");
		baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		queryNewOrder();

		
		tv_daijiaqian = (TextView) findViewById(R.id.tv_daijiaqian);//代驾前
		tv_daijiahou = (TextView) findViewById(R.id.tv_daijiahou);//代驾后
		layout_daijiaqian=(LinearLayout) findViewById(R.id.layout_daijiaqian);
		layout_daijiahou=(LinearLayout) findViewById(R.id.layout_daijiahou);
		shouphoto=(LinearLayout) findViewById(R.id.shouphoto);
		
			layout_daijiaqian.setBackgroundColor(getResources().getColor(R.color.blue2));
			//点击代驾前
			getPhoto();
			tv_daijiaqian.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					djType = "0";
					layout_daijiahou.setBackgroundColor(getResources().getColor(R.color.white));
					layout_daijiaqian.setBackgroundColor(getResources().getColor(R.color.blue2));
					getPhoto();
				}
			});
			//点击代驾后
			tv_daijiahou.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					djType = "1";
					layout_daijiaqian.setBackgroundColor(getResources().getColor(R.color.white));
					layout_daijiahou.setBackgroundColor(getResources().getColor(R.color.blue2));
					getPhoto();
				}
			});
		


		confirm_order = (Button) findViewById(R.id.comment);
		confirm_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				confirmOrder();

			}
		});
	}

	// 查询订单信息
	public void queryNewOrder() {

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		Log.e("MUSIC", "userid=" + userid);
		ap.put("userid", userid);
		ap.put("dingdanid", dingDanId);
		ap.put("act", "postok");
		fp.post(baseurl + "/api/mydaijiaclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);

						Log.i("/api/mydaijiaclient.php", " 8.获取当前订单信息 ");

						Gson gson = new Gson();
						final CurrentOrderBean bean = gson.fromJson(
								t.toString(), CurrentOrderBean.class);
						if (bean != null) {
							ifshowphoto=bean.getIfshowphoto();//是否展示图片
							//如果为no（接口传的）隐藏，否则可以看到图片
							if("no".equals(ifshowphoto)){
								shouphoto.setVisibility(View.GONE);
							}else{
							shouphoto.setVisibility(View.VISIBLE);
							}
							Log.e(ifshowphoto, ifshowphoto);
							// 订单id
							if (!TextUtils.isEmpty(bean.getDingdanhao())) {
								ordernum_tv.setText(bean.getDingdanhao());
							}
							// 支付金额
							if (!TextUtils.isEmpty(bean.getFuwuleirong())) {
								servicect_tv.setText(bean.getFuwuleirong());
							}
							// 金额
							if (!TextUtils.isEmpty(bean.getZhifujine())) {
								payallmoney_tv.setText(bean.getZhifujine());
							}
							// 支付方式
							if (!TextUtils.isEmpty(bean.getZhifufangshi())) {
								payway_tv.setText(bean.getZhifufangshi());
							}
						}
					}
				});
	}
	
	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}
	


	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				// handler.sendEmptyMessage(0);// 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return url.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	//获取图片
	private void getPhoto() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");
		ap.put("dingdanid", dingDanId);
		// ap.put("dingdanid", "321");
		ap.put("act", "postok");
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		fp.post(baseurl + "/api/piclistclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						SysTemInfo msg = gson.fromJson(t.toString(),
								SysTemInfo.class);
						url = new ArrayList<String>();
						if (djType.equals("0")) {
							List<String> all_djqian = new ArrayList<String>();
							if (msg.getPaizhao() != null
									&& msg.getPaizhao().size() > 0) {
								for (int i = 0; i < msg.getPaizhao().size(); i++) {
									all_djqian.add(msg.getPaizhao().get(i)
											.getUrl());
								}
							}
							if (all_djqian.size() > 0) {
								url.clear();
								url.addAll(all_djqian);
							}
						} else {
							List<String> all_djhou = new ArrayList<String>();
							if (msg.getPaizhaohou() != null
									&& msg.getPaizhaohou().size() > 0) {
								for (int i = 0; i < msg.getPaizhaohou().size(); i++) {
									all_djhou.add(msg.getPaizhaohou().get(i)
											.getUrl());
								}
							}
							if (all_djhou.size() > 0) {
								url.clear();
								url.addAll(all_djhou);
							}
						}
						getPicList gp = new getPicList();
						//file = gp.getFilePathList(url);
						imageViews = new ArrayList<ImageView>();
						// 初始化图片资源
						for (int i = 0; i < url.size(); i++) {
							ImageView image = new ImageView(ConfirmOrderActivity.this);
							// imageView.setImageBitmap(getBitmap(file.get(i).toString()));
							AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
							Bitmap bitmap = asyncBitmapLoader.loadBitmap(image,
									url.get(i).toString(), new ImageCallBack() {

										public void imageLoad(
												ImageView imageView,
												Bitmap bitmap) {
											// TODO Auto-generated method stub
											imageView.setImageBitmap(bitmap);
										}
									});
							if (bitmap == null) {
								image.setImageResource(R.color.gray);
							} else {
								image.setImageBitmap(bitmap);
							}

							image.setScaleType(ScaleType.FIT_XY);
							imageViews.add(image);
						}
						viewPager = (ViewPager) findViewById(R.id.viewpager_picture);
						
						viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
						// 设置一个监听器，当ViewPager中的页面改变时调用
						viewPager
								.setOnPageChangeListener(new MyPageChangeListener());
					}
				});
	}
	
	// 确认订单完成
	public void confirmOrder() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		Log.e("MUSIC", "userid=" + userid);
		ap.put("userid", userid);
		ap.put("dingdanid", dingDanId);
		ap.put("act", "postok");
		fp.post(baseurl + "/api/queren.php", ap, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Gson gson = new Gson();
				Orderbean bean = gson.fromJson(t.toString(), Orderbean.class);
				if ("success".equals(bean.getRet())) {
					Toast.makeText(ConfirmOrderActivity.this, bean.getMsg(),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ConfirmOrderActivity.this,
							PinglunActivity.class);
					intent.putExtra("dingDanId", dingDanId);
					startActivity(intent);
				}
			}
		});
	}
}
