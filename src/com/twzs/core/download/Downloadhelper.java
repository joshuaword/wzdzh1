package com.twzs.core.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 下载进程框....会根据不同的需求做出不同的下载样式
 * @author fucheng
 *
 */
public class Downloadhelper {
	
	public Context mContext = null;
	private ProgressBarDialog mProgress;
	public final int DOWNLOAD_STATUS_INPROCESS = 0;
	public final int DOWNLOAD_STATUS_SUCCEED = 1;
	public final int DOWNLOAD_STATUS_ERROR = 2;
	public final int DOWNLOAD_STATUS_CANCEL = 3;
	public final int DOWNLOAD_STATUS_INPROCESS_MAX = 4;
	public final int DOWNLOAD_NO_PROGRESS_STATUS_SUCCEED = 5;

	private int connectTimeout = 30 * 1000;
	private int readTimeout = 30 * 1000;
	private Proxy mProxy = null;

	private int maxSize;
	private int nowSize = 0;

	private boolean isHorizontalLoading = false;
	private boolean isDownloading = true;

	public Downloadhelper(Context context) {
		this.mContext = context;
		setDefaultHostnameVerifier();
	}

	public Downloadhelper(Context context, boolean ishorizontal) {
		this.mContext = context;
		this.isHorizontalLoading = ishorizontal;
		setDefaultHostnameVerifier();
	}
	/***
	 * 有提示进度条的
	 * @param downloadMessage
	 * @param fileUrl
	 * @param filePath
	 */
	public void downLoadNOPressFile(CharSequence downloadMessage,
			final String fileUrl, final String filePath) {
		if (isHorizontalLoading) {
			mProgress = ActivityUtil.showProgressHorizontal(mContext, "",
					downloadMessage, true, false, new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							Message msg = new Message();
							msg.what = DOWNLOAD_STATUS_CANCEL;
							msg.obj = filePath;
							mHandler.sendMessage(msg);
						}
					});
		} else {
			mProgress = ActivityUtil.showProgress(mContext, "", downloadMessage,
					false, true, false, new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							Message msg = new Message();
							msg.what = DOWNLOAD_STATUS_CANCEL;
							msg.obj = filePath;
							mHandler.sendMessage(msg);
						}
					});
		}
		new Thread(new Runnable() {
			public void run() {
				if (isDownloadFile(mContext, fileUrl, filePath)) {
					Message msg = new Message();
					msg.what = DOWNLOAD_NO_PROGRESS_STATUS_SUCCEED;
					msg.obj = filePath;
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = DOWNLOAD_STATUS_ERROR;
					msg.obj = filePath;
					mHandler.sendMessage(msg);
				}
			}
		}).start();

	}

	/***
	 * 有下载进度条的
	 * @param downloadMessage
	 * @param fileUrl
	 * @param filePath
	 */
	public void downLoadFile(CharSequence downloadMessage,
			final String fileUrl, final String filePath) {
		if (isHorizontalLoading) {
			mProgress = ActivityUtil.showProgressHorizontal(mContext, "",
					downloadMessage, true, false, new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							Message msg = new Message();
							msg.what = DOWNLOAD_STATUS_CANCEL;
							msg.obj = filePath;
							mHandler.sendMessage(msg);
						}
					});
		} else {
			mProgress = ActivityUtil.showProgress(mContext, "", downloadMessage,
					false, true, false, new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							Message msg = new Message();
							msg.what = DOWNLOAD_STATUS_CANCEL;
							msg.obj = filePath;
							mHandler.sendMessage(msg);
						}
					});
		}
		new Thread(new Runnable() {
			public void run() {
				if (isDownloadFile(mContext, fileUrl, filePath)) {
					Message msg = new Message();
					msg.what = DOWNLOAD_STATUS_SUCCEED;
					msg.obj = filePath;
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = DOWNLOAD_STATUS_ERROR;
					msg.obj = filePath;
					mHandler.sendMessage(msg);
				}
			}
		}).start();

	}

	/**
	 * 
	 * @param context
	 * @param strurl
	 * @param filename
	 * @return
	 */
	public boolean isDownloadFile(Context context, String strurl,
			String filename) {
		boolean bRet = false;
		try {
			bRet = urlDownloadToFile(context, strurl, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bRet;
	}

	private void detectProxy() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()
				&& ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			if (proxyHost != null) {
				final InetSocketAddress sa = new InetSocketAddress(proxyHost,
						port);
				mProxy = new Proxy(Proxy.Type.HTTP, sa);
			}
		}
	}

	private void setDefaultHostnameVerifier() {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	/**
	 * 
	 * @param strReqData
	 * @param strUrl
	 * @return
	 */
	public String SendAndWaitResponse(String strReqData, String strUrl) {
		detectProxy();

		String strResponse = null;
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("requestData", strReqData));

		HttpURLConnection httpConnect = null;
		UrlEncodedFormEntity p_entity;
		try {
			p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
			URL url = new URL(strUrl);

			if (mProxy != null) {
				httpConnect = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				httpConnect = (HttpURLConnection) url.openConnection();
			}
			httpConnect.setConnectTimeout(connectTimeout);
			httpConnect.setReadTimeout(readTimeout);
			httpConnect.setDoOutput(true);
			httpConnect.addRequestProperty("Content-type",
					"application/x-www-form-urlencoded;charset=utf-8");

			httpConnect.connect();

			OutputStream os = httpConnect.getOutputStream();
			p_entity.writeTo(os);
			os.flush();

			InputStream content = httpConnect.getInputStream();
			strResponse =convertStreamToString(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpConnect.disconnect();
		}

		return strResponse;
	}
	 public static String convertStreamToString(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                is.close();
	            }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	/**
	 * 
	 * @param context
	 * @param strurl
	 * @param path
	 * @return
	 */
	public boolean urlDownloadToFile(Context context, String strurl, String path) {
		boolean bRet = false;

		detectProxy();
		InputStream in =null;
		FileOutputStream out =null;
		try {
			URL url = new URL(strurl);
			HttpURLConnection conn = null;
			if (mProxy != null) {
				conn = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoInput(true);
			this.maxSize = conn.getContentLength();
			conn.connect();
			in= conn.getInputStream();
			File file = new File(path);
			file.createNewFile();
			out= new FileOutputStream(file);
			
			if (isHorizontalLoading) {
				Message msg = new Message();
				msg.what = DOWNLOAD_STATUS_INPROCESS_MAX;
				mHandler.sendMessage(msg);
			}
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = in.read(temp)) > 0 && isDownloading()) {
				out.write(temp, 0, i);
				if (isHorizontalLoading) {
					Message msg = new Message();
					msg.what = DOWNLOAD_STATUS_INPROCESS;
					msg.arg2 = i;
					mHandler.sendMessage(msg);
				}
			}

			bRet = isDownloading();

		} catch (IOException e) {
			e.printStackTrace();
			bRet = false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bRet;
	}

	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
				nowSize = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case DOWNLOAD_STATUS_INPROCESS_MAX:
					mProgress.setDMax(maxSize);
					break;
				case DOWNLOAD_STATUS_INPROCESS:
					nowSize += msg.arg2;
					mProgress.setDProgress(nowSize);
					break;
				case DOWNLOAD_STATUS_SUCCEED:
					closeProgress();
					String cachePath = (String) msg.obj;
					IntentUtil.openFileByType(mContext, new File(cachePath));
					break;
				case DOWNLOAD_NO_PROGRESS_STATUS_SUCCEED:
					closeProgress();
					String cachePath1 = (String) msg.obj;
//					to(mContext, "下载成功!路径为"+cachePath1);
					break;
				case DOWNLOAD_STATUS_CANCEL:
					closeProgress();
					String tepfile = (String) msg.obj;
					setDownloading(false);
					break;
				case DOWNLOAD_STATUS_ERROR:
					closeProgress();
					Toast.makeText(mContext,"下载失败",
							Toast.LENGTH_LONG).show();
					setDownloading(false);
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}

	
}
