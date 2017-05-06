package com.dajia.util;

import net.k76.wzd.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Assign_UpDateDialog {
	// 派客升级自定义对话框传字符
	public static void showUpdateDialog(Context context, String title,
			String msg, String positiveButtonText, String negativeButtonText,
			final ConfirmDialogListener dialogListener) {
		WindowManager wm = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
		int screenHeight = wm.getDefaultDisplay().getHeight();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.update_dialog_style, null);
		Builder builder = new AlertDialog.Builder(context);
		final AlertDialog alertDialog = builder.create();
		TextView tvtitle, tvcontent;
		Button positiveButton, negativeButton;
		tvtitle = (TextView) view.findViewById(R.id.title);
		tvcontent = (TextView) view.findViewById(R.id.content);
		positiveButton = (Button) view.findViewById(R.id.positiveButton);
		negativeButton = (Button) view.findViewById(R.id.negativeButton);
		tvtitle.setText(title);
		tvcontent.setText(msg);
		if (!TextUtils.isEmpty(positiveButtonText)) {
			positiveButton.setText(positiveButtonText);
			positiveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
					if (dialogListener != null) {
						dialogListener.onPositive(alertDialog);
					}
				}
			});

		} else {
			positiveButton.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(negativeButtonText)) {
			negativeButton.setText(negativeButtonText);
			negativeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
					if (dialogListener != null) {
						dialogListener.onNegative(alertDialog);
					}
				}
			});
		} else {
			negativeButton.setVisibility(View.GONE);
		}
		alertDialog.show();
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
		lp.width = screenWidth - screenWidth / 4;
		alertDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		alertDialog.getWindow().setAttributes(lp);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.getWindow().setContentView(view);
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCancelable(false);
	}
	
}
