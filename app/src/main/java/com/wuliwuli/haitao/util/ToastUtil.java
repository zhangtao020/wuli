package com.wuliwuli.haitao.util;

import android.widget.Toast;

import com.wuliwuli.haitao.base.AppApplication;

public class ToastUtil {
    private static Toast mToast ;

    public static void show(String s){
        if(mToast==null) {
            mToast = Toast.makeText(AppApplication.application, s, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(s);
        }

        mToast.show();
    }
}
