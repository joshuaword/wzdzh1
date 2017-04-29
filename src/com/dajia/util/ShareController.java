package com.dajia.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import net.k76.wzd.R;
import com.dajia.constant.Constant;
import com.dajia.constant.UrlConstant;
import com.umeng.socialize.bean.CallbackConfig.ICallbackListener;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 所有分享平台申请key，都是已com.linkage.lejia为包名申请的，且微信用到的打包keystore也是慧驾的。
 * 
 * 
 * <分享> 控制类 使用方法： ShareController mController = new ShareController(this); mController.share("分享的标题","分享的文字",
 * "分享的链接",分享的图片,分享回调);
 * 
 * @author super小志
 * 
 */
public class ShareController {

    private UMSocialService mController;
    private String mShareTitle;// 分享的标题
    private String mShareUrl;// 分享的链接
    private String mShareText;// 分享的内容
    private UMImage mUMImgBitmap;// 分享的icon
    private Activity mContext;

    public ShareController(Activity context) {
        mContext = context;
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    }

    // 如果传空，标题默认是慧驾，内容默认是推荐大家使用慧驾APP，装了它洗车特便宜，维保更透明，还有许多实用功能，快来试试吧！，默认url是http://download.huijiacn.com/huijia
    public void share(String title, String share_text, String share_url, Bitmap bitmap, ICallbackListener listener) {// 如果都传入null，则这边自动显示（“慧驾”，“推荐大家使用慧驾APP，装了它洗车特便宜，维保更透明，还有许多实用功能，快来试试吧！
                                                                                                                     // ”，“http://download.huijiacn.com/huijia”，“慧驾的icon”，无回调）
        if (!TextUtils.isEmpty(title))
            mShareTitle = title;
        else
            mShareTitle = mContext.getResources().getString(R.string.app_name);
        if (!TextUtils.isEmpty(share_text))
            mShareText = share_text;
        else
            mShareText = mContext.getResources().getString(R.string.share_detail);
        ;
        if (!TextUtils.isEmpty(share_url))
            mShareUrl = share_url;
        else
            mShareUrl = UrlConstant.SHARE_URL;
        if (null != bitmap)
            mUMImgBitmap = new UMImage(mContext, bitmap);
        else
            mUMImgBitmap = new UMImage(mContext, R.drawable.logo);
        // 设置分享内容
        mController.setShareContent(mShareText + mShareUrl + " ");// 末尾加空格，是防止用户微博分享时，@好友时，@符号也会添加到新浪自己的url编译
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(mUMImgBitmap);
        shareTencent();

        mController.openShare(mContext, false);
        if (null != listener)
            mController.registerListener(listener);
    }

    private void shareTencent() {

        String appId = Constant.WEIXINID;
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, appId);
        wxHandler.addToSocialSDK();
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(mShareText);
        weixinContent.setTitle(mShareTitle);
        weixinContent.setShareImage(mUMImgBitmap);
        weixinContent.setTargetUrl(mShareUrl);
        mController.setShareMedia(weixinContent);
    }

}
