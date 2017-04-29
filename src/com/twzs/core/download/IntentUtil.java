package com.twzs.core.download;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 
 * <一句话功能简述>
 * @author  fucheng
 * @createon 2014年6月27日
 */
public class IntentUtil {
    public static final String IMAGE_UNSPECIFIED = "image/*";
    
    /**
     * 
     * <发送短信>
     * @param mobile
     * @param content
     * @return
     */
    public static Intent getSmsIntent(String mobile, String content) {
        String smsUri = "smsto:";
        if (mobile != null) {
            smsUri += mobile;
        }
        Uri smsToUri = Uri.parse(smsUri);
        Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        if (content != null) {
            mIntent.putExtra("sms_body", content);
        }
        return mIntent;
    }
    
    /**
     * 
     * <拨打电话>
     * @param mobile
     * @param content
     * @return
     */
    public static Intent getCallIntent(String mobile) {
        
        String callUri = "tel:";
        if (mobile != null) {
            callUri += filterTel(mobile);
        }
        Uri callToUri = Uri.parse(callUri);
        Intent mIntent = new Intent(Intent.ACTION_CALL, callToUri);
        return mIntent;
    }
    
    /**
     * 
     * <通过坐标获取地图点>
     * @param lat
     * @param lng
     * @param markName
     * @return
     */
    public static Intent getMapIntent(String lat, String lng, String markName) {
        String callUri = "geo:";
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
            callUri += lat + "," + lng;
            callUri += "?q=" + markName;
        }
        Uri mapToUri = Uri.parse(callUri);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, mapToUri);
        return mIntent;
    }
    
    /**
     * 
     * <4008电话处理>
     * @param tel
     * @return
     */
    public static String filterTel(String tel) {
        if ("".equals(tel) || "暂无".equals(tel) || "待定".equals(tel)) {
            return "4008";
        }
        else {
            tel = tel.replace("转", ",");
            tel = tel.replace("-", "");
            tel = tel.replace(" ", "");
            if (tel.contains("/")) {
                String[] tels = tel.split("/");
                return tels[0];
            }
            else {
                return tel;
            }
        }
    }
    
    /**
     * 
     * <打开相机>
     * @return
     */
    public static Intent getCameraIntent() {
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        return i;
    }
    
    //打开照相机
    public static Intent getCameraIntent(File tempfile) {
        Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        shootIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempfile));
        return shootIntent;
    }
    
    //打开相册
    public static Intent getAlbumIntent() {
        Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT, null);
        intentAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        return intentAlbum;
    }
    
    // 裁剪
    public static Intent startPhotoZoom(Uri input, Uri output) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(input, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("output", output);
        return intent;
    }
    
    //裁剪传入高宽
    @Deprecated
    public static Intent startPhotoZoom(Uri uri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IntentUtil.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        return intent;
    }
    
    /**
     * 裁剪
     * <一句话功能简述>
     * @param input
     * @param output
     * @param width
     * @param height
     * @return
     */
    public static Intent startPhotoZoom(Uri input, Uri output, int width, int height) {
        return startPhotoZoom(input, output, 1, 1, width, height, Bitmap.CompressFormat.JPEG.toString());
    }
    
    public static Intent startPhotoZoom(Uri input, Uri output, int aspectX, int aspectY, int width, int height) {
        return startPhotoZoom(input, output, aspectX, aspectY, width, height, Bitmap.CompressFormat.JPEG.toString());
    }
    
    /**
     * 裁剪图片，
     * 1.通过aspectX，aspectY控制图片比例
     * 2.width，height控制图片像素
     * 3.outputFormat 控制图片输出格式
     * 4.通过width、height、outputFormat三个参数甚至可以控制图片大小，比如jpg，默认为32位，一个像素点为4个byte，比如width，height各100像素，最终大小为 100*100*4byte =0.04M
     * @param input
     * @param output
     * @param aspectX
     * @param aspectY
     * @param width
     * @param height
     * @param outputFormat
     * @return
     */
    public static Intent startPhotoZoom(Uri input, Uri output, int aspectX, int aspectY, int width, int height,
        String outputFormat) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(input, IntentUtil.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        if (aspectX > 0 && aspectY > 0) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        if (width > 0 && height > 0) {
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        intent.putExtra("output", output);
        return intent;
    }
    
  
    public static Intent getMIMEIntent(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        return intent;
    }
    
    //    打开另一个app
    public static void openApp(Context context, String packageName) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pi != null) {
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(packageName1, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        }
        else {
//            ActivityUtil.showToastView(context,  R.string.text_noapp, R.layout.toast);
        }
    }
    
    // 打开另一个app
    public static Intent getOpenAppIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }
    
    //打开一个url
    public static Intent getUriIntent(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        return intent;
    }
    
    //    调本机分享
    public static Intent getShareIntent(Context context, String shareSubject, String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
    
    public static void openFileByType(Context context, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		String type =getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}
    public static  String getMIMEType(File f) {
      String type = "";
      String fName = f.getName();
      String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
      if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
          || end.equals("wav")) {
          type = "audio";
      }
      else if (end.equals("3gp") || end.equals("mp4")) {
          type = "video";
      }
      else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
          type = "image";
      }
      else if (end.equals("apk")) {
          type = "application/vnd.android.package-archive";
      }
      else if (end.equals("doc") || end.equals("docx")) {
          type = "application/msword";
      }
      else if (end.equals("xls") || end.equals("xlsx")) {
          type = "application/vnd.ms-excel";
      }
      else if (end.equals("ppt") || end.equals("pptx")) {
          type = "application/vnd.ms-powerpoint";
      }
      else if (end.equals("pdf")) {
          type = "application/pdf";
      }
      else {
          type = "*";
      }
      if (type.indexOf("/") == -1) {
          type += "/*";
      }
      return type;
  }
  
    //跳转公用
    public static void startIntent(Activity activity,Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
	}
    
}
