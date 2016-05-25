package com.wuliwuli.haitao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.adapter.BonusAdapter;
import com.wuliwuli.haitao.adapter.TopicAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PMyBonusBean;
import com.wuliwuli.haitao.bean.PTopicBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.transform.Transformation;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;
import com.wuliwuli.haitao.view.RecyclerViewHeader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/14.
 */
public class MyRedbaoActivity extends AppBaseActivity{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private BonusAdapter adapter;

    int money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_redbao);
        setSystemBarTit();
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(this, 10), false));

        adapter = new BonusAdapter(AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);
        initRefresh();
        requestTopicData();
        setMainTitle("红包明细");
        showBackButton();
    }

    private void initRefresh(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addHeadView(PMyBonusBean.CountBean countBean){
        money = Integer.parseInt(countBean.kt);
    }


    public void requestTopicData(){
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));

        NormalPostRequest request = new NormalPostRequest(UrlManager.MY_BONUS,
                new Response.Listener<PMyBonusBean>() {
                    @Override
                    public void onResponse(PMyBonusBean response) {
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
//                        if(response.data.count!=null)
                            addHeadView(response.data.count);
                        if(response.data.bonus!=null)
                            adapter.buildData(response.data.bonus,response.data.count);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PMyBonusBean.class);
        request.doRequest();
    }

}
