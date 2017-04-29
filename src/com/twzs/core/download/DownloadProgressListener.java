package com.twzs.core.download;

/**
 * 描述：下载线程监听.
 */
public interface DownloadProgressListener {
	
	/**
	 * On download size.
	 * @param size the size
	 */
	public void onDownloadSize(long size);
}


