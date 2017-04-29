package com.zms.wechatrecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class MyAudioManager {
	private MediaRecorder mediaRecorder;
	private String dir;
	private String currentFilePath;
	private static MyAudioManager audioInstance; 
	public boolean isPrepared = false;

	private MyAudioManager(String dir) {
		this.dir = dir;
	}

	public interface AudioStateChangeListener {
		void wellPrepared();
	}

	public AudioStateChangeListener audioStateChangeListener;

	public void setOnAudioStateChangeListener(AudioStateChangeListener listener) {
		audioStateChangeListener = listener;
	}

	public static MyAudioManager getInstance(String dir) {
		if (audioInstance == null) {
			synchronized (MyAudioManager.class) {
				if (audioInstance == null) {
					audioInstance = new MyAudioManager(dir);
				}
			}
		}
		return audioInstance;
	}

	public void prepareAudio() throws IllegalStateException, IOException {
			isPrepared = false;
			File fileDir = new File(dir);
			if (!fileDir.exists())
				fileDir.mkdirs();
			String fileName = generateFileName();
			File file = new File(fileDir, fileName);

			currentFilePath = file.getAbsolutePath();
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setOutputFile(file.getAbsolutePath());
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.prepare();
			mediaRecorder.start();
			isPrepared = true;
			if (audioStateChangeListener != null) {
				audioStateChangeListener.wellPrepared();
			}
		
	}

	private String generateFileName() {
		return UUID.randomUUID().toString() + ".amr";
	}

	public int getVoiceLevel(int maxLevel) {
		if (isPrepared) {
			try {
				return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
			} catch (Exception e) {
			}
		}
		return 1;
	}

	public void release() {
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;

	}

	public void cancel() {
		release();
		if (currentFilePath != null) {
			File file = new File(currentFilePath);
			file.delete();
			currentFilePath = null;
		}
	}

	public String getCurrentPath() {
		return currentFilePath;
	}
}
