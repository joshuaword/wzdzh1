package com.dajia.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.k76.wzd.R;

public class ConfirmOrderView extends RelativeLayout
  implements View.OnClickListener
{
  private TextView addressTextView;
  private Button cancelOrderBtn;
  private Button confirmOrderBtn;
  private TextView driverInfoView;
  protected Handler mHandler = new Handler();
  private TimerTask task;
  private TextView telephoneView;
  private int timeCount = 5;
  private Timer timer;
  private TextView typeNameTextView;

  public ConfirmOrderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViews(paramContext);
  }

  private void cancelOrder()
  {
    
  }

  private void confirmOrder()
  {
     stopTimer();
  }

  private void initViews(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.view_confirm_order, this);
    this.typeNameTextView = ((TextView)findViewById(R.id.confirmOrderInfo_name));
    this.driverInfoView = ((TextView)findViewById(R.id.confirmOrderInfo_driver));
    this.telephoneView = ((TextView)findViewById(R.id.confirmOrderInfo_telephone));
    this.addressTextView = ((TextView)findViewById(R.id.confirmOrderInfo_address));
    this.confirmOrderBtn = ((Button)findViewById(R.id.confirmOrderInfo_btn_confirm));
    this.confirmOrderBtn.setOnClickListener(this);
    this.cancelOrderBtn = ((Button)findViewById(R.id.confirmOrderInfo_btn_cancel));
    this.cancelOrderBtn.setOnClickListener(this);
  }

  private void startTimer()
  {
    stopTimer();
    this.timer = new Timer("ConfirmOrderView");
    this.task = new TimerTask()
    {
      public void run()
      {
        ConfirmOrderView.this.updateTime();
      }
    };
    this.timer.schedule(this.task, 1000L);
  }

  private void stopTimer()
  {
    if (this.timer != null)
    {
      this.timer.cancel();
      this.timer = null;
    }
  }

  private void updateTime()
  {
    this.mHandler.post(new Runnable()
    {
      public void run()
      {
        if (ConfirmOrderView.this.timeCount == 0)
        {
          ConfirmOrderView.this.confirmOrderBtn.setText("确认订单");
        }
          ConfirmOrderView.this.confirmOrderBtn.setText("确认订单（" + ConfirmOrderView.this.timeCount + "）");
          ConfirmOrderView.this.startTimer();
      }
    });
  }

  public void cancel()
  {
    cancelOrder();
  }

  public void onClick(View paramView)
  {
    
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

}