package com.dajia.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import net.k76.wzd.R;
import com.dajia.Bean.DriverInfo;
import com.dajia.activity.DriverDetails;
import com.dajia.activity.StoreDetailsActivity;
import com.dajia.view.XCRoundRectImageView;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author Administrator
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DriverListAdapter extends BaseAdapter {
    private Context context;
    public List<DriverInfo> list;
	private String typename;

    public DriverListAdapter(Context paramContext, List<DriverInfo> list) {
        this.context = paramContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public DriverInfo getItem(int paramInt) {
        return (DriverInfo) this.list.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        final DriverInfo localDriverInfo = getItem(paramInt);
        ViewHolder localViewHolder;
        if (paramView == null) {
            paramView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(
                    R.layout.item_driver_list, paramViewGroup, false);
            localViewHolder = new ViewHolder();
            localViewHolder.img_drivericon = ((XCRoundRectImageView) paramView.findViewById(R.id.img_drivericon));
            localViewHolder.text_DriverName = ((TextView) paramView.findViewById(R.id.text_DriverName));
            localViewHolder.text_distance = ((TextView) paramView.findViewById(R.id.text_distance));
            localViewHolder.text_sign = ((TextView) paramView.findViewById(R.id.text_sign));
            localViewHolder.driver_location_ratingBar = ((RatingBar) paramView
                    .findViewById(R.id.driver_location_ratingBar));
            localViewHolder.text_price=(TextView) paramView.findViewById(R.id.text_price);
            paramView.setTag(localViewHolder);
        } else {
            localViewHolder = (ViewHolder) paramView.getTag();
        }

        localViewHolder.driver_location_ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                return true;
            }
        });
        localViewHolder.text_DriverName.setText(localDriverInfo.getShowname());
        localViewHolder.driver_location_ratingBar.setRating(Float.parseFloat(localDriverInfo.getXinji()));
        localViewHolder.text_distance.setText(localDriverInfo.getShowinfo1());
        localViewHolder.text_sign.setText(localDriverInfo.getSign());
        SharedPreferences settings = context.getSharedPreferences(
				"setting", 0);
    	typename = settings.getString("selectfuwutype", "");
        if (typename.contains("到店")||typename.contains("封釉")) {
        	localViewHolder.text_price.setVisibility(View.VISIBLE);
        	localViewHolder.text_price.setText(localDriverInfo.getPrice()+"元");
        }else {
        	localViewHolder.text_price.setVisibility(View.GONE);
		}
        Bitmap bitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_driver);
        FinalBitmap finalBitMap=FinalBitmap.create(context);
        if (!TextUtils.isEmpty(localDriverInfo.getThefile())) {
        	 finalBitMap.display(localViewHolder.img_drivericon, localDriverInfo.getThefile(),bitMap);
		}

        paramView.setOnClickListener(new View.OnClickListener() {
            private Intent localIntent;

			public void onClick(View paramAnonymousView) {
            	
            	if (typename.contains("到店")||typename.contains("封釉")) {
            		 localIntent = new Intent(DriverListAdapter.this.context, StoreDetailsActivity.class);
				}else {
					localIntent = new Intent(DriverListAdapter.this.context, DriverDetails.class);
				}
                localIntent.putExtra("info", localDriverInfo);
                ((Activity) DriverListAdapter.this.context).startActivity(localIntent);
            }
        });

        return paramView;
    }

    private static class ViewHolder {
        RatingBar driver_location_ratingBar;
        XCRoundRectImageView img_drivericon;
        TextView text_DriverName;
        TextView text_distance;
        TextView text_sign;
        TextView text_price;
    }

}