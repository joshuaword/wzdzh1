package com.dajia.activity;


import com.dajia.Bean.MessageBean;
import com.google.gson.Gson;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 评论
 * 
 * @author Administrator
 * 
 */
public class PinglunActivity extends Activity {
	private TextView title;
	private LinearLayout backLayout;
	//private TextView txt_choice;
	Button commentBtn;
	RatingBar commentRating;
	EditText commentEdit;
	private String dingdanid;
	//private ArrayList<String> pingLunListString = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinglun_layout);
		title = (TextView) findViewById(R.id.title);
		title.setText("评价");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	/*	txt_choice = (TextView) findViewById(R.id.txt_choice);
		txt_choice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSinChosDia();
			}
		});*/
		commentEdit = (EditText) findViewById(R.id.commentEdit);
		commentBtn = (Button) findViewById(R.id.comment);
		commentBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submit("no");
			}
		});
		commentRating = (RatingBar) findViewById(R.id.ratingBar);
		dingdanid = getIntent().getStringExtra("dingDanId");
	}

	public void submit(String flag) {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("dingdanid", dingdanid);
		ap.put("dafen", String.valueOf(commentRating.getRating()));
		ap.put("leirong", commentEdit.getText().toString());
		ap.put("ifdel", flag);
		ap.put("act", "postok");

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
								Toast.makeText(PinglunActivity.this, "评价成功",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(
										PinglunActivity.this,
										CompleteOrderActivity.class);
								startActivity(intent);
							}
							if ("删除订单成功".equals(msg.getMsg())) {
								Toast.makeText(PinglunActivity.this, "删除订单成功",
										Toast.LENGTH_SHORT).show();
							}
						}
						finish();
					}
				});
	}

	/*
	 //选择评论
	 int yourChose = -1;

	private void showSinChosDia() {

		if (pingLunListString.size() > 0) {
			String[] strings = new String[pingLunListString.size()];
			final String punlun[] = pingLunListString.toArray(strings);
			yourChose = -1;
			AlertDialog.Builder sinChosDia = new AlertDialog.Builder(
					PinglunActivity.this);
			sinChosDia.setTitle("选择评论");
			sinChosDia.setSingleChoiceItems(punlun, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							yourChose = which;

						}
					});
			sinChosDia.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (yourChose != -1) {
								showClickMessage(punlun[yourChose]);
							}
						}
					});
			sinChosDia.create().show();
		}

	}

	// 显示点击的内容 
	private void showClickMessage(String message) {
		commentEdit.setText(message);
	}*/
}
