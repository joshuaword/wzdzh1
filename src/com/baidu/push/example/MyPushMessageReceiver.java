package com.baidu.push.example;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.dajia.activity.MainActivity;

public class MyPushMessageReceiver extends BroadcastReceiver {

	public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("MyPushMessageReceiver",
				"MyPushMessageReceiverMyPushMessageReceiverMyPushMessageReceiver");
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {

		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			// 接受到消息的动作
			String content = intent.getExtras().getString(
					PushConstants.EXTRA_EXTRA);
			String activityId = null;
			if (content != null) {
				try {
					JSONObject contentJson = new JSONObject(content);
					activityId = contentJson.getString("activityId");
				} catch (JSONException e) {
				}
			}
			// 活动id
			if (activityId != null && !activityId.equals("")) {
				Bundle bundle = new Bundle();
				bundle.putString("activityId", activityId);
				intent.putExtras(bundle);
				intent.setClass(context, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {
				intent.setClass(context, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		}
	}
}
