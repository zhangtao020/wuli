package com.wuliwuli.haitao.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.H5Activity;
import com.wuliwuli.haitao.LoginActivity_;
import com.wuliwuli.haitao.base.AppBaseListener;
import com.wuliwuli.haitao.bean.PBuyUrlBean;
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
public class BuyProductListener extends AppBaseListener{

    String product_id;

    public BuyProductListener(Context context, String id){
        super.mContext = context;
        this.product_id = id;
    }

    @Override
    public void onClick(View v) {

        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(mContext,LoginActivity_.class);
            intent.putExtra("showback",true);
            mContext.startActivity(intent);
            return;
        }
        requestProData();
    }

    public void requestProData(){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("product_id", product_id);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.SCP,
                new Response.Listener<PBuyUrlBean>() {
                    @Override
                    public void onResponse(PBuyUrlBean response) {
                        if(response == null){
                            ToastUtil.show("解析失败");
                        }
                        String url = response.data.url;
                        Intent intent = new Intent();
                        intent.setClass(mContext, H5Activity.class);
                        intent.putExtra("url",url);
                        mContext.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wuli", error.getMessage(), error);
            }
        }, obj,PBuyUrlBean.class);
        request.doRequest();
    }
}
