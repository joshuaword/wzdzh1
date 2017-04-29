package net.k76.wzd.wxapi.constant;

/**
 * 
 * @author fucheng 2015年4月22日
 */

public class Constants {

	public static final String APP_ID = "wx10a08b885fc21e10";
	public static final String MCH_ID = "1271080601";
	public static final String API_KEY = "wxzf18955176222tcq18955176222tcq";

	/**************************************** 微信支付相关 **************************************/
	// APP_ID 你的应用从官方网站申请到的合法appId

	// 商家id
	//public static final String PARTNER_ID = "1271080601";

	public static final String APP_SECRET = "ac6b8f7d1ab6dea9c66326a44788cb85";

	//public static final String PARTNER_KEY = "wxzf18955176222tcq18955176222tcq";

	//public static final String APP_KEY = "GqT53g9UoxqoOhEUBhgrWLF98ofSNvPMe5BeU6sk6xWyX2PqdY7Xw8f3M5s6opowb60yu6buAjjdoAvhz6avDaMQkZGfRfcQo8pjY5wQhz2UTlrskuz5aWIaxld5eaYo"; // 对应的支付密钥

	public static final String WXPAY_NOTIFY_URL = "http://pay.51zouyizou.com/fms_server/weixinpay.do";

	public static final String WXPAY_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	public static final String WXPAY_PREID_URL = "https://api.weixin.qq.com/pay/genprepay?access_token=%s";

	public static final String WXPAY_SPBILL_CREATE_IP = "218.94.131.180";
}
