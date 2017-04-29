package com.dajia.adapter;

import java.io.File;
import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dajia.Bean.ServiceProjectBean;
import com.dajia.widgets.AsyncImageView;

public class HomeGridviewAdapter extends BaseAdapter {
	private List<ServiceProjectBean> mGist;
	Context context;
	private LayoutInflater inflater;
	private final static String CACHE_DIR = getSDPath() + "/dashou/";

	public HomeGridviewAdapter(Context context, List<ServiceProjectBean> gist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mGist = gist;
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mGist == null) {
			return 0;
		} else {
			return mGist.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mGist == null) {
			return null;
		} else {
			return mGist.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.homegrid_item, null, false);
			holder = new GViewHolder();
			holder.giv = (AsyncImageView) convertView
					.findViewById(R.id.homegrid_iv);
			holder.gtv = (TextView) convertView.findViewById(R.id.homegrid_tv);
			convertView.setTag(holder);
		} else {
			holder = (GViewHolder) convertView.getTag();
		}

		holder.giv.asyncLoadBitmapFromUrl(
				mGist.get(position).getIcon(),
				CACHE_DIR + context.getPackageName() + "/menuimg/"
						+ mGist.get(position).getName() + ".jpg");
		holder.gtv.setText(mGist.get(position).getName());
		return convertView;
	}

	public static class GViewHolder {

		AsyncImageView giv;
		TextView gtv;
	}
}
