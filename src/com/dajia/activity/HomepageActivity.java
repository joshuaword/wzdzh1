package com.dajia.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dajia.Bean.FuwuxiangmuBean;
import com.dajia.Bean.HomescrollBean;
import com.dajia.Bean.MessageBean;
import com.dajia.Bean.MessageLisyBean;
import com.dajia.Bean.PicBean;
import com.dajia.Bean.SetBean;
import com.dajia.Bean.UpDateInfo;
import com.dajia.adapter.HomeAdvAdapter;
import com.dajia.adapter.HomeListviewAdapter;
import com.dajia.fragment.MenuFragment;
import com.dajia.util.Assign_UpDateDialog;
import com.dajia.util.ConfirmDialogListener;
import com.dajia.view.myListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.picasso.Picasso;
import com.twzs.core.download.Downloadhelper;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageActivity extends SlidingFragmentActivity {
	private Fragment menuFragment;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private List<View> advPics = new ArrayList<View>();
	private String[] picnames = new String[3];
	private ArrayList<HomescrollBean> servicetypelists = new ArrayList<HomescrollBean>();
	//private List<ServiceProjectBean> gridlists=new ArrayList<ServiceProjectBean>();
	private ViewGroup group;
	private String login;
	public static final String TEMPPATH = "temp";
	private TextView message_point;
	public static List<MessageBean> messagelist = new ArrayList<MessageBean>();
	public static String unRead;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private TextView city_txt;
	private SharedPreferences settings;
	private String path;
	private PicBean picBean;
	private long exitTime = 0;
	private String gpscity;
	private String userid;
	private String baseurl;
	private myListView typelist;
	//private ViewPager menv_vipager;
	//private List<GridView> mviewLists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homepagelayout);

		settings = getSharedPreferences("setting", 0);
		login = settings.getString("login", "");
		userid = settings.getString("userid", "");
		baseurl = settings.getString("baseurl", "http://sxah.k76.net");

		if (login.equals("1")) {
			checkUPDATE();
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("login", "0");
			editor.commit();
		}

		// 初始化开机广告
		loadPic();
		// 初始化SlideMenus
		initMenu();
		initViewPager();
		initView();
		initlocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMessage();
	}

	public void initView() {
		Button leftBtn = (Button) findViewById(R.id.homebtnLeft);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSlidingMenu().showMenu();
			}
		});
		message_point = (TextView) findViewById(R.id.message_point);

		LinearLayout choosecity = (LinearLayout) findViewById(R.id.homecity_layout);
		city_txt = (TextView) findViewById(R.id.currentcity_txt);
		choosecity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomepageActivity.this,
						ChoosecityActivity.class);
				intent.putExtra("localcity", gpscity);
				startActivityForResult(intent, 1);
			}
		});

		typelist = (myListView) findViewById(R.id.hometype_listview);
		initMenulistdata();

		//menv_vipager = (ViewPager) findViewById(R.id.menu_viewpager);
		//menv_vipager.setOnPageChangeListener(new GuidePageChangeListener());
		//viewadapter = new HomeMenuviewpagerAdapter(HomepageActivity.this,
			//	mviewLists);
		//menv_vipager.setAdapter(viewadapter);
		// 初始化新菜单页面
		//initComplexlayout();
	}

	public void initlocation() {
		mLocClient = new LocationClient(getApplicationContext());// 声明locationClient类(定位的核心类)
		mLocClient.registerLocationListener(myListener);// 注册监听函数
		LocationClientOption option = new LocationClientOption();// 该类用来设置定位SDK的定位方式
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms
		mLocClient.setLocOption(option);
		mLocClient.start();// 开始定位
	}

	@Override
	public void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

	public void initComplexlayout() {
		// TODO Auto-generated method stub
	/*	for (int i = 0; i < servicetypelists.size(); i++) {
			ServiceProjectBean bean=new ServiceProjectBean();
			gridlists.add(servicetypelists.get(i).getFuwuxiangmu());
		}*/
	}

	public void loadPic() {
		path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
				.getAbsolutePath();

		final FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/shengchengclient.php", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {

						String client1 = settings.getString("client1", "");
						String client2 = settings.getString("client2", "");
						String client3 = settings.getString("client3", "");
						int i = 0;

						Gson gson = new Gson();
						picBean = gson.fromJson(t.toString(), PicBean.class);
						if (!TextUtils.isEmpty(picBean.getClient1())
								&& !picBean.getClient1().equals("")) {
							if (!client1.equals(picBean.getClient1())) {
								fp.download(picBean.getClient1(), path
										+ "/1.jpg", null);
							}
							i++;
						} else {
							File file = new File(path + "/1.jpg");
							if (file.isFile()) {
								file.delete();
							}
						}
						if (!TextUtils.isEmpty(picBean.getClient2())
								&& !picBean.getClient2().equals("")) {
							if (!client2.equals(picBean.getClient2())) {
								fp.download(picBean.getClient2(), path
										+ "/2.jpg", null);
							}
							i++;
						} else {
							File file = new File(path + "/2.jpg");
							if (file.isFile()) {
								file.delete();
							}
						}
						if (!TextUtils.isEmpty(picBean.getClient3())
								&& !picBean.getClient3().equals("")) {
							if (!client3.equals(picBean.getClient3())) {
								fp.download(picBean.getClient3(), path
										+ "/3.jpg", null);
							}
							i++;
						} else {
							File file = new File(path + "/3.jpg");
							if (file.isFile()) {
								file.delete();
							}
						}
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("picNum", i);
						editor.putString("client1", picBean.getClient1());
						editor.putString("client2", picBean.getClient2());
						editor.putString("client3", picBean.getClient3());
						editor.commit();
					}
				});
	}

	// 左侧slidingMenu
	private void initMenu() {
		menuFragment = new MenuFragment();
		setBehindContentView(R.layout.menu_layout);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_layout, menuFragment).commit();
		SlidingMenu menu = getSlidingMenu();
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		menu.setBehindScrollScale(0.0F);
	}

	private void initViewPager() {

		// 从布局文件中获取ViewPager父容器
		LinearLayout pagerLayout = (LinearLayout) findViewById(R.id.view_pager_content);
		// 创建ViewPager
		advPager = new ViewPager(this);

		// 获取屏幕像素相关信息
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// 根据屏幕信息设置ViewPager广告容器的宽高
		LayoutParams laypar = new LayoutParams(dm.widthPixels,
				dm.heightPixels * 3 / 10);

		advPager.setLayoutParams(laypar);

		// 将ViewPager容器设置到布局文件父容器中
		pagerLayout.addView(advPager);
		group = (ViewGroup) findViewById(R.id.home_viewGroup);

		String baseurl = settings.getString("baseurl", "http://sxah.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("act", "postok");
		fp.post(baseurl + "/api/getginfoclient.php", ap,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						SetBean setBean = gson.fromJson(t.toString(),
								SetBean.class); // 获取imageviews路径 //
												// List<FuwuxiangmuBean>
						List<FuwuxiangmuBean> caidanguanggaoList = setBean
								.getCaidanguanggao();
						if (caidanguanggaoList != null) {
							for (int i = 0; i < caidanguanggaoList.size(); i++) {
								picnames[i] = caidanguanggaoList.get(i)
										.getName();
								Log.e("urllllll===", picnames[i]);
								Uri path = Uri.parse(picnames[i]);
								ImageView img = new ImageView(
										HomepageActivity.this);
								Picasso.with(HomepageActivity.this).load(path)
										.into(img);
								advPics.add(img);
							}
							// 对imageviews进行填充
							imageViews = new ImageView[advPics.size()];
							// 小图标
							for (int i = 0; i < advPics.size(); i++) {
								imageView = new ImageView(HomepageActivity.this);
								imageView.setLayoutParams(new LayoutParams(20,
										20));
								imageView.setPadding(0, 2, 0, 5);
								imageViews[i] = imageView;

								if (i == 0) {
									imageViews[i]
											.setBackgroundResource(R.drawable.dot_current);
								} else {
									imageViews[i]
											.setBackgroundResource(R.drawable.dot_normal);
								}
								group.addView(imageViews[i]);
							}
							advPager.setAdapter(new HomeAdvAdapter(advPics));
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}
				});

		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		advPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(what.get());
						whatOption();
					}
				}
			}

		}).start();
	}

	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > advPics.size() - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			advPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};
	//private HomeMenuviewpagerAdapter viewadapter;

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < advPics.size(); i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.dot_current);

				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.dot_normal);
				}
			}

		}

	}

	public void initMenulistdata() {
		String userid = settings.getString("userid", "");
		String baseurl = settings.getString("baseurl", "http://sxah.k76.net");

		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/fuwuleixinclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Gson gson = new Gson();
						Type listType = new TypeToken<LinkedList<HomescrollBean>>() {
						}.getType();
						LinkedList<HomescrollBean> beanlsits = gson.fromJson(
								t.toString(), listType);

						for (Iterator<HomescrollBean> iterator = beanlsits
								.iterator(); iterator.hasNext();) {
							HomescrollBean type = (HomescrollBean) iterator
									.next();
							servicetypelists.add(type);
							HomeListviewAdapter adapter = new HomeListviewAdapter(
									HomepageActivity.this, servicetypelists);
							typelist.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
					}
				});
	}

	/***
	 * @return
	 */
	public static final String getAppSDPath() {
		File file = new File(Environment.getExternalStorageDirectory(), "apk");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static final String getAppTmpSDPath() {
		File file = new File(getAppSDPath(), TEMPPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static String getFileNameFromUrl(String s) {
		int i = s.lastIndexOf("\\");
		if (i < 0 || i >= s.length() - 1) {
			i = s.lastIndexOf("/");
			if (i < 0 || i >= s.length() - 1)
				return s;
		}
		return s.substring(i + 1);
	}

	/**
	 * 显示升级对话框
	 */
	public void showUpdate_Dialog(String newVersion, final String downUrl,
			final String isAPK) {
		String newverStr = "有新版本更新啦！";
		final String version_downloading = this.getResources().getString(
				R.string.version_downloading, newVersion);
		final String installapkfile = getAppTmpSDPath() + "/"
				+ getFileNameFromUrl(downUrl);
		Assign_UpDateDialog.showUpdateDialog(this, newverStr, "增加新功能，优化性能！",
				"立即尝鲜", "残忍拒绝", new ConfirmDialogListener() {
					@Override
					public void onPositive(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (isAPK.equals("yes")) {
							Downloadhelper dm = new Downloadhelper(
									HomepageActivity.this, true);
							dm.downLoadFile(version_downloading, downUrl,
									installapkfile);
							dialog.cancel();
						} else {
							if (downUrl != null && !downUrl.equals("")) {

								Intent intent = new Intent(
										HomepageActivity.this,
										CheckHtmlActivity.class);
								intent.putExtra("baseurl", downUrl);
								startActivity(intent);
							}
						}

					}

					@Override
					public void onNegative(DialogInterface dialog) {
						dialog.cancel();
					}
				});

	}

	private void checkUPDATE() {

		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		params.put("appname", "android");
		fp.post(baseurl + "/api/shengjiclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						UpDateInfo upDateBean = gson.fromJson(t.toString(),
								UpDateInfo.class);
						if (upDateBean != null) {
							if (!upDateBean.getVer().equals("7.4.20170419")) {
								showUpdate_Dialog(upDateBean.getVer(),
										upDateBean.getUrl(),
										upDateBean.getApk());
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});

	}

	private void getMessage() {

		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/tongzhiclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						MessageLisyBean messagelisybean = gson.fromJson(
								t.toString(), MessageLisyBean.class);
						if (messagelisybean != null) {
							unRead = messagelisybean.getUnread();
							if (TextUtils.isEmpty(unRead)) {
								unRead = "0";
							}
							String ordernum = messagelisybean.getDingdannum();
							if (TextUtils.isEmpty(ordernum)) {
								ordernum = "0";
							}
							if (!unRead.equals("0") || !ordernum.equals("0")) {
								message_point.setVisibility(View.VISIBLE);
								message_point.setText(Integer.parseInt(unRead)
										+ Integer.parseInt(ordernum) + "");
							} else {
								message_point.setVisibility(View.GONE);
							}

							if (messagelisybean.getTongzhilist() != null
									&& messagelisybean.getTongzhilist().size() > 0) {
								messagelist.addAll(messagelisybean
										.getTongzhilist());
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});

	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			city_txt.setText(location.getCity());
			gpscity = location.getCity();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			String scity = data.getStringExtra("seclectcity");
			if (requestCode == 1) {
				city_txt.setText(scity);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
