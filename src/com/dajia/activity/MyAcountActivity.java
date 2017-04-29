package com.dajia.activity;

import net.k76.wzd.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAcountActivity extends BaseActivity implements OnClickListener {

	private TextView title;
	private TextView rightBtn;
	private LinearLayout yeLayout;
	private LinearLayout yhqLayout;
	private LinearLayout eLayout;
	private ImageView yeImage;
	private ImageView yhqImage;
	private ImageView eImage;
	private TextView yeText;
	private TextView yhqText;
	private TextView eText;
	private LinearLayout yeContentLayout;
	private ImageView emptyIconImg;
	private TextView emptyTitleTv;
	private FrameLayout emptyLayout;
	private LinearLayout ecoinLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);

		title = (TextView) findViewById(R.id.title);
		title.setText("我的账号");
		rightBtn = (TextView) findViewById(R.id.btnRight);
		rightBtn.setText("开发票");

		yeLayout = (LinearLayout) findViewById(R.id.ye_layout);
		yhqLayout = (LinearLayout) findViewById(R.id.yhq_layout);
		yhqLayout.setVisibility(View.GONE);
		eLayout = (LinearLayout) findViewById(R.id.e_layout);
		yeLayout.setOnClickListener(this);
		yhqLayout.setOnClickListener(this);
		eLayout.setOnClickListener(this);

		yeImage = (ImageView) findViewById(R.id.ye_image);
		yhqImage = (ImageView) findViewById(R.id.yhq_image);
		eImage = (ImageView) findViewById(R.id.e_image);

		yeText = (TextView) findViewById(R.id.ye_text);
		yhqText = (TextView) findViewById(R.id.yhq_text);
		eText = (TextView) findViewById(R.id.e_text);

		Intent intent = getIntent();
		String flag = intent.getStringExtra("flag");
		
		yeContentLayout = (LinearLayout) findViewById(R.id.ye_content);
		emptyIconImg = (ImageView) findViewById(R.id.img_empty_icon);
		emptyTitleTv = (TextView) findViewById(R.id.tv_empty_title);
		emptyLayout = (FrameLayout) findViewById(R.id.empty_layout);
		ecoinLayout = (LinearLayout) findViewById(R.id.ecoin_layout);
		
		if ("1".equals(flag)) {
			yhqLayout.performClick();
		}
		if ("2".equals(flag)) {
			eLayout.performClick();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ye_layout:

			left();

			break;

		case R.id.yhq_layout:

			middle();

			break;

		case R.id.e_layout:
			right();
			break;

		default:
			break;
		}
	}

	private void right() {
		rightBtn.setText("");
		yeContentLayout.setVisibility(View.GONE);
		emptyLayout.setVisibility(View.GONE);
		ecoinLayout.setVisibility(View.VISIBLE);
		
		yeLayout.setBackgroundResource(R.drawable.account_item_left_normal);
		yeImage.setBackgroundResource(R.drawable.myaccout_trade_title);
		yeText.setTextColor(getResources().getColor(
				R.color.segment_button_color));

		yhqLayout.setBackgroundResource(R.drawable.account_item_middle_normal);
		yhqImage.setBackgroundResource(R.drawable.coupon_title_icon);
		yhqText.setTextColor(getResources().getColor(
				R.color.segment_button_color));

		eLayout.setBackgroundResource(R.drawable.account_item_right_focused);
		eImage.setBackgroundResource(R.drawable.e_focused);
		eText.setTextColor(getResources().getColor(R.color.white));
	}

	private void middle() {
		rightBtn.setText("添加优惠券");
		yeContentLayout.setVisibility(View.GONE);
		emptyLayout.setVisibility(View.VISIBLE);
		ecoinLayout.setVisibility(View.GONE);
		emptyTitleTv.setText("暂无优惠券");
		emptyIconImg.setBackgroundResource(R.drawable.coupon_unavailabe);

		yeLayout.setBackgroundResource(R.drawable.account_item_left_normal);
		yeImage.setBackgroundResource(R.drawable.myaccout_trade_title);
		yeText.setTextColor(getResources().getColor(
				R.color.segment_button_color));

		yhqLayout.setBackgroundResource(R.drawable.account_item_middle_focused);
		yhqImage.setBackgroundResource(R.drawable.coupon_title_icon_hl);
		yhqText.setTextColor(getResources().getColor(R.color.white));

		eLayout.setBackgroundResource(R.drawable.account_item_right_normal);
		eImage.setBackgroundResource(R.drawable.e_normal);
		eText.setTextColor(getResources()
				.getColor(R.color.segment_button_color));
	}

	private void left() {
		rightBtn.setText("开发票");
		yeContentLayout.setVisibility(View.VISIBLE);
		emptyLayout.setVisibility(View.VISIBLE);
		ecoinLayout.setVisibility(View.GONE);
		emptyTitleTv.setText("暂无交易信息");
		emptyIconImg.setBackgroundResource(R.drawable.nopayorder);
		
		yeLayout.setBackgroundResource(R.drawable.account_item_left_focused);
		yeImage.setBackgroundResource(R.drawable.myaccout_trade_title_hl);
		yeText.setTextColor(getResources().getColor(R.color.white));

		yhqLayout.setBackgroundResource(R.drawable.account_item_middle_normal);
		yhqImage.setBackgroundResource(R.drawable.coupon_title_icon);
		yhqText.setTextColor(getResources().getColor(
				R.color.segment_button_color));

		eLayout.setBackgroundResource(R.drawable.account_item_right_normal);
		eImage.setBackgroundResource(R.drawable.e_normal);
		eText.setTextColor(getResources()
				.getColor(R.color.segment_button_color));
	}
}
