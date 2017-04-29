package com.dajia;

import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.dajia.Bean.SetBean;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Application;
import net.k76.wzd.R;

public class VehicleApp extends Application {
	private static VehicleApp instance;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private List<Activity> activitys = new LinkedList<Activity>();
	private SetBean setBean = new SetBean();
	
	

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

		
		
}
