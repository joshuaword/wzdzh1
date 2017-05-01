package com.dajia;

import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.dajia.Bean.KeyItemArray;
import com.dajia.Bean.SetBean;
import com.dajia.util.ReflectException;
import com.dajia.util.SharedPreferenceUtil;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twzs.core.json.JSONException;

import android.app.Activity;
import android.app.Application;
import net.k76.wzd.R;

public class VehicleApp extends Application {
	private static VehicleApp instance;
	public final String KEY_CITY = "CITY";
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private List<Activity> activitys = new LinkedList<Activity>();
	private SetBean setBean = new SetBean();
	public final String  KEY_APICACHE = "apicache_";
	private SharedPreferenceUtil sharedPrefsUtil;
	private final String PREFERENCE_KEY_TAB = "tab";
	
	private final String PREFERENCE_KEYSEARCH_HISTORY = "keysearch";

	private final String SPLIT_CHAR = "_";
	

	public SetBean getSetBean() {
		return setBean;
	}

	public void setSetBean(SetBean setBean) {
		this.setBean = setBean;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		SDKInitializer.initialize(getApplicationContext());
		SpeechUtility.createUtility(VehicleApp.this, "appid=" + getString(R.string.app_id));
		if (sharedPrefsUtil == null) {
			sharedPrefsUtil = new SharedPreferenceUtil(this);
		}
	}

	public static VehicleApp getInstance() {
		return instance;
	}
	
	// 添加Activity到容器中
		public void addActivity(Activity activity) {
			if (activitys != null && activitys.size() > 0) {
				if (!activitys.contains(activity)) {
					activitys.add(activity);
				}
			} else {
				activitys.add(activity);
			}

		}
		
		public void exit() {
			if (activitys != null && activitys.size() > 0) {
				for (Activity activity : activitys) {
					activity.finish();
				}
			}
			System.exit(0);

			// 直接在服务中，暂停了。。。

			// 以便下次打开下载管理时，可以置成下载状态中。。。。
			// 发送广播，让其全部暂停掉下载...
			/*
			 * sendBroadcast(new
			 * Intent(DownloadService.PAUSEALLRECEIVER_ACTION).putExtra("isExit",
			 * true));
			 */
			// Log.i(TAG, "发送广播方式退出了.....");
			// 怎么停止掉服务...

			// Process.killProcess(Process.myPid());//停止掉本应用进程，即退出了应用
		}

		public List<Activity> getActivitys() {
			return activitys;
		}

		public void setActivitys(List<Activity> activitys) {
			this.activitys = activitys;
		}


		/**
		 * 是否存在同一个搜索记录
		 */
		public boolean hasKeyHistory(KeyItemArray histroy) {
			return sharedPrefsUtil.hasListItem(getKeyWordsSearch(), histroy);
		}

		// 获取搜索记录
		public List<KeyItemArray> getKeySearchHistory() throws ReflectException,
				 com.twzs.core.json.JSONException {
			List<KeyItemArray> histroys = sharedPrefsUtil.getListWithCast(
					new KeyItemArray(), getKeyWordsSearch());
			return histroys;
		}

		/**
		 * 删除某一个搜索记录
		 */
		public void delKeyHistory(KeyItemArray histroy) {
			sharedPrefsUtil.removeListItem(getKeyWordsSearch(), histroy);
		}

		/** 搜索记录数据 **/
		private String getKeyWordsSearch() {
			String key = KEY_APICACHE + getCityName() + SPLIT_CHAR + SPLIT_CHAR;
			return key + PREFERENCE_KEYSEARCH_HISTORY;
		}

		/**
		 * 清除所有的搜索记录
		 */
		public void cleanKeySearchHistroy() {
			sharedPrefsUtil.clean(getKeyWordsSearch());
		}

		/**
		 * 添加搜索记录
		 * @param histroy
		 */
		public void addKeySearchHistroy(KeyItemArray histroy) {
			if (hasKeyHistory(histroy)) {
				delKeyHistory(histroy);
			}
			int hcount = 0;
			List<KeyItemArray> histroys = null;
			try {
				histroys = sharedPrefsUtil.getListWithCast(new KeyItemArray(),
						getKeyWordsSearch());
				hcount = histroys.size();
			} catch (ReflectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (hcount >= 10) {
				// 删除一条最老的记录
				sharedPrefsUtil
						.removeListItem(getKeyWordsSearch(), histroys.get(0));
			}
			sharedPrefsUtil.addListItem(getKeyWordsSearch(), histroy);
		}
		public String getCityName() {
			// TODO Auto-generated method stub
			return SharedPreferenceUtil.getString(KEY_CITY, "南京");
		}
}
