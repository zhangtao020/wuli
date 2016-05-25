package com.wuliwuli.haitao.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.wuliwuli.haitao.ProductInfoActivity;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.SpecialActivity;
import com.wuliwuli.haitao.SuperReturnActivity;
import com.wuliwuli.haitao.TopicActivity;
import com.wuliwuli.haitao.bean.InfoBean;

/**
 * Created by mac-z on 16/4/14.
 */
public class HomePicListener implements OnClickListener{

    String id;
    int mType;
    int mPosition;
    Context mContext;

    public HomePicListener(Context context,String id,int type,int position){
        this.mContext = context;
        this.id = id;
        this.mType = type;
        this.mPosition = position;
    }

    @Override
    public void onClick(View v) {
        Class mClass = TopicActivity.class;
        if(mType == 1){
            mClass = SpecialActivity.class;
        }else if(mType == 3){
            mClass = SuperReturnActivity.class;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, mClass);
        intent.putExtra("id",id);
        intent.putExtra("position",mPosition);
        mContext.startActivity(intent);
    }
}
