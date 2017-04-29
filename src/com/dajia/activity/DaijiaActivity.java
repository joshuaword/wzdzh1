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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dajia.Bean.CurrentOrderBean;
import com.dajia.Bean.DaijiaInfo;
import com.dajia.Bean.SysTemInfo;
import com.dajia.adapter.DaijiaLcAdapter;
import com.dajia.util.Assign_UpDateDialog;
import com.dajia.util.ConfirmDialogListener;
import com.dajia.view.CircularImage;
import com.dajia.view.NoScrollListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tony.viewpager.AsyncBitmapLoader;
import com.tony.viewpager.AsyncBitmapLoader.ImageCallBack;
import com.tony.viewpager.getPicList;
/**
 * 代驾中
 * @author Administrator
 *
 */
public class DaijiaActivity extends BaseActivity {
	private TextView tv_apply_invoice_title, title, txt_cancle;
	EditText edt;
	private NoScrollListView daijiaLc;
	private DaijiaLcAdapter daijialcadapter;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private List<DaijiaInfo> daijiaInfo = new ArrayList<DaijiaInfo>();
	ProgressDialog p;
	ImageView circle_imge1, circle_imge2, circle_imge3;
	private String djType = "0";
	private String[] titles; // 图片标题
	private List<String> url = null, file = null;
	private int currentItem = 0; // 当前图片的索引号

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;
	// 切换当前显示的图片

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
	private TextView btn_daijiaqian, btn_daijiahou;
	private String dingdanid;
	private String number;
	private View back_layout, one_layout, two_layout, three_layout;
	String userid;
	private ImageView txt_call;
	private CircularImage personImage;
	private TextView name, txt_distance, txt_one, txt_one_content, txt_two,
			txt_two_content, txt_three, txt_three_content;
	SharedPreferences settings;
	private String baseurl,servicename;
	private int i = 0;
	private View showphoto_layout;
	private static DisplayImageOptions options;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private int TIME = 10000;
	Button guiji;
	RatingBar has_driver_rating;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				queryNewOrder();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daijia);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		daijiaLc = (NoScrollListView) findViewById(R.id.daijiaLc);
		RatingBar driverRating = (RatingBar) findViewById(R.id.driver_rating);
		circle_imge1 = (ImageView) findViewById(R.id.circle_imge1);
		circle_imge2 = (ImageView) findViewById(R.id.circle_imge2);
		circle_imge3 = (ImageView) findViewById(R.id.circle_imge3);
		showphoto_layout = findViewById(R.id.showphoto_layout);
		dingdanid = getIntent().getStringExtra("Dingdanid");
		back_layout = findViewById(R.id.back_layout);
		txt_cancle = (TextView) findViewById(R.id.txt_cancle);
		txt_distance = (TextView) findViewById(R.id.txt_distance);
		name = (TextView) findViewById(R.id.name);
		//取消订单
		txt_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cancelDialog();
			}
		});
		settings = getSharedPreferences("setting", 0);
		servicename=settings.getString("selectfuwulx", "");
		btn_daijiaqian = (TextView) findViewById(R.id.btn_daijiaqian);
		if (servicename.contains("上门洗车")) {
			btn_daijiaqian.setText("洗车前 ");
		} else if (servicename.contains("到店洗车")) {
			btn_daijiaqian.setText("洗车前 ");
		} else if (servicename.contains("代驾")) {
			btn_daijiaqian.setText("代驾前 ");
		} else {
			btn_daijiaqian.setText(servicename + "前");
		}
		btn_daijiahou = (TextView) findViewById(R.id.btn_daijiahou);
		if (servicename.contains("上门洗车")) {
			btn_daijiahou.setText("洗车后 ");
		} else if (servicename.contains("到店洗车")) {
			btn_daijiahou.setText("洗车后 ");
		} else if (servicename.contains("代驾")) {
			btn_daijiahou.setText("代驾后 ");
		} else {
			btn_daijiahou.setText( servicename + "后");
		}
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

		btn_daijiahou.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				djType = "1";
				btn_daijiaqian.setTextColor(Color.parseColor("#00FF00"));
				btn_daijiahou.setTextColor(Color.parseColor("#ffffffff"));
				getPhoto();
			}
		});
		personImage = (CircularImage) findViewById(R.id.person_icon);
		one_layout = findViewById(R.id.one_layout);
		two_layout = findViewById(R.id.two_layout);
		three_layout = findViewById(R.id.three_layout);
		txt_one = (TextView) findViewById(R.id.txt_one);
		txt_one_content = (TextView) findViewById(R.id.txt_one_content);
		txt_three = (TextView) findViewById(R.id.txt_three);
		txt_three_content = (TextView) findViewById(R.id.txt_three_content);
		txt_two = (TextView) findViewById(R.id.txt_two);
		txt_two_content = (TextView) findViewById(R.id.txt_two_content);
		txt_call = (ImageView) findViewById(R.id.txt_call);
		txt_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showUpdate_Dialog();
			}
		});
		title = (TextView) findViewById(R.id.title);
		tv_apply_invoice_title = (TextView) findViewById(R.id.tv_daijia_title);
		title.setText("订单详情");
		
		userid = settings.getString("userid", "");
		baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		back_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		queryNewOrder();
		handler.postDelayed(runnable, TIME); // 每隔10s执行

	}

	public void showUpdate_Dialog() {
		Assign_UpDateDialog.showUpdateDialog(this, "提示", "是否拨打" + number
				+ "电话？", "拨打", "取消", new ConfirmDialogListener() {
			@Override
			public void onPositive(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}

			@Override
			public void onNegative(DialogInterface dialog) {
				dialog.cancel();
			}
		});

	}

	public static void setCacheImageLoad(Context context, int drawId,
			int Rounded, ImageView imageView, String imageUrl) {
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder().showStubImage(drawId)
				.showImageForEmptyUri(drawId).showImageOnFail(drawId)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(Rounded)).build();
		imageLoader.displayImage(imageUrl, imageView, options);
	}

	private void cancelDialog() {
		String title = settings.getString("quxiao", "取消原因");
		final String yuanyin1 = settings.getString("quxiaoyuanyin1", "代驾员态度不好");
		final String yuanyin2 = settings.getString("quxiaoyuanyin2", "收费有点贵");
		final String yuanyin3 = settings.getString("quxiaoyuanyin3", "其他原因");

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(title);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_view, null);
		edt = (EditText) layout.findViewById(R.id.edt_input);
		TextView tv1 = (TextView) layout.findViewById(R.id.yuanyin1);
		tv1.setText(yuanyin1);
		//取消原因1
		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin1);
			}
		});
		TextView tv2 = (TextView) layout.findViewById(R.id.yuanyin2);
		tv2.setText(yuanyin2);
		//取消原因2
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin2);
			}
		});
		TextView tv3 = (TextView) layout.findViewById(R.id.yuanyin3);
		tv3.setText(yuanyin3);
		//取消原因3
		tv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin3);
			}
		});
		builder.setView(layout);
		/**
		 * 向后台提交取消订单和取消原因
		 */
		builder.setPositiveButton(getResources().getString(R.string.ensure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						FinalHttp fp = new FinalHttp();
						AjaxParams ap = new AjaxParams();
						ap.put("userid", userid);
						ap.put("act", "postok");
						ap.put("dingdanid", dingdanid);
						ap.put("quxiaoyuanyin", edt.getEditableText()
								.toString().trim());
						fp.post(baseurl + "/api/quxiaoclient.php", ap,
								new AjaxCallBack<Object>() {
									@Override
									public void onSuccess(Object t) {
										super.onSuccess(t);
										sendBroadcast(new Intent("cancle"));
										finish();
									}
								});

					}

				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

		builder.create().show();

	}

	int yourChose = -1;

	@Override
	protected void onResume() {
		super.onResume();
	}
