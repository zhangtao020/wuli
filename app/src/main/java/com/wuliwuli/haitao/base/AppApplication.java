package com.wuliwuli.haitao.base;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.wuliwuli.haitao.util.WuliConfig;

import cn.jpush.android.api.JPushInterface;

public class AppApplication extends Application {

	public static AppApplication application;

	/**
	 * 屏幕的宽度
	 */
	public static int DISPLAY_WIDTH = 720;
	/**
	 * 屏幕的高度
	 */
	public static int DISPLAY_HEIGHT = 1280;
	/**
	 * 手机型号
	 */
	public static String PHONE_MODEL = "";
	/**
	 * 系统版本
	 */
	public static String PHONE_ANDROID_RELEASE = "";

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;

		WuliConfig.context = getApplicationContext();

		getDisplayWidth();
		getDiviceInfo();
		JPushInterface.init(this);
	}

	private void showInfo() {
		System.out.println("------------------------info-----------------------------");
		System.out.println("PHONE_MODEL=" + PHONE_MODEL);
		System.out.println("PHONE_ANDROID_RELEASE=" + PHONE_ANDROID_RELEASE);
		System.out.println("DISPLAY_WIDTH=" + DISPLAY_WIDTH);
		System.out.println("DISPLAY_HEIGHT=" + DISPLAY_HEIGHT);
		System.out.println("------------------------info-----------------------------");
	}

	/**
	 * 获取手机型号和版本
	 */
	private void getDiviceInfo() {
		PHONE_MODEL = android.os.Build.MODEL;
		PHONE_ANDROID_RELEASE = android.os.Build.VERSION.SDK + ","
				+ android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取屏幕的宽高
	 */
	private void getDisplayWidth() {
		WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = windowManager.getDefaultDisplay();
		DISPLAY_WIDTH = defaultDisplay.getWidth();
		DISPLAY_HEIGHT = defaultDisplay.getHeight();

//		// 计算宽度比例系数
//		PROPORTION_WIDTH = (double)DISPLAY_WIDTH / 750;
//		// 计算高度比例系数
//		PROPORTION_HEIGHT = (double)DISPLAY_HEIGHT / 1334;

	}

	public static AppApplication getInstance(){
		return application;
	}
}
