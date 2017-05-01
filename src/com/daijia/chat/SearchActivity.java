package com.daijia.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dajia.VehicleApp;
import com.dajia.Bean.HotSearchInfo;
import com.dajia.Bean.KeyItemArray;
import com.dajia.activity.SearchResultListActivity;
import com.dajia.adapter.SearchHistoryAdapter;
import com.dajia.util.ReflectException;
import com.dajia.view.NoScrollListView;
import com.dajia.view.myListView;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.twzs.core.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import net.k76.wzd.R;

public class SearchActivity extends Activity implements OnClickListener, OnPageChangeListener {
	static boolean popInputFlag = false;
	private ImageView btn_back;
	private VehicleApp itiyanAPP;
	private EditText keywords_edit;
	ImageView mVoiceInput;// 语音输入按钮
	private TextView keywords;
	private View clear_layout, history_layout;
	private String type;
	private myListView historyList;
	private List<KeyItemArray> history_data;
	private SearchHistoryAdapter search_adapter;
	// private ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();;
	ArrayList<HotSearchInfo> searchInfo = new ArrayList<HotSearchInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		// 科大讯飞初始化
		SpeechUtility.createUtility(this, SpeechConstant.APPID  +"55d3f58b");
		initView();
	}

	protected void initView() {
		history_layout = findViewById(R.id.history_layout);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		keywords = (TextView) findViewById(R.id.keywords);
		keywords_edit = (EditText) findViewById(R.id.keywords_edit);

		// 设置语音识别
		mVoiceInput = (ImageView) findViewById(R.id.search_layout_voiceinput);
		mVoiceInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				keywords_edit.getEditableText().clear();
				final RecognizerDialog iatDialog = new RecognizerDialog(SearchActivity.this, null);
				// 2.设置听写参数，同上节
				// 3.设置回调接口
				iatDialog.setListener(new RecognizerDialogListener() {

					boolean flag = false;

					@Override
					public void onResult(RecognizerResult results, boolean arg1) {
						if (flag == false) {
							String keyWord = JsonParser.parseIatResult(results.getResultString());
							flag = true;
							Intent intent = new Intent(SearchActivity.this, SearchResultListActivity.class);
							intent.putExtra("keyword", keyWord);
							keywords_edit.setText(keyWord);
							VehicleApp.getInstance()
							.addKeySearchHistroy(new KeyItemArray(keywords_edit.getText().toString()));
							startActivity(intent);
						}

					}

					@Override
					public void onError(SpeechError arg0) {
						Log.i("errorCode", arg0 + "");
					}
				});

				iatDialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {

						keywords_edit.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								InputMethodManager imm = (InputMethodManager) SearchActivity.this
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(keywords_edit, InputMethodManager.SHOW_FORCED);

							}
						}, 500);

					}
				});
				// 4.开始听写
				iatDialog.show();
			}
		});

		historyList = (myListView) findViewById(R.id.history_list);
		View footView = this.getLayoutInflater().inflate(R.layout.search_foodview, null);
		clear_layout = footView.findViewById(R.id.clear_layout);
		clear_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				VehicleApp.getInstance().cleanKeySearchHistroy();
				sethistory();
			}
		});
		historyList.addFooterView(footView);
		historyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this, SearchResultListActivity.class);
				intent.putExtra("keyword", search_adapter.getItem(position).toString());
				keywords_edit.setText(search_adapter.getItem(position).toString());
				startActivity(intent);

			}
		});
		// 返回
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 软键盘回车进入搜索列表结果页
		keywords_edit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO
						|| actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH) {
					Intent intent = new Intent(SearchActivity.this, SearchResultListActivity.class);
					intent.putExtra("keyword", keywords_edit.getText().toString());
					intent.putExtra("type", type);
					if (!isEmpty(keywords_edit.getText().toString())) {
						VehicleApp.getInstance()
								.addKeySearchHistroy(new KeyItemArray(keywords_edit.getText().toString()));
						InputMethodManager imm = (InputMethodManager) SearchActivity.this
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(keywords_edit, InputMethodManager.SHOW_FORCED);
						sethistory();
					}
					startActivity(intent);
				}
				return false;
			}
		});
		
	}
	 public static boolean isEmpty(String str) {
	        return str == null || str.trim().length() == 0;
	    }
	@Override
	protected void onResume() {
		sethistory();
		super.onResume();
	}

	// 设置历史搜索容器
	public void setHistoryAdapterData(List<String> list_data) {
		for (int i = 0; i < history_data.size(); i++) {
			list_data.add(history_data.get(i).getArray());
		}
		Collections.reverse(list_data);
		search_adapter = new SearchHistoryAdapter(this,list_data);
		historyList.setAdapter(search_adapter);
		search_adapter.notifyDataSetChanged();
	}

	// 展现标签数据
	private void sethistory() {
		try {
			history_data = VehicleApp.getInstance().getKeySearchHistory();
			List<String> list_data = new ArrayList<String>();
			if (history_data != null && history_data.size() > 0) {
				setHistoryAdapterData(list_data);
				history_layout.setVisibility(View.VISIBLE);
			} else {
				history_layout.setVisibility(View.GONE);
			}
		} catch (ReflectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void initData() {

	}

	@Override
	public void onClick(View v) {
	}

	/** {@inheritDoc} */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
	}
}
