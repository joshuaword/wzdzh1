package com.dajia.activity;

import com.dajia.VehicleApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	VehicleApp.getInstance().addActivity(this);
}

@Override
protected void onDestroy() {
    super.onDestroy();
    VehicleApp.getInstance().getActivitys().remove(this); // 退出时从集合中拿出
}

}
