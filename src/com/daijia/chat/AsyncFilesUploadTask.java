package com.daijia.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dajia.constant.Constant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import net.tsz.afinal.core.AsyncTask;

public class AsyncFilesUploadTask extends AsyncTask<String, Integer, String> {

	private List<String> mPhotoList;
	private Map<String, String> parameter;
	private Context context;
	private String path;
	private Dialog dialog;

	protected boolean isUploadSuccess = false;

	public AsyncFilesUploadTask(Context context, String path, Map<String, String> parameter, List<String> mPhotoList,
			Dialog dialog) {
		super();
		this.mPhotoList = mPhotoList;
		this.parameter = parameter;
		this.context = context;
		this.path = path;
		this.dialog = dialog;
	}

	@Override
	protected String doInBackground(String... arg0) {
		List<Map<String, File>> fileList = new ArrayList<Map<String, File>>();
		if (mPhotoList != null && mPhotoList.size() > 0) {
			for (int i = 0; i < mPhotoList.size(); i++) {
				Map<String, File> fileMap = new HashMap<String, File>();
				File file = new File(mPhotoList.get(i));
				fileMap.put(".jpg", file);
				fileList.add(fileMap);
			}
		}
		String response = postUploadFile(path, parameter, fileList);
		return response;
	}

	@Override
	protected void onPostExecute(String r) {
		if (!TextUtils.isEmpty(r)) {
			dialog.dismiss();
			context.sendBroadcast(new Intent(Constant.RECEIVEREFRESH));
			Toast.makeText(context, "发送图片成功", Toast.LENGTH_SHORT).show();
		}

	}

	// 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
	@Override
	protected void onPreExecute() {
	}

	public static String postUploadFile(String actionUrl, Map<String, String> params, List<Map<String, File>> files) {
		StringBuffer responseBuffer = null;
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		try {
			Log.e("do post:", actionUrl);
			URL uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setReadTimeout(500 * 1000); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			// 请求时候设置COOKER
			// request.setHeader("Cookie", strCookieKey + "=" + strCookieVal);
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			InputStream in = null;
			if (files != null) {
				for (int i = 0; i < files.size(); i++) {

					for (Map.Entry<String, File> file : files.get(i).entrySet()) {
						StringBuilder sb1 = new StringBuilder();
						sb1.append(PREFIX);
						sb1.append(BOUNDARY);
						sb1.append(LINEND);

						if (i == 0) {
							sb1.append("Content-Disposition: form-data; name=\"thefile" + "\"; filename=\""
									+ file.getKey() + "\"" + LINEND);
						} else {
							sb1.append("Content-Disposition: form-data; name=\"thefile" + i + "\"; filename=\""
									+ file.getKey() + "\"" + LINEND);
						}

						sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
						sb1.append(LINEND);
						outStream.write(sb1.toString().getBytes());

						InputStream is = new FileInputStream(file.getValue());
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}

						is.close();
						outStream.write(LINEND.getBytes());
					}
				}

			}
			// // 发送文件数据
			// if (files != null) {
			// for (int i = 0; i < files.size(); i++) {
			//
			// for (Map.Entry<String, File> file : files.get(i).entrySet()) {
			// StringBuilder sb1 = new StringBuilder();
			// sb1.append(PREFIX);
			// sb1.append(BOUNDARY);
			// sb1.append(LINEND);
			// sb1.append("Content-Disposition: form-data; name=\"thefile\";
			// filename=\"" + file.getKey()
			// + "\"" + LINEND);
			// sb1.append("Content-Type: application/octet-stream; charset=" +
			// CHARSET + LINEND);
			// sb1.append(LINEND);
			// outStream.write(sb1.toString().getBytes());
			//
			// InputStream is = new FileInputStream(file.getValue());
			// byte[] buffer = new byte[1024];
			// int len = 0;
			// while ((len = is.read(buffer)) != -1) {
			// outStream.write(buffer, 0, len);
			// }
			//
			// is.close();
			// outStream.write(LINEND.getBytes());
			// }
			// }
			//
			// }
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				responseBuffer = new StringBuffer();
				String data = "";
				while ((data = br.readLine()) != null) {
					responseBuffer.append(data + "\n");
				}
			} else {
				return null;
			}
			outStream.close();
			conn.disconnect();
		} catch (Exception e) {

		}
		if (responseBuffer == null) {
			return null;
		}
		return responseBuffer.toString();

	}

}
