package com.dajia.fragment;

import java.util.ArrayList;
import java.util.List;

import net.k76.wzd.R;
import net.k76.wzd.wxapi.WXPayEntryActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dajia.Bean.MessageBean;
import com.dajia.Bean.MessageLisyBean;
import com.dajia.Bean.UserBean;
import com.dajia.activity.AcountActivity;
import com.dajia.activity.ChaxunWGActivity;
import com.dajia.activity.GuangWangHtmlActivity;
import com.dajia.activity.HistoryOrderActivity;
import com.dajia.activity.JifenHtmlActivity;
import com.dajia.activity.LoginActivity;
import com.dajia.activity.MessageListActivity;
import com.dajia.activity.MipcaActivityCapture;
import com.dajia.activity.PriceActivity;
import com.dajia.activity.SettingActivity;
import com.dajia.activity.ShareActivity;
import com.dajia.activity.YouhuiAcitivity;
import com.dajia.view.RoundImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * slidingmenu 左侧
 * 
 * @author Administrator
 * 
 */
public class MenuFragment extends Fragment implements OnClickListener {
	private View mView;
	private LinearLayout menuHead;
	private TextView menuAccountBalance;// 余額
	private TextView menuCoupon;
	private TextView menuAccountEcoins;
	private LinearLayout menuHistoryorder;
	private LinearLayout menuRecharge;
	private LinearLayout menuInvoiceApply;
	private LinearLayout menuFeedback;
	private LinearLayout menuPrice;
	private LinearLayout menuSetting;
	private LinearLayout menu_chaxun;
	private MessageLisyBean messagelisybean;
	private TextView message_point;
	public List<MessageBean> messagelist = new ArrayList<MessageBean>();
	public String unRead, dingdannum;
	private TextView name;
	private TextView phone, txt_message;
	private int size = 0;
	private TextView kefuPhone, img_has_notice;

	private UserBean userBean = new UserBean();

