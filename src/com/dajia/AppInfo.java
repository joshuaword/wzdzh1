package com.dajia;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.widget.Toast;

public class AppInfo
{
  public static String ACCESS_TOKEN;
  public static final String ACTION_ON_GET_LOCATION = "cn.edaijia.android.client.ON_GET_LOCATION";
  public static final int APP_KEY = 10000002;
  public static String BASE_DATA_DIR;
  public static String CHANELID;
  public static final String CURRENT_VERSION = "current_version";
  public static String DEVICE_ID;
  public static final int ERROR_GPS_FAILED = 4;
  public static final int ERROR_GPS_OFF = 3;
  public static final int ERROR_NETWORK_ERROR = 2;
  public static final int ERROR_NO_DRIVERS = 6;
  public static final int ERROR_NO_NETWORK = 1;
  public static final int ERROR_SERVICE_NOT_OPEN = 5;
  public static final String FILE_APP_CONTENT = "app_content.txt";
  public static final int FLING_MIN_DISTANCE = 100;
  public static final int FLING_MIN_VELOCITY = 200;
  public static int FloatOverlayHeight = 0;
  public static int FloatOverlayWidth = 0;
  public static final String GPS_TYPE = "baidu";
  public static final String IS_RECHARGE_NEW = "is_recharge_new";
  private static final int KEY_BASE_URL = 1;
  public static final String KEY_DRIVER_LIST = "driverlist";
  public static final String KEY_DRIVER_MAP = "driver_map";
  public static final String KEY_ERROR_TYPE = "error_type";
  public static final String KEY_INVITE_PAGE = "invite_page";
  public static final String KEY_PRICE_LIST = "pricelist";
  public static final String KEY_REQUEST_DATA = "request_data";
  public static final String KEY_RESERVATION = "reservation";
  public static final String KEY_SINA_WEIBO = "sina_weibo";
  public static final String KEY_TENCENT_WEIBO = "tencent_weibo";
  public static final String KEY_WHERE_TO_ERROR = "go_to_error";
  public static final String KEY_WHERE_TO_GO = "where_to_go";
  public static final int PAGE_SIZE = 10;
  public static final String PARAMS_AGREE_EULA = "agree_eula";
  public static final String PARAMS_DRIVER_ID = "params_driver_id";
  public static final String PARAMS_KEY_CITY = "cityname";
  public static final String PARAMS_KEY_ERROR_TYPE = "params_error_code";
  public static final String PARAMS_LAST_LATITUDE = "last_latitude";
  public static final String PARAMS_LAST_LONGITUDE = "last_longitude";
  public static final String PARAMS_LATITUDE = "params_latitude";
  public static final String PARAMS_LONGTITUDE = "params_longtitude";
  public static final String PARAMS_NEWE_APP_PATH = "new_app_file";
  public static final String PARAMS_OPEN_WIFI = "open_wifi";
  public static final String PARAMS_OPEN_WIFI_SHOW = "show_open_wifi";
  public static final String PARAMS_REQUEST_NEARBY_DRIVER = "request_nearby";
  public static final int PAY_PP_SUCCESS = 1066;
  public static final int PAY_ZHIFUBAO_SUCCESS = 102;
  public static final String PHONE_NUMBER_400 = "4006913939";
  public static final String PRE_APPINFO_EDAIJIA = "appinfo_edaijia";
  public static final String PRE_EDAIJIA = "edaijia";
  public static final String PRE_EDAIJIA_GUIDE = "guideinterface";
  public static final String PRE_LAST_LOCATION = "last_location";
  public static String PRF_KEY_FIRST;
  public static final boolean REQUEST_GZIP = false;
  public static final int REQ_POLLING_MSG = 1000;
  public static String SECRET_TOKEN;
  public static final int SERVER_BACK_CODE_BAD_PARAMS = 2;
  public static final int SERVER_BACK_CODE_BAD_TOKEN = 1;
  public static final int SERVER_BACK_CODE_ERROR = -1;
  public static final int SERVER_BACK_CODE_SUCCESS = 0;
  public static final int SERVER_BACK_CODE_TIME_ERROR = -1001;
  public static final String SHARESDK_APPKEY = "116cd6a6a40a";
  private static String SINA_WeiboKey;
  private static String SINA_WeiboRedirectUrl;
  private static String SINA_WeiboSecret;
  public static String SPLASH_FILE;
  public static final String STR_DEFAULT_CITY_NAME = "北京";
  public static final String TENCENT_WEIBO_KEY = "801336959";
  public static final String TENCENT_WEIBO_SECRET = "4d0a6f121cb2f647c0a95a48862a95c9";
  public static String TOKEN;
  public static String UDID;
  public static final String UPYUN_API_KEY = "jujKekxMy5ycKIDOpO9dUz6hYho=";
  public static final String WAP_PAGE = "http://wap.edaijia.cn/";
  public static final String WEBCHAT_APP_ID = "wxe23243cf45dc18a4";
  public static int driverOverlayHeight;
  public static int driverOverlayWidth;
  public static String sBaiduKey;
  private static String sChannel;
  private static String sNewestVersion;

  static
  {
    DEVICE_ID = "";
    SINA_WeiboKey = "1864959196";
    SINA_WeiboSecret = "c5a44c8e9f90cae911b932689f04529f";
    SINA_WeiboRedirectUrl = "http://www.edaijia.cn/v2/index.php?r=site/callback&app=sina";
    sBaiduKey = "7ccc670e05f21608cac745a7f7042ada";
    SPLASH_FILE = "eDaijia_splash.png";
    PRF_KEY_FIRST = "agree_eula";
    BASE_DATA_DIR = null;
    TOKEN = "-1";
    driverOverlayWidth = 0;
    driverOverlayHeight = 0;
    FloatOverlayHeight = 0;
    FloatOverlayWidth = 0;
    ACCESS_TOKEN = "5f9239f1c2efd8fce27897837172539b";
    SECRET_TOKEN = "e749cb5ab06d368f966079ac699a1b07c8740c44";
    System.loadLibrary("edaijia");
  }

  public static native String a(int paramInt);

  public static native String b(String paramString1, String paramString2);

  public static native String c(String paramString1, String paramString2, String paramString3, String paramString4);

  public static native String d(String paramString);

  public static String getChannel()
  {
    if (TextUtils.isEmpty(sChannel));
    for (String str = "edaijia"; ; str = sChannel)
      return str;
  }

  public static String getDeviceName()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Build.MANUFACTURER).append("-").append(Build.MODEL);
    localStringBuilder.append("(").append(getDeviceOSVersion()).append(")");
    return localStringBuilder.toString();
  }

  public static String getDeviceOSVersion()
  {
    return Build.VERSION.RELEASE;
  }

  public static String getNewestVersion()
  {
    return sNewestVersion;
  }

  public static boolean isUMengEnabled()
  {
    return false;
  }

  public static void setChannel(String paramString)
  {
    sChannel = paramString;
  }

  public static void setNewestVersion(String paramString)
  {
    sNewestVersion = paramString;
  }

  public static void showToast(Context paramContext, int paramInt)
  {
    Toast.makeText(paramContext, paramInt, 0).show();
  }

  public static void showToast(Context paramContext, String paramString)
  {
    Toast.makeText(paramContext, paramString, 0).show();
  }


}