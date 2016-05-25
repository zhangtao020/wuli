package com.wuliwuli.haitao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.adapter.LikeAdapter;
import com.wuliwuli.haitao.adapter.OrderAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PLikeBean;
import com.wuliwuli.haitao.bean.POrderBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/18.
 */
public class MyOrderActivity extends AppBaseActivity{

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    OrderAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_home);
        setSystemBarTit();
        onViewCreated();
    }

    public void onViewCreated() {

        setMainTitle("我的订单");
        showBackButton();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_refreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(this, 10), true));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(null, AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);


        initRefresh();
        requestOrderData();
    }

    private void initRefresh(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                mHandler.sendEmptyMessageDelayed(1, 300);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                requestOrderData();
            }
        }
    };


    public void requestOrderData(){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));
        obj.put("p","16");

        NormalPostRequest request = new NormalPostRequest(UrlManager.MY_ORDER,
                new Response.Listener<POrderBean>() {
                    @Override
                    public void onResponse(POrderBean response) {
                        refreshLayout.setRefreshing(false);
                        if(!parseHeadObject(response)){
                            return;
                        }
                        if(response.data == null || response.data.isEmpty()){
                            ToastUtil.show("暂无数据");
                            return;
                        }
                        adapter.buildData(response.data);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,POrderBean.class);
        request.doRequest();
    }


}
