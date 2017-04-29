package com.dajia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * <基础Fragment类>
 * @author fucheng
 * 2014年11月21日
 */
public abstract class BaseFragment extends Fragment {

	protected Activity thisInstance;

	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		thisInstance = getActivity();
	}

	// 当Fragment已经跟Activity关联上的时候，这个回调被调用。Activity会作为onAttach()回调方法的参数来传递。/
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	};

	/** 创建跟Fragment关联的视图层时，调用这个回调方法。 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	/** 当Activity的onCreate()方法执行完之后，调用这个回调方法。 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/** 当跟Fragment关联的视图层正在被删除时，调用这个回调方法。 */

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/** 当从Activity中解除Fragment的关联时，调用这个回调方法。 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	//设置缓存
	public View setRootView(LayoutInflater inflater, int layoutId) {
		if (rootView == null) {
			rootView = inflater.inflate(layoutId, null);
			initView(rootView);
			initData();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	//初始化布局
	protected abstract void initView(View rootView);

	//初始化数据
	protected abstract void initData();



	/**
	 * 切换主框架下面的内容
	 * 
	 * @param layout
	 *            要替换布局区域
	 * @param fragment
	 *            替换的内容
	 */
	protected void replaceFragment(int layout, Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(layout, fragment).commit();
	}


	public boolean isCancelDialog() {
		return true;
	}

	public void onExitDialog() {
		thisInstance.finish();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}
}
