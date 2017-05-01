package com.dajia.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.twzs.core.json.JSONArray;
import com.twzs.core.json.JSONException;
import com.twzs.core.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * 
 * 名称：SharedPreferenceUtil.java 
 * 描述：SharedPreferences工具类
 * 
 */

public class SharedPreferenceUtil {
    private static SharedPreferences sharedPreferences = null;
    
    public SharedPreferenceUtil(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public SharedPreferenceUtil(SharedPreferences mPrefs) {
		this.sharedPreferences = mPrefs;
	}
    public static void init(String fileName, Context context) {
        if (null == sharedPreferences) {
            sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
    }
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
        
    }
    
    /**
     * 
     * @param key
     * @param context
     * @return
     */
    public static List getIntList(String key, Context context) {
        List mSharedPreferenceList = getList(key, context);
        if (mSharedPreferenceList == null) {
            mSharedPreferenceList = new ArrayList();
        }
        List localList = new ArrayList();
        Iterator localIterator = mSharedPreferenceList.iterator();
        while (localIterator.hasNext()) {
            localList.add(Integer.valueOf((String)localIterator.next()));
        }
        return localList;
    }
    
    /**
     * 
     * @param key
     * @param context
     * @return
     */
    public static List getList(String key, Context context) {
        String strValue = sharedPreferences.getString(key, "");
        if ("".equals(strValue)) {
            return null;
        }
        else {
            return new ArrayList(Arrays.asList(strValue.split("#")));
        }
    }
    
    /**
     * 
     * @param key
     * @param def
     * @return
     */
    public static int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }
    
    /**
     * 
     * @param key
     * @param def
     * @return
     */
    public static long getLong(String key, long def) {
        return sharedPreferences.getLong(key, def);
    }
    
    /**
     * 
     * @param key
     * @param context
     * @return
     */
    public static String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }
    
    /**
     * 
     * @param key
     * @param context
     * @param def
     * @return
     */
    public static boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }
    
    /**
     * 
     * @param key
     * @param list
     * @param context
     */
    public static void setIntList(String key, List<Integer> list, Context context) {
        List<String> listValue = new ArrayList();
        if (list != null) {
            Iterator localIterator = list.iterator();
            while (localIterator.hasNext())
                listValue.add(String.valueOf((Integer)localIterator.next()));
        }
        setList(key, listValue, context);
    }
    
    /**
     * 
     * @param key
     * @param list
     * @param context
     */
    public static void setList(String key, List<String> list, Context context) {
        String strResult = "";
        if (list != null && list.size() > 0) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                String strValue = (String)iterator.next();
                String strTemp = "";
                if ("".equals(strValue)) {
                    strTemp = (new StringBuilder()).append(strResult).append(" ").toString();
                }
                else {
                    strTemp = (new StringBuilder()).append(strResult).append(strValue).toString();
                }
                strResult = (new StringBuilder()).append(strTemp).append("#").toString();
            }
            Editor editor = sharedPreferences.edit();
            editor.putString(key, strResult);
            editor.commit();
        }
        
    }
    
    /**
     * 
     * @param key
     * @param value
     * @param context
     * @return
     */
    public static boolean setInt(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }
    
    /**
     * 
     * @param key
     * @param value
     * @param context
     * @return
     */
    public static boolean setLong(String key, long value) {
        return sharedPreferences.edit().putLong(key, value).commit();
    }
    
    /**
     * 
     * @param key
     * @param value
     * @param context
     * @return
     */
    public static boolean setString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }
    
    /**
     * 
     * @param key
     * @param value
     * @param context
     * @return
     */
    public static boolean setBoolean(String key, boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }
    
    /**
     * 
     * @param key
     * @param context
     * @return
     */
    public static boolean remove(String key) {
        return sharedPreferences.edit().remove(key).commit();
    }
    
    /**
     * 
     * @param context
     * @return
     */
    public static boolean removeAll() {
        return sharedPreferences.edit().clear().commit();
    }
    
    /**
     * ���
     * @param type
     */
    public void clean(String key) {
        sharedPreferences.edit().remove(key).commit();
    }
    
    /**
     * @param key
     * @param t
     */
    public <T> void addListItem(String key, T t) {
        JSONArray array = getList(key);
        array.put(new JSONObject(t));
        sharedPreferences.edit().putString(key, array.toString()).commit();
        
    }
    
    /**
     * ��ȡһ��list������jsonarray�ķ�ʽ����
     * @param key
     * @return
     */
    public JSONArray getList(String key) {
        String oldInfo = sharedPreferences.getString(key, null);
        JSONArray oldInfoArray;
        if (oldInfo == null) {
            oldInfoArray = new JSONArray();
        }
        else {
            try {
                oldInfoArray = new JSONArray(oldInfo);
            }
            catch (JSONException e) {
                e.printStackTrace();
                oldInfoArray = new JSONArray();
            }
        }
        return oldInfoArray;
    }
    
    /**
     * ��ȡ��ʷ��¼������ת����Ķ���
     * @param type
     * @return
     * @throws ReflectException
     * @throws JSONException
     */
    public <T extends SharedPreferencesDTO> List<T> getListWithCast(T t, String key)
        throws ReflectException, JSONException {
        JSONArray jsonArray = getList(key);
        List<T> list = new ArrayList<T>();
        T temp;
        for (int i = 0; i < jsonArray.length(); i++) {
            temp = (T)ReflectUtil.copy(t.getClass(), jsonArray.getJSONObject(i));
            list.add(temp);
        }
        return list;
    }
    
    /**
     * ɾ��ĳһ���ղ�
     * @param houseInfo
     */
    public <T extends SharedPreferencesDTO> void removeListItem(String key, T t) {
        JSONArray infoArray = getList(key);
        int flag = -1;
        for (int i = 0; i < infoArray.length(); i++) {
            try {
                T temp = (T)ReflectUtil.copy(t.getClass(), infoArray.getJSONObject(i));
                if (t.isSame(temp)) {
                    flag = i;
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (flag != -1) {
            infoArray.remove(flag);
            sharedPreferences.edit().putString(key, infoArray.toString()).commit();
        }
        
    }
    
    /**
     * �Ƿ��Ѿ�������ʷ��¼
     * @param type
     * @param houseInfo
     * @return
     */
    public <T extends SharedPreferencesDTO> boolean hasListItem(String key, T t) {
        JSONArray infoArray = getList(key);
        boolean flag = false;
        for (int i = 0; i < infoArray.length(); i++) {
            try {
                T temp = (T)ReflectUtil.copy(t.getClass(), infoArray.getJSONObject(i));
                if (t.isSame(temp)) {
                    flag = true;
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
    
    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }
    
    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }
    
    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }
    
    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }
    
    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }
    
    public void putObject(String key, Object value) {
        sharedPreferences.edit().putString(key, new JSONObject(value).toString()).commit();
    }
    
    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }
    
    public <T> T getObject(String key, T t)
        throws SharedPreferenceException {
        try {
            return (T)ReflectUtil.copy(t.getClass(), new JSONObject(sharedPreferences.getString(key, "")));
        }
        catch (Exception e) {
            throw new SharedPreferenceException("get object occurs exception", e);
        }
    }
    
    public Object getObject(String key, Class clazz)
			throws SharedPreferenceException {
		try {
			return ReflectUtil.copy(clazz,
					new JSONObject(sharedPreferences.getString(key, "")));
		} catch (Exception e) {
			throw new SharedPreferenceException("get object occurs exception",
					e);
		}
	}
    
}
