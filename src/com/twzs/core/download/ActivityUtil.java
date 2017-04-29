package com.twzs.core.download;

import java.util.List;

import net.k76.wzd.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.util.ConfirmDialogListener;
import com.dajia.util.DialogOnPositiveListener;

/**
 * 
 * <界面工具类>
 * 
 * @author fucheng
 * @createon 2014年6月9日
 */
public class ActivityUtil {


	/**
	 * 进程对话框
	 * @param context
	 * @param title
	 * @param message
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @param listener
	 * @return
	 */
	public static ProgressBarDialog showProgressHorizontal(Context context,
			CharSequence title, CharSequence message, boolean cancelable,
			boolean canceledOnTouchOutside, OnCancelListener listener) {
		ProgressBarDialog dialog = new ProgressBarDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.setOnCancelListener(listener);
		dialog.show();
		return dialog;
	}
	
	//无进度框的
	public static ProgressBarNoPressDialog showNoProgressHorizontal(Context context,
			CharSequence title, CharSequence message, boolean cancelable,
			boolean canceledOnTouchOutside, OnCancelListener listener) {
		ProgressBarNoPressDialog dialog = new ProgressBarNoPressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.setOnCancelListener(listener);
		dialog.show();
		return dialog;
	}
	public static ProgressBarDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable, boolean canceledOnTouchOutside,
			OnCancelListener listener) {
		ProgressBarDialog dialog = new ProgressBarDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.setOnCancelListener(listener);
		dialog.show();
		return dialog;
	}

	/**
	 * 	//无进度框的进程框
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @param listener
	 * @return
	 */
	public static ProgressBarNoPressDialog showNoProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable, boolean canceledOnTouchOutside,
			OnCancelListener listener) {
		ProgressBarNoPressDialog dialog = new ProgressBarNoPressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.setOnCancelListener(listener);
		dialog.show();
		return dialog;
	}
	@SuppressWarnings("deprecation")
	public static void showConfrimDialog(Context context, int msgStrId,
			final DialogOnPositiveListener onPositiveListener) {
		new AlertDialog.Builder(context)
				.setMessage(msgStrId)
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						})
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (onPositiveListener != null) {
									onPositiveListener.onPositive(dialog);
								}
								dialog.dismiss();
							}
						}).show();
	}
}
