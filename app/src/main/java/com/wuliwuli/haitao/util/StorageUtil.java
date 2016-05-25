package com.wuliwuli.haitao.util;

import android.os.Environment;
import android.util.Log;


import com.wuliwuli.haitao.base.AppApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StorageUtil {
	
	public static File getCacheDir() {
		File path = AppApplication.getInstance().getExternalCacheDir();
		if(null == path)
			path = AppApplication.getInstance().getCacheDir();
		return path;
	}
	
	public static File getCacheDir(String name) {
		File path = getCacheDir();
		File dir = new File(path, name);
		if(!dir.exists())
			dir.mkdirs();
		return dir;
	}
	
	public static File getCacheFile(String name) {
		File path = getCacheDir();
		return new File(path, name);
	}
	
	public static File getFileDir() {
		File path = AppApplication.getInstance().getExternalFilesDir(null);
		if(null == path)
			path = AppApplication.getInstance().getFilesDir();
		return path;
	}
	
	public static File getFileDir(String name) {
		File path = getFileDir();
		File dir = new File(path, name);
		if(!dir.exists())
			dir.mkdirs();
		return dir;
	}
	
	public static String getFileCachePath(){
		return getCacheDir("file").getAbsolutePath();
	}
	
	public static String getImageCachePath(){
		return getCacheDir("image").getAbsolutePath();
	}
	
	public static String getDownloadCachePath(){
		return getCacheDir("download").getAbsolutePath();
	}
	
	public static boolean remove(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; null != listFiles && i < listFiles.length && remove(listFiles[i]); i++) {
			};
		}
		return dir.delete();
	}
	
	public static boolean removeCache(String name) {
		return remove(getCacheDir(name));
	}
	
	public static boolean removeAllCache() {
		return remove(getCacheDir());
	}
	
	public static boolean saveCache(String fileName, Object cache) {
		
		File file = getCacheFile(fileName);
		file.getParentFile().mkdirs();
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			Log.d(StorageUtil.class.getSimpleName(), "SAVE CACHE FILE: " + file);
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(cache);
			return true;
		}
		catch(Throwable e) {
			Log.e(StorageUtil.class.getSimpleName(), e.toString(), e);
			file.delete();
			return false;
		}
		finally {
			try {
				if(null != oos)
					oos.close();
				if(null != fos)
					fos.close();
			}
			catch(Throwable e1) {
				file.delete();
			}
		}
	}
	
	public static Object loadCache(String fileName) {
		File file = getCacheFile(fileName);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		if(file.exists()) {
			try {
				Log.d(StorageUtil.class.getSimpleName(), "LOAD CACHE FILE: " + file);
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				Serializable o = (Serializable)ois.readObject();
				return o;
			}
			catch(Throwable e) {
				Log.e(StorageUtil.class.getSimpleName(), e.toString(), e);
			}
			finally{
				try {
					if(null != ois)
						ois.close();
					if(null != fis)
						fis.close();
				}
				catch(Throwable e1) {
					
				}
			}
		}
		return null;
	}
	
	/**
	 * 判断sd卡是否可以读写
	 * 
	 * @return
	 */
	public static boolean isCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getSdCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
}
