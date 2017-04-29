package com.dajia.util;

import com.dajia.VehicleApp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/****
 */
public class Preference {

	private static SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(VehicleApp.getInstance());

	public static void putString(String key, String value) {
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void putDouble(String key, double value) {
		Editor editor = pref.edit();
		editor.putString(key, String.valueOf(value));
		editor.commit();
	}

	public static void putInt(String key, int value) {
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getInt(String key) {
		return pref.getInt(key, 0);
	}

	public static int getInt(String key, int default_value) {
		return pref.getInt(key, default_value);
	}

	public static String getString(String key) {
		return pref.getString(key, "");
	}

	public static String getString(String key, String default_value) {
		return pref.getString(key, default_value);
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		return pref.getBoolean(key, defValue);
	}

}
