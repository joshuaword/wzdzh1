//package com.daijia.chat;
//
//import java.io.File;
//
//import com.baidu.navisdk.util.common.LogUtil;
//import com.dajia.constant.Constant;
//import com.dajia.util.CropImage;
//import com.dajia.util.CropImageView;
//import com.dajia.util.FileUtil;
//import com.twzs.core.download.ActivityUtil;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//import net.k76.wzd.R;
//
//
///**
// * 裁剪界面
// *
// */
//public class CropImageActivity extends Activity implements OnClickListener{
//	private static final String TAG = "CropImageActivity";
//	private CropImageView mImageView;
//	private Bitmap mBitmap;
//	private CropImage mCrop;
//	private Button mSave;
//	private Button mCancel,rotateLeft,rotateRight;
//	private String mPath = "CropImageActivity";
//	public int screenWidth = 0;
//	public int screenHeight = 0;
//
//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				break;
//			}
//		}
//	};
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crop_image);
//        init();
//    }
//    @Override
//    protected void onStop(){
//    	super.onStop();
//    	if(mBitmap!=null){
//    		mBitmap=null;
//    	}
//    }
//    
//    private void init(){
//    	getWindowWH();
//    	mPath = getIntent().getStringExtra("PATH");
//        mImageView = (CropImageView) findViewById(R.id.crop_image);
//        mSave = (Button) this.findViewById(R.id.okBtn);
//        mCancel = (Button) this.findViewById(R.id.cancelBtn);
//        rotateLeft = (Button) this.findViewById(R.id.rotateLeft);
//        rotateRight = (Button) this.findViewById(R.id.rotateRight);
//        mSave.setOnClickListener(this);
//        mCancel.setOnClickListener(this);
//        rotateLeft.setOnClickListener(this);
//        rotateRight.setOnClickListener(this);
//        //相册中原来的图片
//        File mFile = new File(mPath);
//        try{
//        	mBitmap = FileUtil.getBitmapFromSD(mFile,Constant.SCALEIMG,800,800);
//            if(mBitmap==null){
//            	Toast.makeText(this, "没有找到图片", Toast.LENGTH_SHORT).show();
//    			finish();
//            }else{
//            	resetImageView(mBitmap);
//            }
//        }catch (Exception e) {
//           	Toast.makeText(this, "没有找到图片", Toast.LENGTH_SHORT).show();
//			finish();
//		}
//    }
//    /**
//     * 获取屏幕的高和宽
//     */
//    private void getWindowWH(){
//		DisplayMetrics dm=new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		screenWidth = dm.widthPixels;
//		screenHeight = dm.heightPixels;
//	}
//    private void resetImageView(Bitmap b){
//    	 mImageView.clear();
//    	 mImageView.setImageBitmap(b);
//         mImageView.setImageBitmapResetBase(b, true);
//         mCrop = new CropImage(this, mImageView,mHandler);
//         mCrop.crop(b);
//    }
//    
//    public void onClick(View v){
//    	switch (v.getId()){
//    	case R.id.cancelBtn:
//    		finish();
//    		break;
//    	case R.id.okBtn:
//    		String path = mCrop.saveToLocal(mCrop.cropAndSave());
//    		Intent intent = new Intent();
//    		intent.putExtra("PATH", path);
//    		setResult(RESULT_OK, intent);
//    		finish();
//    		break;
//    	case R.id.rotateLeft:
//    		mCrop.startRotate(270.f);
//    		break;
//    	case R.id.rotateRight:
//    		mCrop.startRotate(90.f);
//    		break;
//    		
//    	}
//    }
//   
//}
