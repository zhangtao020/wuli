package com.wuliwuli.haitao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.adapter.HomeAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseFragment;
import com.wuliwuli.haitao.bean.InfoBean;
import com.wuliwuli.haitao.bean.PProductBean;
import com.wuliwuli.haitao.http.MCAppRequest;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mac-z on 16/4/13.
 */
public class HomeFragment extends AppBaseFragment{

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    LinearLayoutManager layoutManager;

    int lastVisibleItem = 0;
    String maxId="",disorder="";
    boolean isLoadMore = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMainTitle(getString(R.string.app_name));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_refreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(mActivity,10),false));
        adapter = new HomeAdapter(null, AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);

        initRefresh();
        requestHomeData("");
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

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    isLoadMore = true;
                    mHandler.sendEmptyMessageDelayed(2, 300);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                requestHomeData("");
            }else if(msg.what == 2){
                requestHomeData(maxId);
            }
        }
    };


    public void requestHomeData(String maxid){

        Map<String,String> obj = new HashMap<String,String>();
        if(!TextUtils.isEmpty(maxid))
            obj.put("max_id", maxid);
        obj.put("p", "15");
        if(!TextUtils.isEmpty(disorder))
            obj.put("disorder",disorder);

        NormalPostRequest request = new NormalPostRequest(UrlManager.HOME_INDEX,
                new Response.Listener<InfoBean>() {
                    @Override
                    public void onResponse(InfoBean response) {
                        isLoadMore =false;
                        refreshLayout.setRefreshing(false);
                        if(!parseHeadObject(response)){
                            return;
                        }
                        if(response.data == null || response.data.isEmpty()){
                            ToastUtil.show("暂无数据");
                            adapter.hideFootView();
                            return;
                        }
                        maxId = response.data.get(response.data.size()-1).id;
                        disorder = response.data.get(response.data.size()-1).disorder;
                        adapter.buildData(response.data);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                isLoadMore =false;
                refreshLayout.setRefreshing(false);
            }
        }, obj,InfoBean.class);
        request.doRequest();
    }

}
