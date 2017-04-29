package com.dajia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.dajia.Bean.MessageBean;
import com.dajia.util.FileUtils;
import com.dajia.view.Bimp;
import com.dajia.view.XCRoundRectImageView;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class RegiterActivity extends BaseActivity {
	private TextView title,xingbie_txt;
	private LinearLayout backLayout;
	Button codeBtn, registBtn;
	private TimeCount mTime;
	private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
	private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间
	public static final String KEY_LONGIN_FLAG = "licence_show";
	public static final String SP_KEY_PHONE = "sp_phone";
	public static final String SP_KEY_PASSWORD = "sp_password";
	public static final String SP_KEY_ISCHECKED = "sp_is_checked";
	EditText phoneEdit, yanzhenmaEdit, nameEdit, passwordEdit;
	TextView sexEdit;
	private Button regist_go;
	private SharedPreferences settings;
	private String baseurl;
	private CheckBox mLicenceCheckBox;
	private static final int REQUEST_CODE = 99;
	private String select_sex;
	private XCRoundRectImageView headimg;
	private PopupWindow headimgpop;
	/**
	 * 拍照的照片存储位置
	 */

	private String mFileName;
	/**
	 * 照相机拍照得到的图片
	 */
	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private Uri photoUri;
	private float roatdp;
	private TextView headtxt;
	Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_register);
		mTime = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);

		title = (TextView) findViewById(R.id.title);
		xingbie_txt= (TextView) findViewById(R.id.xingbie_txt);
		xingbie_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				showSexpop(v);
			}
		});
		title.setText(getString(R.string.check_and_register));
		// 返回
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		settings = getSharedPreferences("setting", 0);
		baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		roatdp = getResources().getDimension(R.dimen.introduce3_text_top);
		codeBtn = (Button) findViewById(R.id.get_valicode);

		nameEdit = (EditText) findViewById(R.id.name_ed);
		sexEdit = (TextView) findViewById(R.id.sex_ed);
		sexEdit.setFocusableInTouchMode(false);
		yanzhenmaEdit = (EditText) findViewById(R.id.vali_ed);
		passwordEdit = (EditText) findViewById(R.id.password_ed);

		headimg = (XCRoundRectImageView) findViewById(R.id.headbg_register);
		headtxt = (TextView) findViewById(R.id.headtxt);
		RelativeLayout headlayout = (RelativeLayout) findViewById(R.id.headimg_layout);
		headlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHeadimgpop(v);
				
			}
		});

		sexEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				showSexpop(v);
			}
		});
		LinearLayout sexlayout = (LinearLayout) findViewById(R.id.sex_layout);
		sexlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				showSexpop(v);
			}
		});

		mLicenceCheckBox = (CheckBox) findViewById(R.id.licence_check);
		mLicenceCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
		mLicenceCheckBox.setText(getString(R.string.licence_begin));
		mLicenceCheckBox.append(getClickableSpan());
		mLicenceCheckBox.setChecked(true);

		phoneEdit = (EditText) findViewById(R.id.phone_ed);
		phoneEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// ^1[3|4|5|8][0-9]\\d{8}$

				if (checkString(String.valueOf(arg0),
						"^1[3|4|5|7|8][0-9]\\d{8}$")) {
					codeBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		codeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mTime.start();
				yanzhen("01");
			}
		});

		regist_go = (Button) findViewById(R.id.btn_register);
		regist_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
					Toast.makeText(RegiterActivity.this, "请输入姓名",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(sexEdit.getText().toString().trim())) {
					Toast.makeText(RegiterActivity.this, "请选择性别",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())) {
					Toast.makeText(RegiterActivity.this, "请输入手机号码",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(passwordEdit.getText().toString().trim())) {
					Toast.makeText(RegiterActivity.this, "请输入密码",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(bitmap==null){
					Toast.makeText(RegiterActivity.this, "请设置您的头像",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(TextUtils.isEmpty(yanzhenmaEdit.getText().toString().trim())){
					Toast.makeText(RegiterActivity.this, "请输入验证码",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!mLicenceCheckBox.isChecked()){
					Toast.makeText(RegiterActivity.this, "请同意许可证",
							Toast.LENGTH_LONG).show();
					return;
				}
				yanzhen("02");
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void showHeadimgpop(View v) {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.headimg_popview, null);
		headimgpop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		headimgpop.setBackgroundDrawable(null);
		headimgpop.setOutsideTouchable(true);
		headimgpop.setFocusable(true);
		headimgpop.setTouchable(true);
		headimgpop.showAtLocation(v, Gravity.BOTTOM, 0, 0);

		Button btn_make_photo = (Button) view.findViewById(R.id.btn_camera);
		Button btn_take_photo = (Button) view
				.findViewById(R.id.btn_selectalbum);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancelload);

		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (headimgpop.isShowing()) {
						headimgpop.dismiss();
					}
					return true;
				}
				return false;
			}
		});

		btn_make_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openCamera();
				headimgpop.dismiss();
			}
		});

		btn_take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openAlbum();
				headimgpop.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headimgpop.dismiss();
			}
		});
	}

	/**
	 * 打开相机
	 */
	@SuppressLint("ShowToast")
	private void openCamera() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakephoto();
		} else {
			Toast.makeText(RegiterActivity.this, "没有可用的存储卡", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 打开相册
	 */
	private void openAlbum() {
		try {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(RegiterActivity.this, "没有找到照片", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 拍照获取图片
	 */
	public void doTakephoto() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	private void startPhotoZoom(Uri uri) {
		try {
			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyyMMddhhmmss");
			String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");

			}
			Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
					+ ".JPEG");
			mFileName = FileUtils.SDPATH + address + ".JPEG";
			final Intent intent = new Intent("com.android.camera.action.CROP");

			// 照片URL地址
			intent.setDataAndType(uri, "image/*");

			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			// 输出路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// 输出格式
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			// 不启用人脸识别
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", false);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case TAKE_PICTURE:// 当选择拍照时调用

			startPhotoZoom(photoUri);

			break;
		case RESULT_LOAD_IMAGE:
			if (data != null) {// 当选择从本地获取图片时
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
			    bitmap = Bimp.getLoacalBitmap(mFileName);
				bitmap = Bimp.createFramedPhoto(300, 300, bitmap,
						(int) (roatdp * 1.6f));
				headtxt.setVisibility(View.GONE);
				headimg.setImageBitmap(bitmap);
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	public void showSexpop(View v) {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.registe_sex_layout,
				null);
		@SuppressWarnings("deprecation")
		final PopupWindow pw = new PopupWindow(view,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		pw.setTouchable(true);
		pw.showAtLocation(v, Gravity.BOTTOM, 0, 0);

		final RadioButton femaleRadioButton = (RadioButton) view
				.findViewById(R.id.radioFemales);
		final RadioButton maleRadioButton = (RadioButton) view
				.findViewById(R.id.radioMales);
		// 根据ID找到RadioGroup实例
		RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroups);
		select_sex = "男";
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				if (arg1 == femaleRadioButton.getId()) {
					select_sex = "女";
				}
				if (arg1 == maleRadioButton.getId()) {
					select_sex = "男";
				}

			}
		});
		Button button = (Button) view.findViewById(R.id.btn_confirmsex);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				sexEdit.setText(select_sex);
			}
		});
	}

	// 正则表达式验证
	public static boolean checkString(String s, String regex) {
		return s.matches(regex);
	}

	/**
	 * 输入手机号验证
	 * 
	 * @param flag
	 */
	public void yanzhen(final String flag) {
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("telphone", phoneEdit.getText().toString().trim());

		if ("01".equals(flag)) {
			params.put("yanzhenma", "");
		}

		if ("02".equals(flag)) {
			if (TextUtils.isEmpty(yanzhenmaEdit.getText().toString().trim())) {
				Toast.makeText(RegiterActivity.this, "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			params.put("yanzhenma", yanzhenmaEdit.getText().toString().trim());
		}

		params.put("act", "postok");

		Log.d("jiang", baseurl + "/api/askyanzhenmaclient.php" + " params:"
				+ params);
		fp.post(baseurl + "/api/askyanzhenmaclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						if (t == null) {
							Toast.makeText(RegiterActivity.this, "服务器未返回数据",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Gson gson = new Gson();
						MessageBean msg = gson.fromJson(t.toString(),
								MessageBean.class);
						if (msg != null) {
							Toast.makeText(RegiterActivity.this, msg.getMsg(),
									Toast.LENGTH_SHORT).show();

							if ("01".equals(flag))
								return;

							String theret = msg.getRet();
							// 如果返回success，把手机号和userid放入本地
							if (theret.equals("success")) {
								SharedPreferences sp = getSharedPreferences(
										"setting", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, phoneEdit
										.getText().toString().trim());

								editor.putString("userid", msg.getUserid());
								editor.putBoolean(SP_KEY_ISCHECKED, true);
								editor.commit();
								commitregist();
							} 
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						cancelTimer();
					}
				});
	}

	private void cancelTimer() {
		mTime.cancel();
		codeBtn.setText("获取验证码");
		codeBtn.setEnabled(true);
	}

	public void commitregist() {
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		try {
			if (mFileName==null) {
				Toast.makeText(RegiterActivity.this, "请上传头像", Toast.LENGTH_LONG).show();
			}else {
				File imgfile = new File(mFileName);
				params.put("thefile", imgfile);
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("nickname", nameEdit.getText().toString());
		params.put("telphone", phoneEdit.getText().toString());
		params.put("sex", sexEdit.getText().toString());
		params.put("password", passwordEdit.getText().toString());
		params.put("act", "postok");
		fp.post(baseurl + "/api/appregisterclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						if (t == null) {
							Toast.makeText(RegiterActivity.this, "服务器未返回数据",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Gson gson = new Gson();
						MessageBean msg = gson.fromJson(t.toString(),
								MessageBean.class);
						if (msg != null) {
							if (msg.getRet().equals("success")) {
								Log.e("registsuccess", t.toString());
								SharedPreferences sp = getSharedPreferences(
										"setting", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, phoneEdit
										.getText().toString().trim());
								editor.putString(SP_KEY_PASSWORD, passwordEdit
										.getText().toString().trim());
								editor.putString("userid", msg.getUserid());
								editor.putBoolean(SP_KEY_ISCHECKED, true);
								editor.commit();
								Intent intent = new Intent(
										RegiterActivity.this,
										ChatLoginActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(RegiterActivity.this,
										msg.getRet(), Toast.LENGTH_SHORT)
										.show();
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	private SpannableString getClickableSpan() {

		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegiterActivity.this,
						LicenseActivity.class);
				intent.putExtra(KEY_LONGIN_FLAG, true);
				startActivityForResult(intent, REQUEST_CODE);
			}
		};

		SpannableString spanableInfo = new SpannableString(
				RegiterActivity.this.getString(R.string.licence_end));
		int start = 0;
		int end = spanableInfo.length();
		spanableInfo.setSpan(new Clickable(l), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			codeBtn.setText(millisUntilFinished / COUNT_DOWN_INTERVAL + "秒后重发");
			codeBtn.setEnabled(false);
		}

		@Override
		public void onFinish() {
			codeBtn.setText("获取验证码");
			codeBtn.setEnabled(true);
		}

	}

	class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}
}
