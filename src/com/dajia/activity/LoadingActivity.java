package com.dajia.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.push.example.Utils;
import com.daijia.chat.ChatActivity;
import com.dajia.VehicleApp;
import com.dajia.Bean.FuwuxiangmuBean;
import com.dajia.Bean.SetBean;
import com.dajia.Bean.UserBean;
import com.google.gson.Gson;

public class LoadingActivity extends BaseActivity {

	int picNum;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private String x;
	private String y, address, city, province, district;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(LoadingActivity.this, "api_key"));
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		picNum = settings.getInt("picNum", 0);

		Boolean isFirst = settings.getBoolean("isFirst", true);
		if (isFirst) {
			/*
			 * SharedPreferences.Editor editor = settings.edit();
			 * editor.putBoolean("isFirst", false); editor.commit();
			 */

			new Thread() {
				public void run() {
					try {
						sleep(3500);

						Intent intent = new Intent(LoadingActivity.this,
								LicenseActivity.class);
						startActivity(intent);
						finish();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();

		} else {
			new Thread() {
				public void run() {
					try {
						sleep(3500);
						Intent intent = new Intent();

						if (picNum > 0) {
							intent.setClass(LoadingActivity.this,
									PicShowActivity.class);
							intent.putExtra("picNum", picNum);
						} else {
							 intent.setClass(LoadingActivity.this,
							 ChatActivity.class);
//							intent.setClass(LoadingActivity.this,
//									HomepageActivity.class);
						}
						startActivity(intent);
						finish();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}

		String dengluhao = settings.getString("dengluhao", "");
		if (dengluhao.length() <= 0) {
			FinalHttp fp = new FinalHttp();
			AjaxParams ap = new AjaxParams();
			ap.put("leixing", "lm"); // 用于区别之前的版本，获取联盟登录号
			ap.put("mystr", "android");
			ap.put("act", "postok");
			fp.post("http://wzd.k76.net/api/askdengluhao.php", ap,
					new AjaxCallBack<Object>() {
						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);
							Gson gson = new Gson();
							UserBean userBean = gson.fromJson(t.toString(),
									UserBean.class);
							if (userBean != null) {
								SharedPreferences.Editor editor = settings
										.edit();
								editor.putString("dengluhao",
										userBean.getDengluhao());
								editor.commit();
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
						}
					});
		}

		initMap();

	}

	/**
	 * 登陆的时候 把系统参数获取到，放到本地
	 */

	@SuppressLint("NewApi")
	public void getinfo() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String dengluhao = settings.getString("dengluhao", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("x", x);
		ap.put("y", y);
		ap.put("address", address);
		ap.put("city", city);
		ap.put("province", province);
		ap.put("district", district);
		ap.put("lmdengluhao", dengluhao); // 区别于老版本的登录号
		ap.put("pingtai", "android");
		ap.put("act", "postok");
		fp.post("http://wzd.k76.net/api/getginfoclient.php", ap,
				new AjaxCallBack<Object>() {
					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						SetBean setBean = gson.fromJson(t.toString(),
								SetBean.class);
						VehicleApp.getInstance().setSetBean(setBean);

						List<FuwuxiangmuBean> fuwuBeanList = setBean
								.getFuwuxiangmu();
						Set<String> setString = new HashSet<String>();
						Set<String> setPriceString = new HashSet<String>();
						for (int i = 0; i < fuwuBeanList.size(); i++) {
							setString.add(fuwuBeanList.get(i).getName());
						}

						for (int i = 0; i < fuwuBeanList.size(); i++) {
							setPriceString.add(fuwuBeanList.get(i).getPrice());
						}

						List<FuwuxiangmuBean> fuwushijianBeanList = setBean
								.getFuwushijian();
						Set<String> setfuwushijian = new HashSet<String>();
						for (int i = 0; i < fuwushijianBeanList.size(); i++) {
							setfuwushijian.add(fuwushijianBeanList.get(i)
									.getName());

						}

						final SharedPreferences settings = getSharedPreferences(
								"setting", 0);

						if (setBean != null) {
							SharedPreferences.Editor editor = settings.edit();
							editor.putString("weiweb", setBean.getWeiweb());
							editor.putString("weizhang",
									setBean.getWeizhangchaxun());
							editor.putString("shangwu", setBean.getShangwu());
							editor.putString("login", "1");
							editor.putString("baseurl", setBean.getBaseurl());
							editor.putString("kefuphone",
									setBean.getKefudianhua());
							editor.putString("appbanquan",
									setBean.getAppbanquan());

							String kefu = setBean.getKefudianhua();
							Log.e("MUSIC", "kefu=" + kefu);
							String mapCon = setBean.getAppmap();
							editor.putString("mapCon", mapCon);// 地图缩放比例(16)
							editor.putString("ifshowfuwuxiangmu",
									setBean.getIfshowfuwuxiangmu());
							editor.putString("ifshowfuwushijian",
									setBean.getIfshowfuwushijian());
							editor.putString("fenxiangurl",
									setBean.getFenxiangurl());
							editor.putString("ifshowfuwushijian",
									setBean.getIfshowfuwushijian());
							editor.putString("yuyuebegintime",
									setBean.getYuyuebegintime());
							editor.putString("yuyueendtime",
									setBean.getYuyueendtime());
							editor.putString("yuyueneedtime",
									setBean.getYuyueneedtime());
							editor.putString("ifnodrivernodan",
									setBean.getIfnodrivernodan());
							String fuwuxiangmu1 = setBean.getFuwuxiangmu1();
							Log.e("MUSIC", "set fuwuxiangmu1=" + fuwuxiangmu1);

							editor.putString("fuwuxiangmu1",
									setBean.getFuwuxiangmu1());
							editor.putString("fuwushijian1",
									setBean.getFuwushijian1());

							editor.putStringSet("fuwuxiangmu", setString);
							editor.putStringSet("fuwuxiangmuprice",
									setPriceString);
							editor.putStringSet("fuwushijian", setfuwushijian);

							editor.putString("quxiao", setBean.getQuxiao());
							editor.putString("quxiaoyuanyin1",
									setBean.getQuxiaoyuanyin1());
							editor.putString("quxiaoyuanyin2",
									setBean.getQuxiaoyuanyin2());
							editor.putString("quxiaoyuanyin3",
									setBean.getQuxiaoyuanyin3());
							editor.putString("ifyanzhenma",
									setBean.getIfyanzhenma());
							editor.commit();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}
				});

	}

	public void initMap() {

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(500);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null) {
				getinfo();
			} else {
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();

				x = String.valueOf(location.getLongitude());
				y = String.valueOf(location.getLatitude());
				address = location.getAddrStr();
				city = location.getCity();
				province = location.getProvince();
				district = location.getDistrict();
				Log.e("MUSIC", "x=" + x + " y=" + y + " address=" + address
						+ " city=" + city);
				Log.e("MUSIC", "province=" + province + "district=" + district);

				final SharedPreferences settings = getSharedPreferences(
						"setting", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("x", x);
				editor.putString("y", y);
				editor.putString("address", address);
				editor.putString("city", city);
				editor.putString("province", province);
				editor.putString("district", district);
				editor.commit();
				mLocClient.stop();
				getinfo();
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
