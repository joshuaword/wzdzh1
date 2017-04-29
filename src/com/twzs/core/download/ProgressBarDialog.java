package com.twzs.core.download;

/**
 * fucheng
 */
import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.k76.wzd.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ProgressBarDialog extends AlertDialog {
	/**
	 * Creates a ProgressDialog with a ciruclar, spinning progress bar. This is
	 * the default.
	 */
	public static final int STYLE_SPINNER = 0;

	/**
	 * Creates a ProgressDialog with a horizontal progress bar.
	 */
	public static final int STYLE_HORIZONTAL = 1;
	private int mProgressStyle = STYLE_SPINNER;
	private SpringProgressView mProgress;
	private TextView mProgressNumber;
	private TextView mProgressPercent;

	public static final int M = 1024 * 1024;
	public static final int K = 1024;

	private double dMax;
	private double dProgress;

	private int middle = K;

	private int prev = 0;

	private Handler mViewUpdateHandler;
	private static final NumberFormat nf = NumberFormat.getPercentInstance();
	private static final DecimalFormat df = new DecimalFormat("###.##");

	public ProgressBarDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		if (mProgressStyle == STYLE_HORIZONTAL) {
			// mViewUpdateHandler = new Handler() {
			//
			// @Override
			// public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			//
			// double precent = dProgress / dMax;
			// if (prev != (int) (precent * 100)) {
			// mProgress.setProgress((int) (precent * 100));
			// mProgressNumber.setText(df.format(dProgress) + "/" +
			// df.format(dMax) + (middle == K ? "K" : "M"));
			// mProgressPercent.setText(nf.format(precent));
			// prev = (int) (precent * 100);
			// }
			//
			// }
			// };

			View view = inflater.inflate(R.layout.app_alert_dialog_progress,
					null);
			mProgress = (SpringProgressView) view.findViewById(R.id.progress);
			mProgress.setMaxCount(100);
			mProgress.setBackgroundDrawable(null);
			mProgressNumber = (TextView) view
					.findViewById(R.id.progress_number);
			mProgressPercent = (TextView) view
					.findViewById(R.id.progress_percent);
			setView(view);
		} else {
			View view = inflater.inflate(R.layout.app_progress_dialog, null);
			mProgress = (SpringProgressView) view.findViewById(R.id.progress);
			mProgress.setMaxCount(100);
			mProgressNumber = (TextView) view
					.findViewById(R.id.progress_number);
			mProgressPercent = (TextView) view
					.findViewById(R.id.progress_percent);
			setView(view);
		}

		onProgressChanged();
		super.onCreate(savedInstanceState);
	}

	private void onProgressChanged() {
		// mViewUpdateHandler.sendEmptyMessage(0);
		double precent = dProgress / dMax;
		if (prev != (int) (precent * 100)) {
			mProgress.setCurrentCount((int) (precent * 100));
			mProgressNumber.setText(df.format(dProgress)
					+ (middle == K ? "KB" : "MB") + "/" + df.format(dMax)
					+ (middle == K ? "KB" : "MB"));
			mProgressPercent.setText(nf.format(precent));
			prev = (int) (precent * 100);
		}
	}

	public double getDMax() {
		return dMax;
	}

	public void setDMax(double max) {
		if (max > M) {
			middle = M;
		} else {
			middle = K;
		}

		dMax = max / middle;
	}

	public double getDProgress() {
		return dProgress;
	}

	public void setDProgress(double progress) {
		dProgress = progress / middle;
		onProgressChanged();
	}

	public void setProgressStyle(int style) {
		mProgressStyle = style;
	}

}