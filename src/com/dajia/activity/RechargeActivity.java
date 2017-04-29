package com.dajia.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dajia.Bean.zhifuInfo;
import com.dajia.p.Keys;
import com.dajia.p.PayResult;
import com.dajia.p.SignUtils;
import com.dajia.view.SegmentLayout;
import com.google.gson.Gson;

public class RechargeActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	private Keys key = new Keys();
	private String orderid, price;
	private TextView title;
	private LinearLayout backLayout;
	private SegmentLayout moneyLayoutOne;
	private SegmentLayout moneyLayoutTwo;
	private SegmentLayout moneyLayoutThree;
	private SegmentLayout moneyLayoutFour;
	private String zhifuType = "zhifubao";
	/*
	 * private ImageView image_select_bankpay; private ImageView
	 * image_select_alipay; private ImageView image_select_pp;
	 */
	private RelativeLayout layout_recharge_bank;
	private RelativeLayout layout_recharge_alipay;
	private RelativeLayout layout_recharge_pp;
	private TextView moneyTwo;
	private TextView moneyThree;
	private TextView moneyFour;
	private LinearLayout rechargeGo;
	private String money = "100";
	// private String zhifuType;

	private TextView account;

	private zhifuInfo info;
	private String userid;
	private SharedPreferences settings;
	private EditText zidingyi;

	private CheckBox weixin_checkbox, zhifubao_checkbox;
	private RelativeLayout zhifubao_layout;
	private RelativeLayout weixin_layout;
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(RechargeActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(RechargeActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(RechargeActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge_detail);
		settings = getSharedPreferences("setting", 0);
		userid = settings.getString("userid", "");
		title = (TextView) findViewById(R.id.title);
		zhifubao_checkbox = (CheckBox) findViewById(R.id.zhifubao_checkbox);
		weixin_checkbox = (CheckBox) findViewById(R.id.weixin_checkbox);
		//zhifubao_layout = (RelativeLayout) findViewById(R.id.layout_recharge_alipay);
		//weixin_layout = (RelativeLayout) findViewById(R.id.layout_recharge_weixin);
		zhifubao_checkbox.setChecked(true);
		weixin_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							zhifubao_checkbox.setChecked(false);
							zhifuType = "weixin";
						}
					}
				});

		zhifubao_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							weixin_checkbox.setChecked(false);
							zhifuType = "zhifubao";
						}
					}
				});
		title.setText("充值");
		String phone = getIntent().getStringExtra("phone");

		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		zidingyi = (EditText) findViewById(R.id.edit_custom_money);
		zidingyi.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {

					// 此处为得到焦点时的处理内容
					moneyLayoutOne.setFlag(false);
					moneyLayoutTwo.setFlag(false);
					moneyLayoutThree.setFlag(false);
					moneyLayoutFour.setFlag(false);

				} else {

					// 此处为失去焦点时的处理内容
					moneyLayoutOne.setFlag(true);
					moneyLayoutTwo.setFlag(false);
					moneyLayoutThree.setFlag(false);
					moneyLayoutFour.setFlag(false);
					money = "100";

				}

			}

		});

		moneyLayoutOne = (SegmentLayout) findViewById(R.id.money_layout_one);
		moneyLayoutTwo = (SegmentLayout) findViewById(R.id.money_layout_two);
		moneyLayoutThree = (SegmentLayout) findViewById(R.id.money_layout_three);
		moneyLayoutFour = (SegmentLayout) findViewById(R.id.money_layout_four);
		moneyLayoutOne.setOnClickListener(this);
		moneyLayoutTwo.setOnClickListener(this);
		moneyLayoutThree.setOnClickListener(this);
		moneyLayoutFour.setOnClickListener(this);
		zidingyi.setOnClickListener(this);
		moneyTwo = (TextView) moneyLayoutTwo.findViewById(R.id.money_tv);
		moneyThree = (TextView) moneyLayoutThree.findViewById(R.id.money_tv);
		moneyFour = (TextView) moneyLayoutFour.findViewById(R.id.money_tv);
		moneyTwo.setText("300元");
		moneyThree.setText("500元");
		moneyFour.setText("1000元");

		/*
		 * image_select_bankpay = (ImageView)
		 * findViewById(R.id.image_select_bankpay); image_select_alipay =
		 * (ImageView) findViewById(R.id.image_select_alipay); image_select_pp =
		 * (ImageView) findViewById(R.id.image_select_pp);
		 */
		layout_recharge_bank = (RelativeLayout) findViewById(R.id.layout_recharge_bank);
		layout_recharge_alipay = (RelativeLayout) findViewById(R.id.layout_recharge_alipay);
		layout_recharge_pp = (RelativeLayout) findViewById(R.id.layout_recharge_pp);
		layout_recharge_bank.setOnClickListener(this);
		layout_recharge_alipay.setOnClickListener(this);
		layout_recharge_pp.setOnClickListener(this);

		rechargeGo = (LinearLayout) findViewById(R.id.recharge_go);
		rechargeGo.setOnClickListener(this);

		account = (TextView) findViewById(R.id.recharge_account_text);
		account.setText(phone);

		moneyLayoutOne.setFlag(true);

	}

	private void finishPay() {
		String money1 = zidingyi.getText().toString().trim();
		if (!money1.equals(""))
			money = money1;

		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("jine", money);
		params.put("act", "postok");

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		fp.post(baseurl + "/api/chongzhiclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						info = gson.fromJson(t.toString(), zhifuInfo.class);
						if (info != null) {
							key.setUrl(info.getZhifubaoreturnurl());
							key.setDEFAULT_PARTNER(info.getZhifubaopid());
							key.setDEFAULT_SELLER(info.getZhifubao());
							key.setPRIVATE(info.getZhifubaorsa());
							pay();
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

	public void pay() {
		// 订单
		String orderInfo = getNewOrderInfo();
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(RechargeActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 获取订单信息
	 * 
	 * @return
	 */
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(key.getDEFAULT_PARTNER());
		sb.append("\"&out_trade_no=\"");
		sb.append(info.getDingdanhao());// 订单id
		sb.append("\"&subject=\"");
		sb.append("客户端充值");// 订单名字
		sb.append("\"&total_fee=\"");
		sb.append(info.getJine());// 订单价格
		sb.append("\"&body=\"");
		sb.append("客户端充值产品");// 商品详情
		sb.append("\"&notify_url=\"");// 订单回调地址
		// 网址需要做URL编码
		// sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append(URLEncoder.encode(key.getUrl()));// 回调地址
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(key.getDEFAULT_SELLER());
		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"24h");
		sb.append("\"");
		return new String(sb);
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(RechargeActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(RechargeActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(RechargeActivity.this, version, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, key.getPRIVATE());
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.money_layout_one:

			moneyLayoutOne.setFlag(true);
			moneyLayoutTwo.setFlag(false);
			moneyLayoutThree.setFlag(false);
			moneyLayoutFour.setFlag(false);

			money = "100";
			zidingyi.setText("");

			break;

		case R.id.money_layout_two:

			moneyLayoutOne.setFlag(false);
			moneyLayoutTwo.setFlag(true);
			moneyLayoutThree.setFlag(false);
			moneyLayoutFour.setFlag(false);

			money = "300";
			zidingyi.setText("");

			break;

		case R.id.money_layout_three:

			moneyLayoutOne.setFlag(false);
			moneyLayoutTwo.setFlag(false);
			moneyLayoutThree.setFlag(true);
			moneyLayoutFour.setFlag(false);

			money = "500";
			zidingyi.setText("");
			break;

		case R.id.money_layout_four:

			moneyLayoutOne.setFlag(false);
			moneyLayoutTwo.setFlag(false);
			moneyLayoutThree.setFlag(false);
			moneyLayoutFour.setFlag(true);

			money = "1000";
			zidingyi.setText("");
			break;

		case R.id.edit_custom_money:

			moneyLayoutOne.setFlag(false);
			moneyLayoutTwo.setFlag(false);
			moneyLayoutThree.setFlag(false);
			moneyLayoutFour.setFlag(false);

			money = "1000";

			break;

		/*
		 * case R.id.layout_recharge_bank:
		 * 
		 * image_select_bankpay.setVisibility(View.VISIBLE);
		 * image_select_alipay.setVisibility(View.GONE);
		 * image_select_pp.setVisibility(View.GONE);
		 * 
		 * zhifuType = "yinlian";
		 * 
		 * break;
		 * 
		 * case R.id.layout_recharge_alipay:
		 * 
		 * image_select_bankpay.setVisibility(View.GONE);
		 * image_select_alipay.setVisibility(View.VISIBLE);
		 * image_select_pp.setVisibility(View.GONE);
		 * 
		 * zhifuType = "zhifubao";
		 * 
		 * break;
		 * 
		 * case R.id.layout_recharge_pp:
		 * 
		 * image_select_bankpay.setVisibility(View.GONE);
		 * image_select_alipay.setVisibility(View.GONE);
		 * image_select_pp.setVisibility(View.VISIBLE);
		 * 
		 * zhifuType = "pp"; break;
		 */

		case R.id.recharge_go:
			if (money.length() == 0) {
				Toast.makeText(RechargeActivity.this, "请先选择充值的金额",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// zhifuHtml();
			if (zhifuType.equals("zhifubao")) {
				finishPay();
			}
			break;

		default:
			break;
		}
	}

	private void zhifuHtml() {
		Intent intent = new Intent(RechargeActivity.this,
				ZhifuHtmlActivity.class);
		intent.putExtra("money", money);
		startActivity(intent);
	}

}