	String kefuPhoneString;
	public static final String SP_KEY_PHONE = "sp_phone";
	String dengluhao;
	String baseurl;
	private String phoneString,passwordString;
	private LinearLayout menuErweima,menuStepCounter;
	SharedPreferences sp;
	private ListView listView;
	private RoundImageView headimg;
	private int TIME = 10000;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				if (messagelisybean.getTongzhilist().size() > 0) {

					size++;
					if (size >= messagelisybean.getTongzhilist().size()) {
						size = 0;
						txt_message.setText(messagelisybean.getTongzhilist()
								.get(size).getBiaoti());
					} else {

						txt_message.setText(messagelisybean.getTongzhilist()
								.get(size).getBiaoti());
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			initView(inflater, container);
		}

		sp = getActivity().getSharedPreferences("setting", 0);
		dengluhao = sp.getString("dengluhao", "");
		kefuPhoneString = sp.getString("kefuphone", "");
		baseurl = sp.getString("baseurl", "http://wzd.k76.net");
		//ifshowcheliang = sp.getString("ifshowcheliang", "no");
		kefuPhone.setText(kefuPhoneString);

		return mView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMessage();
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("telphone", phoneString);
		params.put("password", passwordString);
		params.put("act", "postok");
		Log.e("MUSIC", "LOGIN--baseurl=" + baseurl + " dengluhao=" + dengluhao);
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {
							if (!TextUtils.isEmpty(userBean.getNickname())) {
								name.setText(userBean.getNickname());
							}else {
								name.setText("欢迎您，请登录");
							}
							if (!TextUtils.isEmpty(userBean.getTelphone())) {
								phone.setText(userBean.getTelphone());
							}
							menuAccountBalance.setText(userBean.getStryue());// 余额
							menuCoupon.setText(userBean.getStryouhuijuan());
							menuAccountEcoins.setText(userBean.getStrjifen());

							SharedPreferences.Editor editor = sp.edit();
							editor.putString("userid", userBean.getUserid());
							if (TextUtils.isEmpty(sp.getString(SP_KEY_PHONE, ""))) {
								editor.putString(SP_KEY_PHONE,
										userBean.getTelphone());
							}
							editor.commit();
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

	private void initView(LayoutInflater inflater, ViewGroup container) {
		// 加载左slidingmenu布局
		mView = inflater.inflate(R.layout.menu_main_layout, container, false);
		menuHead = (LinearLayout) mView.findViewById(R.id.menu_head);
		img_has_notice = (TextView) mView.findViewById(R.id.img_has_notice);

		txt_message = (TextView) mView.findViewById(R.id.txt_message);
		txt_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MessageListActivity.class);
				startActivity(intent);
			}
		});

		message_point = (TextView) mView.findViewById(R.id.order_message_point);
		listView = (ListView) mView.findViewById(R.id.messlistView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MessageListActivity.class);
				startActivity(intent);
			}
		});
		menuHead.setOnClickListener(this);
		menuAccountBalance = (TextView) mView
				.findViewById(R.id.menu_account_balance);
		menuAccountBalance.setOnClickListener(this);
		menuCoupon = (TextView) mView.findViewById(R.id.menu_coupon);
		menuCoupon.setOnClickListener(this);
		menuAccountEcoins = (TextView) mView
				.findViewById(R.id.menu_account_ecoins);
		menuAccountEcoins.setOnClickListener(this);
		menuHistoryorder = (LinearLayout) mView
				.findViewById(R.id.menu_historyorder);
		menuHistoryorder.setOnClickListener(this);
		menuRecharge = (LinearLayout) mView.findViewById(R.id.menu_recharge);
		menuRecharge.setOnClickListener(this);
		menuInvoiceApply = (LinearLayout) mView
				.findViewById(R.id.menu_invoice_apply);
		menuInvoiceApply.setOnClickListener(this);
		menuFeedback = (LinearLayout) mView.findViewById(R.id.menu_feedback);
		menuFeedback.setOnClickListener(this);
		menuPrice = (LinearLayout) mView.findViewById(R.id.menu_price);
		menuPrice.setOnClickListener(this);
		menuSetting = (LinearLayout) mView.findViewById(R.id.menu_setting);
		menuSetting.setOnClickListener(this);
		menu_chaxun = (LinearLayout) mView.findViewById(R.id.menu_chaxun);
		menu_chaxun.setOnClickListener(this);

		menuErweima = (LinearLayout) mView.findViewById(R.id.menu_erweima);
		menuErweima.setOnClickListener(this);
		
		menuStepCounter=(LinearLayout) mView.findViewById(R.id.menu_stepcount);
		menuStepCounter.setOnClickListener(this);

		name = (TextView) mView.findViewById(R.id.menu_account_name);
		phone = (TextView) mView.findViewById(R.id.menu_account_telephone);
		headimg = (RoundImageView) mView.findViewById(R.id.menu_account_image);
		SharedPreferences setting=getActivity().getSharedPreferences(
				"setting", Context.MODE_PRIVATE);
		phoneString = setting.getString(SP_KEY_PHONE, "");
		passwordString= setting.getString("sp_password", "");
		if (phoneString!=null) {
			phone.setText(phoneString);
		}
		
		String nameString=setting.getString("nickname", "");
		if (nameString!=null) {
			name.setText(nameString);
		}else {
			name.setText("欢迎您，请登录");
		}
		
		String headimgpath=setting.getString("headimgpath", "");
		if (headimgpath!=null) {
			Log.e("headimgpath", headimgpath);
			Picasso.with(getActivity()).load(Uri.parse(headimgpath)).placeholder(R.drawable.logo).error(R.drawable.logo).into(headimg);
		}
		// 客服电话点击事件
		kefuPhone = (TextView) mView.findViewById(R.id.telephonetext);
		kefuPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ kefuPhoneString));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent balanceIntent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_head:
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
		/*	if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			if (yanzhen()) {
				Intent intent = new Intent(getActivity(), AcountActivity.class);
				startActivity(intent);
			}

			break;
		// 余额点击事件
		case R.id.menu_account_balance:
			// forwardMyAcount("0");
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
		/*	if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			balanceIntent = new Intent(getActivity(), YouhuiAcitivity.class);// 跳转到我的账户(余额)
			balanceIntent.putExtra("userid", userBean.getUserid());
			balanceIntent.putExtra("menu", "0");
			startActivity(balanceIntent);
			break;

		case R.id.menu_coupon:
			// forwardMyAcount("1");
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
		/*	if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			balanceIntent = new Intent(getActivity(), YouhuiAcitivity.class);// 跳转到我的账户(优惠券)
			balanceIntent.putExtra("userid", userBean.getUserid());
			balanceIntent.putExtra("menu", "1");
			startActivity(balanceIntent);
			break;

		case R.id.menu_account_ecoins:
			// forwardMyAcount("2");
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
			/*if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			balanceIntent = new Intent(getActivity(), YouhuiAcitivity.class);// 跳转到我的账户(积分)
			balanceIntent.putExtra("userid", userBean.getUserid());
			balanceIntent.putExtra("menu", "2");
			startActivity(balanceIntent);
			break;

		case R.id.menu_historyorder:
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
		/*	if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			Intent hisIntent = new Intent(getActivity(),
					HistoryOrderActivity.class);
			hisIntent.putExtra("userid", userBean.getUserid());
			startActivity(hisIntent);
			break;

		case R.id.menu_recharge:// 充值
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return;
			}
			/*if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(getActivity(), VehicleActivity.class);
				startActivity(intent);
				return;
			}*/
			Intent reIntent = new Intent(getActivity(),
					WXPayEntryActivity.class);
			reIntent.putExtra("phone", userBean.getTelphone());
			startActivity(reIntent);
			break;

		case R.id.menu_invoice_apply:
			Intent appIntent = new Intent(getActivity(), ShareActivity.class);
			startActivity(appIntent);
			break;

		case R.id.menu_feedback:// 跳转到公司简介
			Intent feedIntent = new Intent(getActivity(),
					GuangWangHtmlActivity.class);
			startActivity(feedIntent);
			break;

		case R.id.menu_price:
			Intent priceIntent = new Intent(getActivity(), PriceActivity.class);
			startActivity(priceIntent);
			break;

		case R.id.menu_setting:
			Intent setIntent = new Intent(getActivity(), SettingActivity.class);
			startActivity(setIntent);
			break;
		case R.id.menu_chaxun:
			Intent intent = new Intent(getActivity(), ChaxunWGActivity.class);
			final SharedPreferences settings = getActivity()
					.getSharedPreferences("setting", 0);
			String weizhang = settings.getString("weizhang", "");
			if (weizhang != null) {
				intent.putExtra("baseurl", weizhang);
			}
			startActivity(intent);
			break;
		case R.id.menu_erweima:
			Intent erweimaIntent = new Intent(getActivity(),
					MipcaActivityCapture.class);
			startActivity(erweimaIntent);
			break;
		/*case R.id.menu_stepcount:
			Intent stepIntent = new Intent(getActivity(),
					StepCountActivity.class);
			startActivity(stepIntent);
			break;*/
		default:
			break;
		}
	}

	private void forwardMyAcount(String flag) {
		Intent balanceIntent = new Intent(getActivity(),
				JifenHtmlActivity.class);
		// balanceIntent.putExtra("flag", flag);
		balanceIntent.putExtra("userid", userBean.getUserid());
		startActivity(balanceIntent);
	}

	public Boolean yanzhen() {
		SharedPreferences sp = getActivity()
				.getSharedPreferences("settings", 0);
		//String phone = sp.getString("phone", "");
		String yanzhenma = sp.getString("ifyanzhenma", "no");
		if ("yes".equals(yanzhenma)) {
			if (TextUtils.isEmpty(phoneString)) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				return false;
			}
		}

		return true;
	}

	private void getMessage() {
		final SharedPreferences settings = getActivity().getSharedPreferences(
				"setting", 0);
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
						messagelisybean = gson.fromJson(t.toString(),
								MessageLisyBean.class);
						if (messagelisybean != null) {
							unRead = messagelisybean.getUnread();
							dingdannum = messagelisybean.getDingdannum();
							if (unRead != null) {
								if (!unRead.equals("0")) {
									img_has_notice.setVisibility(View.VISIBLE);
									img_has_notice.setText(unRead);
								} else {
									img_has_notice
											.setVisibility(View.INVISIBLE);
									txt_message.setText("没有最新消息");
								}
							}
							if (!dingdannum.equals("0")) {
								message_point.setVisibility(View.VISIBLE);
								message_point.setText(dingdannum);
							} else {
								message_point.setVisibility(View.GONE);
							}

							if (messagelisybean.getTongzhilist() != null
									&& messagelisybean.getTongzhilist().size() > 0) {
								messagelist.addAll(messagelisybean
										.getTongzhilist());
								txt_message.setText(messagelist.get(0)
										.getBiaoti());
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
}