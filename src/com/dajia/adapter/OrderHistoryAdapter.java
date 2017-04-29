package com.dajia.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import net.k76.wzd.R;

import com.dajia.Bean.HistoryOrderInfo;
import com.dajia.activity.ConfirmOrderActivity;
import com.dajia.activity.HistoryOrderDetail;
import com.dajia.activity.MainActivity;
import com.dajia.activity.PinglunActivity;
import com.dajia.activity.YouhuiAcitivity;

/**
 * 我的订单 adapter
 * 
 * @author Administrator
 * 
 */
public class OrderHistoryAdapter extends BaseAdapter {
	private Boolean isEditStyle = false;
	List<HistoryOrderInfo> list;// 数据源
	LayoutInflater mLayoutInflater;
	private Context context;

	/**
	 * 单击事件监听器
	 */
	/*
	 * private onDelItemClickListener mDelListener = null;
	 * 
	 * private onNoDelItemClickListener mNoDelListener = null;
	 */

	public OrderHistoryAdapter(Context paramContext,
			List<HistoryOrderInfo> paramList) {
		this.mLayoutInflater = LayoutInflater.from(paramContext);
		this.list = paramList;
		context = paramContext;
	}

	/*
	 * public void setIsEditStyle(Boolean isEditStyle) { this.isEditStyle =
	 * isEditStyle; }
	 */

	public int getCount() {
		return this.list.size();
	}

	/*
	 * public Boolean getIsEditStyle() { return this.isEditStyle; }
	 */

