package com.dajia.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import net.k76.wzd.R;
import com.dajia.view.DJProgressDialogView;

public class ProgressDialogUtil
{
  private Context context;
  private Dialog dialog;
  private DJProgressDialogView dialogView;

  public ProgressDialogUtil(Context paramContext)
  {
    this.context = paramContext;
    this.dialogView = new DJProgressDialogView(paramContext);
    this.dialog = new Dialog(paramContext, R.style.loading_dialog);
    this.dialog.setCancelable(true);
    this.dialog.setCanceledOnTouchOutside(false);
    this.dialog.setContentView(this.dialogView);
  }

  public void hide()
  {
    try
    {
      this.dialog.dismiss();
      this.dialog.setCancelable(true);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void show()
  {
    show(null);
  }

  public void show(String paramString)
  {
    show(paramString, true);
  }

  public void show(String paramString, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = this.context.getResources().getString(2131099711);
    this.dialog.setCancelable(paramBoolean);
    this.dialogView.setText(paramString);
    if (this.dialog.isShowing());
    while (true)
    {
        this.dialog.show();
      return;
    }
  }

}