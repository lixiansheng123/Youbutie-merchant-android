package com.yuedong.youbutie_merchant_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class SharePreferencesUtils {
	/**
	 * 封装工具类
	 * 
	 * @param fileName
	 * @param map
	 * @return
	 */
	public static boolean saveSharePreferences(Context context,
			String fileName, Map<String, Object> map) {
		boolean flag = false;
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object object = entry.getValue();
			if (object instanceof Boolean) {
				Boolean newName = (Boolean) object;
				editor.putBoolean(key, newName);
			}
			if (object instanceof Integer) {
				Integer newName = (Integer) object;
				editor.putInt(key, newName);
			}
			if (object instanceof String) {
				String newName = (String) object;
				editor.putString(key, newName);
			}
			if (object instanceof Float) {
				Float newName = (Float) object;
				editor.putFloat(key, newName);
			}
			if (object instanceof Long) {
				Long newName = (Long) object;
				editor.putFloat(key, newName);
			}
		}
		flag = editor.commit();
		return flag;
	}

	/**
	 * 从共享文件拿到数据
	 * 
	 * @param fileName
	 * @return
	 */
	public static Map<String, ?> getSharedPreferences(Context context,
			String fileName) {
		Map<String, ?> map = new HashMap<String, Object>();
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		map = sp.getAll();
		return map;

	}

	/**
	 * 刷掉指定共享文件的内容
	 * 
	 * @param context
	 * @param fileName
	 */
	public static void clearSharePreferences(Context context, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();

	}

}
