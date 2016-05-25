package com.wuliwuli.haitao.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppApplication;

import java.util.List;

public class AppUtil {
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode() {
	    try {
	    	Context cont = AppApplication.application;
	        PackageManager manager = cont.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(cont.getPackageName(), 0);
	        int version = info.versionCode;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	/**
	 * 获取版本信息
	 * @return 当前应用的版本信息
	 */
	public static String getVersionName() {
	    try {
	    	Context cont = AppApplication.application;
	        PackageManager manager = cont.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(cont.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	
	/**
	 * 获取手机安卓系统的版本
	 */
	public static String getAndroidOSVersion(){
		String phoneVersion =
				"Android " + 
				android.os.Build.VERSION.SDK;
		return phoneVersion;
	}

	/**
	 * 获取手机型号
	 */
	public static String getPhoneType(){
		String phoneType = android.os.Build.MODEL;
		return phoneType;
	}
	
	/**
	 * 获取手机设备号
	 */
	public static String getDeviceID() {
		Context context = AppApplication.getInstance();
		String deviceID;
		TelephonyManager tm =
				(TelephonyManager) context.
				getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = tm.getDeviceId();
		deviceID = (deviceID == null ? 
				android.provider.Settings.System.getString(
						context.getContentResolver(), 
						"android_id"
				) 
				: deviceID);
		return deviceID;
	}
	
	public static String getAppName(){
		return AppApplication.getInstance().getPackageManager().
		getApplicationLabel(AppApplication.
		getInstance().getApplicationInfo()).toString();
	}

	public static String getTopActivityName(){
		ActivityManager manager = (ActivityManager)AppApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE) ;
		List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;

		if(runningTaskInfos != null)
			return (runningTaskInfos.get(0).topActivity).getClassName() ;
		else
			return null ;
	}

	public static Bitmap initFace(Context context){
		Bitmap source = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_face_site);
		int size = Math.min(source.getWidth(), source.getHeight());

		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
		if (squaredBitmap != source) {
			source.recycle();
		}
		if(source.getConfig() == null)return squaredBitmap;
		Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		BitmapShader shader = new BitmapShader(squaredBitmap,
				BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setAntiAlias(true);

		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);

		squaredBitmap.recycle();
		return bitmap;
	}

	/**
	 * 检查当前网络是否可用
	 *
	 * @return
	 */

	public static boolean isNetworkAvailable(Activity activity){
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null)
		{
			return false;
		}
		else
		{
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0)
			{
				for (int i = 0; i < networkInfo.length; i++)
				{
					System.out.println(i + "===状态===" + networkInfo[i].getState());
					System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
