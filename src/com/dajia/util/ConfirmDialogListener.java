package com.dajia.util;

import android.content.DialogInterface;

public interface ConfirmDialogListener {
	public void onPositive(DialogInterface dialog);
	public void onNegative(DialogInterface dialog);
}
