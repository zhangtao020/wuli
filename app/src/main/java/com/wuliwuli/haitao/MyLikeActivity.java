package com.wuliwuli.haitao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.adapter.LikeAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PLikeBean;
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
public class MyLikeActivity extends AppBaseActivity{

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    LikeAdapter adapter;
    GridLayoutManager layoutManager;

    int lastVisibleItem = 0;
    String maxId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_home);
        setSystemBarTit();
        onViewCreated();
    }

    public void onViewCreated() {

        setMainTitle("我的喜欢");
        showBackButton();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_refreshlayout);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dipToPx(this, 10), true));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LikeAdapter(null, AppApplication.DISPLAY_WIDTH/2);
        recyclerView.setAdapter(adapter);


//        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        recyclerView.setLayoutParams();

        initRefresh();
        requestLikeData("");
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

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    mHandler.sendEmptyMessageDelayed(2, 300);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//            }
//        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                requestLikeData("");
            }else if(msg.what == 2){
                requestLikeData(maxId);
            }
        }
    };


    public void requestLikeData(String maxid){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));
        obj.put("p","16");

        NormalPostRequest request = new NormalPostRequest(UrlManager.MY_LIKE,
                new Response.Listener<PLikeBean>() {
                    @Override
                    public void onResponse(PLikeBean response) {
                        refreshLayout.setRefreshing(false);
                        if(!parseHeadObject(response)){
                            return;
                        }
                        if(response.data == null || response.data.isEmpty()){
                            ToastUtil.show("暂无数据");
                            return;
                        }
                        maxId = response.data.get(response.data.size()-1).id;
                        adapter.buildData(response.data);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PLikeBean.class);
        request.doRequest();
    }


}
