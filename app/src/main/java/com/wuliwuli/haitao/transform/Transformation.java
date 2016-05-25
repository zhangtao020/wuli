package com.wuliwuli.haitao.transform;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Created by mac-z on 16/4/14.
 */
public class Transformation implements com.squareup.picasso.Transformation {

    int mScreenWidth;

    Handler mHandler;

    public Transformation(int width) {
        this.mScreenWidth = width;
    }

    public Transformation(int width,Handler handler) {
        this.mScreenWidth = width;
        this.mHandler = handler;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = mScreenWidth;
        Log.d("zt","transform==========");
        try {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            if(mHandler!=null){
                Message message =mHandler.obtainMessage();
                message.obj = targetHeight;
                mHandler.sendMessage(message);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}