	public Object getItem(int paramInt) {
		return this.list.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	@SuppressLint("ResourceAsColor")
	public View getView(final int paramInt, View paramView,
			ViewGroup paramViewGroup) {

		final HistoryOrderInfo localHistoryOrderInfo = (HistoryOrderInfo) this.list
				.get(paramInt);
		final ViewHolder localViewHolder;

		if (paramView == null) {
			paramView = this.mLayoutInflater.inflate(
					R.layout.item_order_history, null);
			localViewHolder = new ViewHolder();
			// 控件

			localViewHolder.tv_dingdanhao = ((TextView) paramView
					.findViewById(R.id.tv_dingdanhao));
			localViewHolder.tv_money = ((TextView) paramView
					.findViewById(R.id.tv_money));
			localViewHolder.tv_from = ((TextView) paramView
					.findViewById(R.id.tv_from));
			localViewHolder.tv_to = ((TextView) paramView
					.findViewById(R.id.tv_to));
			localViewHolder.tv_time = ((TextView) paramView
					.findViewById(R.id.tv_time));
			localViewHolder.bt_pingjia = ((TextView) paramView
					.findViewById(R.id.bt_pingjia));
			// localViewHolder.tv_beizhu = ((TextView)
			// paramView.findViewById(R.id.tv_beizhu));

			paramView.setTag(localViewHolder);
		} else {
			localViewHolder = (ViewHolder) paramView.getTag();
		}
		String state = localHistoryOrderInfo.getState();// 获取订单状态
		// 给控件赋值（7个需要赋值的控件）

		localViewHolder.tv_dingdanhao.setText("订单号："
				+ localHistoryOrderInfo.getDingdanhao());// 订单号
		localViewHolder.tv_from.setText(localHistoryOrderInfo.getLine3());// 出发地
		localViewHolder.tv_to.setText(localHistoryOrderInfo.getLine4());// 目的地
		// localViewHolder.tv_beizhu.setText("备注：");//备注
		localViewHolder.tv_time.setText("时间："
				+ localHistoryOrderInfo.getLine1());// 时间

		// 订单状态（是否评论）
		/*
		 * if ("yes".equals(localHistoryOrderInfo.getIfpingjia())) {
		 * localViewHolder.bt_pingjia.setText("已评论");
		 * localViewHolder.bt_pingjia.
		 * setTextColor(context.getResources().getColor(R.color.pinglun_gray));
		 * localViewHolder.bt_pingjia.setBackgroundResource(R.color.whit);
		 * }else{ localViewHolder.bt_pingjia.setText("评论");
		 * localViewHolder.bt_pingjia
		 * .setBackgroundResource(R.drawable.bull_frame); }
		 */

		localViewHolder.tv_money.setText("已完成");
		localViewHolder.tv_money
				.setBackgroundResource(R.drawable.blue_ordermoneybg);
		localViewHolder.bt_pingjia.setBackgroundResource(R.drawable.bull_frame);
		// 未确认
		if ("未确认".equals(localHistoryOrderInfo.getState())) {
			localViewHolder.bt_pingjia.setText("确认完成，同意支付");
			localViewHolder.bt_pingjia.setTextColor(context.getResources()
					.getColor(R.color.whit));
		}
		// 未评论
		if ("未评论".equals(localHistoryOrderInfo.getState())) {
			localViewHolder.bt_pingjia.setText("评论");
			localViewHolder.bt_pingjia.setTextColor(context.getResources()
					.getColor(R.color.whit));
		}
		// 已评论
		if ("已评论".equals(localHistoryOrderInfo.getState())) {
			localViewHolder.bt_pingjia.setText("已评论");
			localViewHolder.bt_pingjia.setTextColor(context.getResources()
					.getColor(R.color.pinglun_gray));
			localViewHolder.bt_pingjia.setBackgroundResource(R.color.whit);
			localViewHolder.bt_pingjia.setClickable(false);
		}
		// 执行中
		if ("执行中".equals(localHistoryOrderInfo.getState())) {
			localViewHolder.tv_money.setText(localHistoryOrderInfo
					.getXiangqingjien());
			localViewHolder.tv_money
					.setBackgroundResource(R.drawable.red_ordermoneybg);
			localViewHolder.bt_pingjia.setText("执行中，点击查看");
			localViewHolder.bt_pingjia.setTextColor(context.getResources()
					.getColor(R.color.whit));
			localViewHolder.bt_pingjia
					.setBackgroundResource(R.drawable.bull_frame);
		}
		/*localViewHolder.bt_pingjia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 执行中 跳转到 地图界面
				if ("执行中".equals(localHistoryOrderInfo.getState())) {
					intent.putExtra("info", localHistoryOrderInfo);
					intent.setClass(context, MainActivity.class);
				}
				// 未确认 跳转到 确认界面
				if ("未确认".equals(localHistoryOrderInfo.getState())) {
					intent.putExtra("info", localHistoryOrderInfo);
					intent.setClass(context, ConfirmOrderActivity.class);
				}
				// 未评价 跳转到 评价界面
				if ("未评论".equals(localHistoryOrderInfo.getState())) {
					intent.setClass(context, PinglunActivity.class);
				}
				// 已评价 不能点击
				if ("已评论".equals(localHistoryOrderInfo.getState())) {
					return;
				}
				context.startActivity(intent);
			}
		});*/

		/*
		 * paramView.setOnClickListener(new OnClickListener() { //
		 * localViewHolder.deleteRadioButton.isChecked()
		 * 
		 * @Override public void onClick(View v) { if (isEditStyle) { if (true)
		 * { if (mNoDelListener != null) { mNoDelListener.onDelItemClick(v,
		 * paramInt); } }else { if (mDelListener != null) {
		 * mDelListener.onDelItemClick(v, paramInt); } }
		 * 
		 * }else { Intent intent=new Intent(); //执行中 跳转到 地图界面
		 * if("zixingzhong".equals(localHistoryOrderInfo.getState())){
		 * intent.setClass(context, MainActivity.class); } //未确认 跳转到 确认界面
		 * if("weiqueren".equals(localHistoryOrderInfo.getState())){
		 * intent.setClass(context, MainActivity.class); } //未评价 跳转到 评价界面
		 * if("weipingjia".equals(localHistoryOrderInfo.getState())){
		 * intent.setClass(context, HistoryOrderDetail.class); } //已评价 不能点击
		 * if("yipingjia".equals(localHistoryOrderInfo.getState())){
		 * 
		 * } context.startActivity(intent);
		 * 
		 * 
		 * //跳转到详情页面 Intent intent = new Intent(context,
		 * HistoryOrderDetail.class); intent.putExtra("info",
		 * localHistoryOrderInfo); context.startActivity(intent); } } });
		 */

		return paramView;
	}

	/*
	 * public void setmDelListener(onDelItemClickListener mDelListener) {
	 * this.mDelListener = mDelListener; }
	 * 
	 * public void setmNoDelListener(onNoDelItemClickListener mNoDelListener) {
	 * this.mNoDelListener = mNoDelListener; }
	 * 
	 * public interface onDelItemClickListener { void onDelItemClick(View v, int
	 * position); }
	 * 
	 * public interface onNoDelItemClickListener { void onDelItemClick(View v,
	 * int position); }
	 */

	static class ViewHolder {
		// 钱，时间，订单号，出发地，目的地，备注
		TextView tv_money, tv_time, tv_dingdanhao, tv_from, tv_to, tv_beizhu;
		// 评价
		TextView bt_pingjia;
	}
}