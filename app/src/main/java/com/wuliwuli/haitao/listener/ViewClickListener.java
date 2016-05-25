package com.wuliwuli.haitao.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.wuliwuli.haitao.TopicActivity;
import com.wuliwuli.haitao.bean.InfoBean;

/**
 * Created by mac-z on 16/4/14.
 */
public class ViewClickListener implements OnClickListener{

    String mId,mSuperId;
    Context mContext;
    Class mClass;

    public ViewClickListener(Context context,String id,Class tClass){
        this.mContext = context;
        this.mId = id;
        this.mClass = tClass;
    }

    public ViewClickListener(Context context,String id,String superId,Class tClass){
        this.mContext = context;
        this.mId = id;
        this.mSuperId = superId;
        this.mClass = tClass;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(mContext, mClass);
        intent.putExtra("id",mId);
        if(!TextUtils.isEmpty(mSuperId)){
            intent.putExtra("superId",mSuperId);
        }
        mContext.startActivity(intent);
    }
}