/*
 * 
 * 获取最新订单
 */
	public void queryNewOrder() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		Log.e("MUSIC", "userid=" + userid);
		ap.put("userid", userid);
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

							if (bean.getIfhavedingdan().equals("no")) {
								finish();
							}
							if (bean.getIfquxiao().equals("yes")) {
								txt_cancle.setVisibility(View.VISIBLE);
							} else {
								txt_cancle.setVisibility(View.GONE);
							}
							if (bean.getIfshowphoto().equals("yes")) {
								showphoto_layout.setVisibility(View.VISIBLE);
							} else {
								showphoto_layout.setVisibility(View.GONE);
							}
							if (!TextUtils.isEmpty(bean.getTelphone())) {
								txt_call.setVisibility(View.VISIBLE);
								number = bean.getTelphone();
							} else {
								txt_call.setVisibility(View.GONE);
							}

							if (!TextUtils.isEmpty(bean.getThefile())) {
								setCacheImageLoad(DaijiaActivity.this, -1, 0,
										personImage, bean.getThefile());
							}

							if (!TextUtils.isEmpty(bean.getShowname())) {
								name.setText(bean.getShowname());
							}
							if (!TextUtils.isEmpty(bean.getJuli())) {
								txt_distance.setText(bean.getJuli());
							}
							if(!TextUtils.isEmpty(bean.getStr4())){
								tv_apply_invoice_title.setText(bean.getStr4());
							}
						

							if (bean.getDaijiainfo().size() > 0) {
								daijiaInfo.clear();
								daijiaInfo.addAll(bean.getDaijiainfo());
								daijialcadapter = new DaijiaLcAdapter(
										DaijiaActivity.this, daijiaInfo);
										
								daijiaLc.setAdapter(daijialcadapter);
							}
							if ("完成".equals(bean.getState())) {
								Intent intent = new Intent(DaijiaActivity.this,
										ConfirmOrderActivity.class);
								intent.putExtra("dingDanId", dingdanid);
								startActivity(intent);
								finish();
							}

						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);

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
/**
 * 获取照片（司机端在洗车前后的拍照）
 */
	private void getPhoto() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");
		ap.put("dingdanid", dingdanid);
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
						file = gp.getFilePathList(url);
						imageViews = new ArrayList<ImageView>();
						// 初始化图片资源
						for (int i = 0; i < url.size(); i++) {
							ImageView image = new ImageView(DaijiaActivity.this);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (handler != null) {
			handler.removeCallbacks(runnable);
			handler = null;
		}
		super.onDestroy();
	}

}
