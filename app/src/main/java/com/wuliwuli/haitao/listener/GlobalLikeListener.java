package com.wuliwuli.haitao.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.LoginActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppBaseListener;
import com.wuliwuli.haitao.bean.PGlobalInfoBean;
import com.wuliwuli.haitao.bean.PTopicBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/15.
 */
public class GlobalLikeListener extends AppBaseListener{

    ImageView likeTv;
    PGlobalInfoBean.GlobalBean productBean;

    public GlobalLikeListener(Context context, ImageView like, PGlobalInfoBean.GlobalBean bean){
        super.mContext = context;
        this.productBean = bean;
        this.likeTv = like;
    }

    @Override
    public void onClick(View v) {
        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(mContext,LoginActivity_.class);
            intent.putExtra("showback",true);
            mContext.startActivity(intent);
            return;
        }
        if(productBean == null)return;
        requestTopicData();
    }

    public void requestTopicData(){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("product_id", productBean.id);
        obj.put("product_name",productBean.title);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.USER_LIKE,
                new Response.Listener<PTopicBean>() {
                    @Override
                    public void onResponse(PTopicBean response) {
                        if(response == null){
                            ToastUtil.show("解析失败");
                            return;
                        }

                        if(response.getResult() == 1 || response.getResult() == -1){
                            productBean.is_liked = productBean.is_liked == 1?0:1;
                            likeTv.setSelected(productBean.is_liked == 1);
                        }else{
                            ToastUtil.show(response.getPrompt()+"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wuli", error.getMessage(), error);
            }
        }, obj,PTopicBean.class);
        request.doRequest();
    }
}
