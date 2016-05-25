package com.wuliwuli.haitao.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.wuliwuli.haitao.base.AppApplication;


public class ScreenUtil {

	public static float[] getScreenDpi(Activity activity){
		DisplayMetrics dm = getDisplayMetrics(activity);
        int screenWidth = dm.widthPixels; 
        int screenHeight = dm.heightPixels;
        if((screenWidth*screenHeight) < 480*800) {
        	return new float[]{2.0f, 1.0f};
		}else if((screenWidth*screenHeight) >= 480*800 
				&& (screenWidth*screenHeight) < 640*960
		) {
        	return new float[]{1.5f, 1.5f};
		}else if((screenWidth*screenHeight) >= 640*960) {
        	return new float[]{1.0f, 2.0f};
		}
		return new float[]{1.5f, 1.5f}; 
	}
	
	public static DisplayMetrics getDisplayMetrics(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		return dm; 
	}
	
	public static int getScreenWidth(Activity activity){
		DisplayMetrics dm = getDisplayMetrics(activity);
        int screenWidth = dm.widthPixels; 
        return screenWidth; 
	}
	
	public static int getScreenHeight(Activity activity){
		DisplayMetrics dm = getDisplayMetrics(activity);
		int screenHeight = dm.heightPixels;
		return screenHeight; 
	}
	
	/**
	 * 
	 * @param activity
	 * @return int[0] with, int[1] height
	 */
	public static int[] getScreenSize(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		int [] size = new int[2];
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
	}
	
	// dp to px
	public static int dipToPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static int dipToPx(float dpValue){
		return dipToPx(AppApplication.application, dpValue);
	}

	// px to dp
	public static int pxToDip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
