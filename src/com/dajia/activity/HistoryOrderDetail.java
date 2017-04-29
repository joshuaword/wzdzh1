package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.k76.wzd.R;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.Ad;
import com.dajia.Bean.HistoryOrderInfo;
import com.dajia.Bean.MessageBean;
import com.dajia.Bean.SysTemInfo;
import com.dajia.view.RoundImageView;
import com.google.gson.Gson;
import com.tony.viewpager.AsyncBitmapLoader;
import com.tony.viewpager.AsyncBitmapLoader.ImageCallBack;
import com.tony.viewpager.getPicList;
/**
 * 订单详情
 * @author Administrator
 *
 */
public class HistoryOrderDetail extends BaseActivity implements
		android.view.View.OnClickListener {

	private TextView title;

	private TextView rightBtn;
	private String djType = "0";
	private HistoryOrderInfo info;

	/*
	 * private String xiangqingtitle; //"订单详情", private String xiangqingline1;
	 * //"订单号：20150110001 2015-01-10 16:16", private String xiangqingline2;
	 * //"总金额：", private String xiangqingjien; //"￥39", private String
	 * xiangqingline3; //"余额扣除：￥0 优惠券：1 现金支付：￥39", private String
	 * xiangqingline4; //"出发地：南京市三山街地铁口2号口", private String xiangqingline5;
	 * //"目的地：南京市新街口2号地铁口", private String xiangqingline6; //"服务您的司机", private
	 * String xiangqingline7; //"朱清请", private String xiangqingline8;
	 * //"代驾31次 驾龄11年 籍贯江苏", private String xiangqingline9; //"喜欢这个师傅就给他打5分吧",
	 * private String xiangqingtouxiang; //"http://wzd.k76.net/pic/t/76/76.jpg"
	 */
	private LinearLayout backLayout;

	Button commentBtn, guiji;
	RatingBar commentRating,has_driver_rating;
	EditText commentEdit;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	ProgressDialog p;
	private String[] titles; // 图片标题
	private List<String> url = null, file = null;
	private int currentItem = 0; // 当前图片的索引号

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;
	// 切换当前显示的图片

	private Handler handler = new Handler() {
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
	private TextView btn_daijiaqian, btn_daijiahou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_orderdetail);
		title = (TextView) findViewById(R.id.title);
		info = (HistoryOrderInfo) getIntent().getSerializableExtra("info");
		System.out.println("***Dingdanid****"+info.getDingdanid()); 
		title.setText(info.getXiangqingtitle());
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		btn_daijiaqian = (TextView) findViewById(R.id.btn_daijiaqian);
		btn_daijiahou = (TextView) findViewById(R.id.btn_daijiahou);
		getPhoto();
		//代驾前
		btn_daijiaqian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				djType = "0";
				btn_daijiaqian.setTextColor(Color.parseColor("#ffffffff"));
				btn_daijiahou.setTextColor(Color.parseColor("#00FF00"));
				getPhoto();
			}
		});
		//代驾后
		btn_daijiahou.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				djType = "1";
				btn_daijiaqian.setTextColor(Color.parseColor("#00FF00"));
				btn_daijiahou.setTextColor(Color.parseColor("#ffffffff"));
				getPhoto();
			}
		});

		backLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}

		});
		// rightBtn = (TextView) findViewById(R.id.btnRight);
		// rightBtn.setBackgroundResource(R.drawable.trash_title);
		// rightBtn.setOnClickListener(this);

		TextView orderId = (TextView) findViewById(R.id.text_orderdetail_id);
		// TextView orderTime = (TextView) findViewById(R.id.text_driver_time);
		TextView zongjine = (TextView) findViewById(R.id.zongjine);
		TextView money = (TextView) findViewById(R.id.text_income);
		TextView cash = (TextView) findViewById(R.id.text_balance);
		TextView startPos = (TextView) findViewById(R.id.text_startPosition);
		TextView endPos = (TextView) findViewById(R.id.text_endPosition);
		TextView xiangqing6 = (TextView) findViewById(R.id.xiangqing6);

		// TextView driveId = (TextView)
		// findViewById(R.id.orderdetail_driver_id);
		TextView driveName = (TextView) findViewById(R.id.text_driverName);
		TextView driverCount = (TextView) findViewById(R.id.text_driverCount);
		TextView giveRate = (TextView) findViewById(R.id.text_info_giveRate);
		RoundImageView driveImg = (RoundImageView) findViewById(R.id.img_driver);

		commentEdit = (EditText) findViewById(R.id.commentEdit);
		RatingBar driverRating = (RatingBar) findViewById(R.id.driver_rating);
		commentRating = (RatingBar) findViewById(R.id.ratingBar);
		has_driver_rating = (RatingBar) findViewById(R.id.has_driver_rating);
		driverRating.setRating(Float.parseFloat(info.getXinji()));

		Bitmap bitMap = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_driver);
		FinalBitmap finalBitMap = FinalBitmap.create(HistoryOrderDetail.this);
		if (!TextUtils.isEmpty(info.getXiangqingtouxiang())) {
			finalBitMap.display(driveImg, info.getXiangqingtouxiang(), bitMap);
		}

		if (info != null) {
			if (!TextUtils.isEmpty(info.getXiangqingline1())) {
				orderId.setText(info.getXiangqingline1());
			}
			/*
			 * if (!TextUtils.isEmpty(info.getYuyuechufashijia())) {
			 * orderTime.setText(info.getYuyuechufashijia()); }
			 */
			if (!TextUtils.isEmpty(info.getXiangqingjien())) {
				money.setText(info.getXiangqingjien());
			}
			zongjine.setText(info.getXiangqingline2());
			if (!TextUtils.isEmpty(info.getXiangqingline3())) {
				cash.setText(info.getXiangqingline3());
			}
			if (!TextUtils.isEmpty(info.getXiangqingline4())) {
				startPos.setText(info.getXiangqingline4());
			}
			if (!TextUtils.isEmpty(info.getXiangqingline5())) {
				endPos.setText(info.getXiangqingline5());
			}
			xiangqing6.setText(info.getXiangqingline6());
			driveName.setText(info.getXiangqingline7());
			/*
			 * if (!TextUtils.isEmpty(info.getGonghao())) {
			 * driveId.setText(info.getGonghao()); }
			 */
			driverCount.setText(info.getXiangqingline8());
			giveRate.setText(info.getXiangqingline9());
		}

		guiji = (Button) findViewById(R.id.guiji);
		commentBtn = (Button) findViewById(R.id.comment);
		commentBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				submit("no");
			}
		});
		guiji.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HistoryOrderDetail.this,
						AllAdressMapActivity.class);
				intent.putExtra("orderId", info.getDingdanid());
				startActivity(intent);

			}
		});
		if ("yes".equals(info.getIfpingjia())) {
			commentEdit.setVisibility(View.GONE);
			commentBtn.setVisibility(View.GONE);
			commentRating.setVisibility(View.GONE);
			has_driver_rating.setVisibility(View.VISIBLE);
			if(!TextUtils.isEmpty(info.getPingjiaxingji())){
				has_driver_rating.setRating(Float.parseFloat(info.getPingjiaxingji()));
			}
			giveRate.setText(info.getPingjiatext());
		}else{
			commentEdit.setVisibility(View.VISIBLE);
			commentBtn.setVisibility(View.VISIBLE);
			commentRating.setVisibility(View.VISIBLE);
			has_driver_rating.setVisibility(View.GONE);
			giveRate.setText(info.getXiangqingline9());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			delDialog();
			break;

		default:
			break;
		}

	}

	private void delDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定删除当前订单?");
		builder.setPositiveButton(getResources().getString(R.string.ensure),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						submit("yes");
					}

				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

		builder.create().show();

	}

	public void submit(String flag) {
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("dingdanid", info.getDingdanid());
		ap.put("dafen", String.valueOf(commentRating.getRating()));
		ap.put("leirong", commentEdit.getText().toString());
		ap.put("ifdel", flag);
		ap.put("act", "postok");

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		fp.post(baseurl + "/api/pinlunclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						MessageBean msg = gson.fromJson(t.toString(),
								MessageBean.class);
						if (msg.getRet().equals("success")) {
							if ("评论成功".equals(msg.getMsg())) {
								Toast.makeText(HistoryOrderDetail.this, "评价成功",
										Toast.LENGTH_SHORT).show();
							}
							if ("删除订单成功".equals(msg.getMsg())) {
								Toast.makeText(HistoryOrderDetail.this,
										"删除订单成功", Toast.LENGTH_SHORT).show();
							}
						}
						finish();
					}
				});
	}

	private Bitmap getBitmap(String path) {
		String myJpgPath = path;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
		return bm;

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

	private void getPhoto() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");
		ap.put("dingdanid", info.getDingdanid());
//		ap.put("dingdanid", "321");
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
						file = gp.getFilePathList(url);
						imageViews = new ArrayList<ImageView>();
						// 初始化图片资源
						for (int i = 0; i < url.size(); i++) {
							ImageView image = new ImageView(
									HistoryOrderDetail.this);
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
						viewPager = (ViewPager) findViewById(R.id.vp);
						viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
						// 设置一个监听器，当ViewPager中的页面改变时调用
						viewPager
								.setOnPageChangeListener(new MyPageChangeListener());
					}
				});
	}

}
