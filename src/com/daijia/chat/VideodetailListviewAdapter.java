package com.daijia.chat;

import java.util.ArrayList;
import java.util.List;

import com.dajia.Bean.BitmapEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.k76.wzd.R;

@SuppressLint({ "InflateParams", "ViewHolder" })
public class VideodetailListviewAdapter extends BaseAdapter {
	private int clickTemp = -1;
	private List<BitmapEntity> objects = new ArrayList<BitmapEntity>();

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater layoutInflater;

	public VideodetailListviewAdapter(Context context, List<BitmapEntity> objects) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.objects = objects;
	}

	public List<BitmapEntity> setObjects(List<BitmapEntity> objects) {
		return this.objects = objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public BitmapEntity getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 数据量过大可能出现错乱，暂时不用缓存策略
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// if (convertView == null) {
		convertView = layoutInflater.inflate(R.layout.newcluedetail_listview, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imgv = (ImageView) convertView.findViewById(R.id.imgv);
		viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
		viewHolder.size = (TextView) convertView.findViewById(R.id.video_size);
		viewHolder.video_choose = (ImageView) convertView.findViewById(R.id.video_choose);
		viewHolder.duration = (TextView) convertView.findViewById(R.id.video_time_long);
		convertView.setTag(viewHolder);
		
		// }
		initializeViews((BitmapEntity) getItem(position), (ViewHolder) convertView.getTag());
		if (clickTemp == position) {
			viewHolder.video_choose.setBackgroundResource(R.drawable.video_choose);
		} else {
			viewHolder.video_choose.setBackgroundResource(R.drawable.video_nochoose);
		}
		return convertView;
	}

	private void initializeViews(BitmapEntity object, ViewHolder holder) {
		ImageLoader.getInstance().displayImage("file://" + object.getUri_thumb(), holder.imgv);
		holder.tv.setText(object.getName());
		holder.size.setText(TimeChange.bytes2kb(object.getSize()) + "");
		holder.duration.setText(TimeChange.setTime(object.getDuration()));

	}

	protected class ViewHolder {
		private ImageView imgv, video_choose;
		private TextView tv;
		private TextView size;
		private TextView duration;
	}
}
