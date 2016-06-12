package com.wuliwuli.haitao.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.Assert;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.wuliwuli.haitao.base.AppApplication;

public class Util {
	
	private static final String TAG = "SDK_Sample.Util";
	private static final int MAX_LENGTH = 40;

	private static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int length = baos.toByteArray().length/1024;
		if (length < MAX_LENGTH) {
			return image;
		} else {
			// 最大为4
			int sampleSize = 1;
			while (length >= MAX_LENGTH) {
				if (sampleSize > 3) {
					break;
				}
				sampleSize += 1;
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
				BitmapFactory.Options newOpts = new BitmapFactory.Options();
				//开始读入图片，此时把options.inJustDecodeBounds 设回true了
				newOpts.inJustDecodeBounds = true;
				newOpts.inSampleSize = sampleSize;  //设置缩放比例
				newOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;//降低图片从ARGB888到RGB565
				//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
				isBm = new ByteArrayInputStream(baos.toByteArray());
				newOpts.inJustDecodeBounds = false;
				image = BitmapFactory.decodeStream(isBm, null, newOpts);
				baos.reset();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				length = baos.toByteArray().length/1024;
			}// end of while

			if (length > MAX_LENGTH) {
				image = compressImage(image);
			} else {
				return image;
			}
		}
		return image;//压缩好比例大小后再进行质量压缩
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>MAX_LENGTH && options>30) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		Bitmap bitmap = comp(bmp);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bitmap.recycle();
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("zt","bmpToByteArray===="+result.length);
		return result;
	}
	
	public static byte[] getHtmlByteArray(final String url) {
		 URL htmlUrl = null;     
		 InputStream inStream = null;     
		 try {         
			 htmlUrl = new URL(url);         
			 URLConnection connection = htmlUrl.openConnection();         
			 HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			 int responseCode = httpConnection.getResponseCode();         
			 if(responseCode == HttpURLConnection.HTTP_OK){             
				 inStream = httpConnection.getInputStream();         
			  }     
			 } catch (MalformedURLException e) {               
				 e.printStackTrace();     
			 } catch (IOException e) {              
				e.printStackTrace();    
		  } 
		byte[] data = inputStreamToByte(inStream);

		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

		if(offset <0){
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if(len <=0 ){
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if(offset + len > (int) file.length()){
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // ���������ļ���С������
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}
	
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}


	private static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) AppApplication.application.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(msg.what == 10){
				imm.toggleSoftInputFromWindow((IBinder)msg.obj, 0, InputMethodManager.HIDE_NOT_ALWAYS);
			}else if(msg.what == 11){
//				imm.hideSoftInputFromInputMethod((IBinder)msg.obj, InputMethodManager.HIDE_NOT_ALWAYS);
				imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0);
			}else if(msg.what == 12){
				imm.showSoftInputFromInputMethod((IBinder) msg.obj, InputMethodManager.SHOW_IMPLICIT);
			}else if(msg.what == 13){
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			}
		}
	};

	public static   void toggleSoftKeyobard(IBinder iBinder) {
		handler.sendMessageDelayed(handler.obtainMessage(10, iBinder), 200);
	}

	public static  void closeSoftKeyboard(IBinder iBinder){
		handler.sendMessageDelayed(handler.obtainMessage(11, iBinder), 200);
	}

	public static  void openSoftKeyboard(IBinder iBinder){
		handler.sendMessageDelayed(handler.obtainMessage(12, iBinder), 200);
	}

	public static   void toggleSoftKeyobard() {
		handler.sendMessageDelayed(handler.obtainMessage(13), 200);
	}

	public static void openKeyboard(final Context context,final View view){

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context. INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(view, 0);
			}
		},200);
	}

	public static void closeKeyboard(Context context,IBinder iBinder){
		((InputMethodManager) context
				.getSystemService(Context. INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(iBinder,InputMethodManager. HIDE_NOT_ALWAYS);
	}
}
