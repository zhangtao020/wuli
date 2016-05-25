package com.wuliwuli.haitao.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {

	public static <T> T jsonObjToJava(String jsonStr, Class<T> clazz) {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		T retObj;
		try {
			retObj = (T)gson.fromJson(jsonStr, clazz);
			return retObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T jsonObjToJavaUnException(String jsonStr, Class<T> clazz) {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		T retObj;
		retObj = (T)gson.fromJson(jsonStr, clazz);
		return retObj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T jsonObjToJava(String jsonStr, Type type) {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		T retObj;
		try {
			retObj = (T)gson.fromJson(jsonStr, type);
			return retObj;
		} catch (Exception e) {
			return null;
		}
		
	}

	public static <T> List<T> jsonArrayToJava(String jsonStr, Type type) {
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			List<T> retList = gson.fromJson(jsonStr, type);
			return retList;
		} catch (Exception e) {
			return null;
		}
	}

	public static String javaToJson(Object obj, Type type) {
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			String jsonStr = gson.toJson(obj, type);
			return jsonStr;
		} catch (Exception e) {
			return null;
		}
	}

	public static String javaToJson(Object obj) {
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			String jsonStr = gson.toJson(obj);
			return jsonStr;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String listTojson(List<?> list, Type type) {
		try {
			StringBuilder json = new StringBuilder();
			json.append("[");
			if (list != null && list.size() > 0) {
				for (Object obj : list) {
					json.append(javaToJson(obj, type));
					json.append(",");
				}
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append("]");
			}
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		try {
			JSONArray ja = new JSONArray(json);
			List<T> retList = new ArrayList<T>();
			for(int i = 0; i < ja.length(); i++){
				retList.add(jsonObjToJava(ja.get(i).toString(), clazz));
			}
			return retList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> jsonToListUnException(String json, Class<T> clazz) throws Exception {
		JSONArray ja = new JSONArray(json);
		List<T> retList = new ArrayList<T>();
		for(int i = 0; i < ja.length(); i++){
			retList.add(jsonObjToJavaUnException(ja.get(i).toString(), clazz));
		}
		return retList;
	}

	public static String putKeyValeToJson(String key, String value, String json){
		try {
			if(!TextUtils.isEmpty(json)) {
				JSONObject object = new JSONObject(json);
				object.put(key, value);
				return object.toString();
			}else{
				JsonObject object = new JsonObject();
				object.addProperty(key, value);
				return object.toString();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}

	public static String retmoveKey(String key, String json){
		try {
			if(!TextUtils.isEmpty(json)) {
				JSONObject object = new JSONObject(json);
				object.remove(key);
				return object.toString();
			}
			return json;
		}catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}

	public static String mergerToJson(String json1, String json2){
		if(TextUtils.isEmpty(json1) && TextUtils.isEmpty(json2)){
			return "";
		}else if(TextUtils.isEmpty(json1)){
			return json2;
		}else if(TextUtils.isEmpty(json2)){
			return json1;
		}else {
			try {
				JSONObject object = new JSONObject(json1);
				JSONObject object1 = new JSONObject(json2);
				Iterator<String> it = object1.keys();
				while (it.hasNext()){
					String key = it.next();
					object.put(key, object1.get(key));
				}
				return object.toString();
			}catch (Exception e){
				e.printStackTrace();
			}
			return "";
		}
	}

	public  static Object getValue(String json, String key){
		try {
			JSONObject object = new JSONObject(json);
			if(object.has(key)){
				return object.get(key);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
