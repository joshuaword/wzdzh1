package com.twzs.core.download;

/**
 * 常用参数类
 * @author fucheng 2014年7月31日
 */
public class Constant {

	/** 图片处理：裁剪. */
	public static final int CUTIMG = 0;

	/** 图片处理：缩放. */
	public static final int SCALEIMG = 1;

	/** 图片处理：不处理. */
	public static final int ORIGINALIMG = 2;

	/** 返回码：成功. */
	public static final int RESULRCODE_OK = 0;

	/** 返回码：失败. */
	public static final int RESULRCODE_ERROR = -1;

	/** 显示Toast. */
	public static final int SHOW_TOAST = 0;

	/** 显示进度框. */
	public static final int SHOW_PROGRESS = 1;

	/** 删除进度框. */
	public static final int REMOVE_PROGRESS = 2;

	/** 删除底部进度框. */
	public static final int REMOVE_DIALOGBOTTOM = 3;

	/** 删除中间进度框. */
	public static final int REMOVE_DIALOGCENTER = 4;

	/** 删除顶部进度框. */
	public static final int REMOVE_DIALOGTOP = 5;

	/** View的类型. */
	public static final int LISTVIEW = 1;

	/** The Constant GRIDVIEW. */
	public static final int GRIDVIEW = 1;

	/** The Constant GALLERYVIEW. */
	public static final int GALLERYVIEW = 2;

	/** The Constant RELATIVELAYOUTVIEW. */
	public static final int RELATIVELAYOUTVIEW = 3;

	/** Dialog的类型. */
	public static final int DIALOGPROGRESS = 0;

	/** The Constant DIALOGBOTTOM. */
	public static final int DIALOGBOTTOM = 1;

	/** The Constant DIALOGCENTER. */
	public static final int DIALOGCENTER = 2;

	/** The Constant DIALOGTOP. */
	public static final int DIALOGTOP = 3;

	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_FAILURE_CODE = 600;

	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_TIMEOUT_CODE = 601;

	/** 响应失败的HTTP返回码. */
	public static final int RESPONSE_TIMEOUT_CODE = 602;

	/** 未处理的HTTP返回码. */
	public static final int UNTREATED_CODE = 900;

	/** 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;

	/** 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;

	/** 用来标识请求裁剪图片后的activity */
	public static final int CAMERA_CROP_DATA = 3022;

	public static final String CONNECTEXCEPTION = "无法连接到网络";

	public static final String UNKNOWNHOSTEXCEPTION = "连接远程地址失败";

	public static final String SOCKETEXCEPTION = "网络连接出错，请重试";

	public static final String SOCKETTIMEOUTEXCEPTION = "连接超时，请重试";

	public static final String NULLPOINTEREXCEPTION = "抱歉，远程服务出错了";

	public static final String NULLMESSAGEEXCEPTION = "抱歉，程序出错了";

	public static final String CLIENTPROTOCOLEXCEPTION = "Http请求参数错误";

	/** 参数个数不够. */
	public static final String MISSINGPARAMETERS = "参数没有包含足够的值";

}
