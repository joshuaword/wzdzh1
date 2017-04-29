package net.k76.wzd.wxapi;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.k76.wzd.R;
import net.k76.wzd.wxapi.constant.MD5;
import net.k76.wzd.wxapi.constant.Util;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dajia.Bean.WXzhifuInfo;
import com.dajia.Bean.zhifuInfo;
import com.dajia.activity.BaseActivity;
import com.dajia.activity.ZhifuHtmlActivity;
import com.dajia.p.Keys;
import com.dajia.p.PayResult;
import com.dajia.p.SignUtils;
import com.dajia.view.SegmentLayout;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 支付
 * 
 * @author Administrator
 * 
 */
public class WXPayEntryActivity extends BaseActivity implements
		OnClickListener, IWXAPIEventHandler {
	public static final String TAG = "alipay-sdk";
	private Keys key = new Keys();
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
	private boolean isExist;// 是否存在支付宝客户端
	private TextView account;
	private RelativeLayout zhifubao_layout;
	private RelativeLayout weixin_layout;
	private zhifuInfo info;
	private WXzhifuInfo wxinfo;

	private String userid;
	private SharedPreferences settings;
	private EditText zidingyi;

	private CheckBox weixin_checkbox, zhifubao_checkbox;

	private static final int SDK_PAY_FLAG = 1;
	/**
	 * 微信接口
	 */
	private final int WXPAYSUCEESS = 0;// suceess成功
	private final int WXPAYFAILED = -1;// failed 失败
	private final int WXPAYCANCLE = -2;// cancle 取消

	private PayReq req;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private Map<String, String> resultunifiedorder;
	private StringBuffer sb;

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
					Toast.makeText(WXPayEntryActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(WXPayEntryActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(WXPayEntryActivity.this, "您已取消支付！",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(WXPayEntryActivity.this, "检查结果为：" + msg.obj,
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
		zhifubao_checkbox = (CheckBox) findViewById(R.id.wzhifubao_checkbox);
		weixin_checkbox = (CheckBox) findViewById(R.id.wweixin_checkbox);
		zhifubao_checkbox.setChecked(true);
		zhifubao_layout = (RelativeLayout) findViewById(R.id.wlayout_recharge_alipay);
		weixin_layout = (RelativeLayout) findViewById(R.id.wlayout_recharge_weixin);
		zhifubao_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (weixin_checkbox.isChecked()) {
					weixin_checkbox.setChecked(false);
					zhifubao_checkbox.setChecked(true);
					zhifuType = "zhifubao";
				}

			}
		});
		weixin_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (zhifubao_checkbox.isChecked()) {
					zhifubao_checkbox.setChecked(false);
					weixin_checkbox.setChecked(true);
					zhifuType = "weixin";
				}
			}
		});
		// 设置支付方式（微信）
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

		// 设置支付方式（支付宝）
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

		// 自定义金额
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
		 * 
		 * layout_recharge_bank = (RelativeLayout)
		 * findViewById(R.id.layout_recharge_bank); layout_recharge_alipay =
		 * (RelativeLayout) findViewById(R.id.layout_recharge_alipay);
		 * layout_recharge_pp = (RelativeLayout)
		 * findViewById(R.id.layout_recharge_pp);
		 * layout_recharge_bank.setOnClickListener(this);
		 * layout_recharge_alipay.setOnClickListener(this);
		 * layout_recharge_pp.setOnClickListener(this);
		 */

		rechargeGo = (LinearLayout) findViewById(R.id.recharge_go);
		rechargeGo.setOnClickListener(this);

		account = (TextView) findViewById(R.id.recharge_account_text);
		account.setText(phone);

		moneyLayoutOne.setFlag(true);
		wxinfo = new WXzhifuInfo();
		initWXPay();// 初始化微信接口
	}

	private void finishPay() {
		String money1 = zidingyi.getText().toString().trim();
		// 如果自定义金额不为空，则提交自定义金额
		if (!money1.equals("")) {
			money = money1;
		}
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
				PayTask alipay = new PayTask(WXPayEntryActivity.this);
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
				PayTask payTask = new PayTask(WXPayEntryActivity.this);
				isExist = payTask.checkAccountIfExist();

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
		PayTask payTask = new PayTask(WXPayEntryActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(WXPayEntryActivity.this, version, Toast.LENGTH_SHORT)
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

	/** 微信支付 ***/
	private void initWXPay() {
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(wxinfo.getAppid());
		// msgApi.registerApp(Constants.APP_ID);
	}

	/**
	 * 验证包名
	 * 
	 * @author fucheng 2015年3月6日
	 */
	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");

		// sb.append( Constants.PARTNER_KEY); //
		// 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成
		// 进行md5摘要前，params内容为原始内容，未经过url encode处理
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}

	/**
	 * 微信支付订单信息
	 * 
	 * @return
	 */
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", wxinfo.getAppid()));
			packageParams.add(new BasicNameValuePair("body", wxinfo
					.getProduct()));
			packageParams.add(new BasicNameValuePair("mch_id", wxinfo
					.getMchid()));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", wxinfo
					.getZhifubaoreturnurl()));
			packageParams.add(new BasicNameValuePair("out_trade_no", wxinfo
					.getDingdanhao()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", wxinfo
					.getIp()));
			int price = (int) (Double.parseDouble("1") * 100);
			// String jine = wxinfo.getJine() + "00";
			packageParams.add(new BasicNameValuePair("total_fee", wxinfo
					.getJine()));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return xmlstring;
		} catch (Exception e) {
			Log.e("order", "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	private void getWXzhifu() {
		String money2 = zidingyi.getText().toString().trim();
		if (!money2.equals(""))
			money = money2;

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("jine", money);
		ap.put("act", "postok");
		Log.e("12345", "userid=" + userid + "money" + money);

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		fp.post(baseurl + "/api/chongzhiclient_wx.php", ap,
				new AjaxCallBack<Object>() {
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						Log.e("12345aaa", t.toString());
						WXzhifuInfo wxoder = gson.fromJson(t.toString(),
								WXzhifuInfo.class);

						if (wxoder != null) {
							wxinfo.setAppid(wxoder.getAppid());
							wxinfo.setApikey(wxoder.getApikey());

							wxinfo.setIp(wxoder.getIp());
							wxinfo.setDingdanhao(wxoder.getDingdanhao());
							wxinfo.setZhifubaoreturnurl(wxoder
									.getZhifubaoreturnurl());
							wxinfo.setMchid(wxoder.getMchid());
							wxinfo.setJine(wxoder.getJine());
							wxinfo.setProduct(wxoder.getProduct());
						}

					}
				});
	}

	// private String genProductArgs() {
	// StringBuffer xml = new StringBuffer();
	// try {
	// String nonceStr = genNonceStr();
	// xml.append("</xml>");
	// List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
	// packageParams
	// .add(new BasicNameValuePair("appid", Constants.APP_ID));
	// packageParams
	// .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
	// packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
	// packageParams.add(new BasicNameValuePair("trade_type", "APP"));
	// // packageParams.add(new BasicNameValuePair("body", orderinfodetail
	// // .getName()));
	// packageParams.add(new BasicNameValuePair("body", "ticket"));
	// int price = (int) (Double.parseDouble(orderinfodetail
	// .getFactPrice()) * 100);
	// packageParams.add(new BasicNameValuePair("total_fee", price + ""));
	// packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
	// packageParams.add(new BasicNameValuePair("notify_url",
	// ZHConstant.App.WXPAY_NOTIFY_URL));
	// packageParams.add(new BasicNameValuePair("out_trade_no", orderInfo
	// .getOrderId()));
	// packageParams.add(new BasicNameValuePair("partner",
	// ZHConstant.App.PARTNER_ID));
	// packageParams.add(new BasicNameValuePair("spbill_create_ip",
	// ZHConstant.App.WXPAY_SPBILL_CREATE_IP));
	// String sign = genPackageSign(packageParams);
	// packageParams.add(new BasicNameValuePair("sign", sign));
	//
	// String xmlstring = toXml(packageParams);
	// return xmlstring;
	// } catch (Exception e) {
	// return null;
	// }
	//
	// }

	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wxinfo.getApikey());
		// sb.append(Constants.API_KEY);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return packageSign;
	}

	private String toXml(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		Log.e("orion", sb.toString());
		return new String(sb.toString().getBytes(), "ISO8859-1");
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
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
			zidingyi.setText("");
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
				Toast.makeText(WXPayEntryActivity.this, "请先选择充值的金额",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// zhifuHtml();
			if (zhifuType.equals("zhifubao")) {
				finishPay();

			} else {
				boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;// true
				if (isPaySupported == true) {
					getWXzhifu();
					GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
					getPrepayId.execute();
					GetPrepayIdTask getPrepayId1 = new GetPrepayIdTask();
					getPrepayId1.execute();
				} else {
					Toast.makeText(WXPayEntryActivity.this,
							"您没有安装微信或您的微信版本过低,请下载最新的微信版本再试！",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
	}

	private void zhifuHtml() {
		Intent intent = new Intent(WXPayEntryActivity.this,
				ZhifuHtmlActivity.class);
		intent.putExtra("money", money);
		startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		msgApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	/**
	 * 微支付回调
	 */
	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == WXPAYSUCEESS) {
				Toast.makeText(WXPayEntryActivity.this, "支付成功!",
						Toast.LENGTH_SHORT).show();
			} else if (resp.errCode == WXPAYFAILED) {
				Toast.makeText(WXPayEntryActivity.this, "支付失败,请重试!",
						Toast.LENGTH_SHORT).show();
			} else if (resp.errCode == WXPAYCANCLE) {
				Toast.makeText(WXPayEntryActivity.this, "您已取消支付！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void sendPayReq() {
		Log.e("aaaaaa+sendPayRep", "iiiiiisendPayRep");
		msgApi.registerApp(wxinfo.getAppid());
		// msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		Log.e("bbbbb+sendRep", "iiiiiisendRep");
	}

	private void genPayReq() {
		req.appId = wxinfo.getAppid();
		req.partnerId = wxinfo.getMchid();
		// req.appId = Constants.APP_ID;
		// req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		Log.d("", "sign" + sb.append("sign\n" + req.sign + "\n\n"));
		sendPayReq();
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wxinfo.getApikey());
		// sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion", appSign);
		return appSign;
	}

	/**
	 * @author fucheng 2015年4月22日
	 */
	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WXPayEntryActivity.this,
					getString(R.string.app_tip),
					getString(R.string.getting_prepayid));
			Log.e("aaaaaa1111", "Frist");
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {

			if (dialog != null) {
				dialog.dismiss();
			}
			resultunifiedorder = result;
			Log.e("aaaaaa2222result", result.toString());
			genPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();
			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			Log.e("rrrorion+ content=", content);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

}
