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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dajia.Bean.zhifuInfo;
import com.dajia.p.Keys;
import com.dajia.p.PayResult;
import com.dajia.p.SignUtils;
import com.google.gson.Gson;

public class PayActivity extends BaseActivity {
	private Keys key = new Keys();
	private EditText edit_putmoney;
	private String money, orderid, price;
	private Button pay,xianjinPAY;
	public static final String TAG = "PayActivity";
	private static final int RQF_PAY = 1;
	private String userid;
	private SharedPreferences settings;
	private zhifuInfo info;
	private String dingdanid;
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
					Toast.makeText(PayActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_layout);
		dingdanid =getIntent().getStringExtra("Dingdanid");
		System.out.println("***Dingdanid*****"+dingdanid);
		edit_putmoney =(EditText)findViewById(R.id.edit_putmoney);
		pay = (Button)findViewById(R.id.pay);
		xianjinPAY= (Button)findViewById(R.id.btn_xianjin);
		settings = getSharedPreferences("setting", 0);
		userid = settings.getString("userid", "");
		finishPay();
		pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pay();
			}
		});
		xianjinPAY.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	};
	
	
	private void finishPay(){
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		System.out.println("***Dingdanid*****"+dingdanid);
		params.put("userid", userid);
		params.put("jine", money);
		params.put("dingdanid", dingdanid);
		params.put("act", "postok");
		
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		
		fp.post(baseurl + "/api/fufeiclient.php", params,
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
							edit_putmoney.setText(info.getJine());
							if(info.getIfxiugai().equals("1")){
								edit_putmoney.setEnabled(true);
							}else{
								edit_putmoney.setEnabled(false);
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
				PayTask alipay = new PayTask(PayActivity.this);
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
		sb.append("客户端支付");// 订单名字
		sb.append("\"&total_fee=\"");
		if(info.getIfxiugai().equals("1")){
			sb.append(edit_putmoney.getText().toString());// 订单价格
		}else{
			sb.append(info.getJine());// 订单价格
		}
		
		sb.append("\"&body=\"");
		sb.append("客户端支付费用");// 商品详情
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
				PayTask payTask = new PayTask(PayActivity.this);
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
		PayTask payTask = new PayTask(PayActivity.this);
		String version = payTask.getVersion();
		Toast.makeText(PayActivity.this, version, Toast.LENGTH_SHORT).show();
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


}
