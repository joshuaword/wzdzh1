package com.dajia.view;

import net.k76.wzd.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DJProgressDialogView extends LinearLayout
{
  private String title;
  private TextView tvTitle;

  public DJProgressDialogView(Context paramContext)
  {
    this(paramContext, null);
  }

  public DJProgressDialogView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public DJProgressDialogView(String paramString, Context paramContext)
  {
    super(paramContext, null);
    this.title = paramString;
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.view_dj_progress_dialog, this, true);
    this.tvTitle = ((TextView)findViewById(R.id.tv_title));
    if (!TextUtils.isEmpty(this.title))
      this.tvTitle.setText(this.title);
  }

  public void setText(String paramString)
  {
    this.title = paramString;
    this.tvTitle.setText(paramString);
  }
}