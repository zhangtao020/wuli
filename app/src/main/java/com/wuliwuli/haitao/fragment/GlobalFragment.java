package com.wuliwuli.haitao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.adapter.GlobalGridAdapter;
import com.wuliwuli.haitao.adapter.GlobalListAdapter;
import com.wuliwuli.haitao.adapter.LikeAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseFragment;
import com.wuliwuli.haitao.bean.PGlobalBean;
import com.wuliwuli.haitao.bean.PLikeBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac-z on 16/4/18.
 */
public class GlobalFragment extends AppBaseFragment implements View.OnClickListener{


    TextView listModeBtn,gridModeBtn;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    GlobalListAdapter linearAdapter;
    GlobalGridAdapter adapter;
    GridLayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;

    int lastVisibleItem = 0;
    String maxId="",disorder = "";
    boolean isLinearLayout = true;

    GridSpacingItemDecoration linearItemDecoration = null;
    GridSpacingItemDecoration gridItemDecoration = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global,null,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listModeBtn = (TextView) view.findViewById(R.id.title_global_list_btn);
        gridModeBtn = (TextView) view.findViewById(R.id.title_global_grid_btn);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_refreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);

        recyclerView.setHasFixedSize(true);
        adapter = new GlobalGridAdapter(null,AppApplication.DISPLAY_WIDTH/2);

        linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearItemDecoration = new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(mActivity,10),false);
        recyclerView.addItemDecoration(linearItemDecoration);
        linearAdapter = new GlobalListAdapter(null, AppApplication.DISPLAY_WIDTH/2);
        recyclerView.setAdapter(linearAdapter);
        listModeBtn.setSelected(true);
        listModeBtn.setOnClickListener(this);
        gridModeBtn.setOnClickListener(this);

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

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    mHandler.sendEmptyMessageDelayed(2, 300);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLinearLayout)
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                else
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                maxId="";
                disorder = "";
                requestLikeData("");
            }else if(msg.what == 2){
                requestLikeData(maxId);
            }
        }
    };


    public void requestLikeData(final String maxid){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("p","16");
        if(!TextUtils.isEmpty(maxid))
            obj.put("max_id", maxid);
        if(!TextUtils.isEmpty(disorder))
            obj.put("disorder",disorder);

        NormalPostRequest request = new NormalPostRequest(UrlManager.GOLBAL_PRICE,
                new Response.Listener<PGlobalBean>() {
                    @Override
                    public void onResponse(PGlobalBean response) {
                        refreshLayout.setRefreshing(false);
                        if(!parseHeadObject(response)){
                            return;
                        }
                        if(response.data == null || response.data.isEmpty()){
                            ToastUtil.show("暂无数据");
                            if(isLinearLayout){
                                linearAdapter.hideFootView();
                            }else{
                                adapter.hideFootView();
                            }
                            return;
                        }
                        if(TextUtils.isEmpty(maxid)){
                            linearAdapter.buildData(response.data);
                            adapter.buildData(response.data);
                        }else {
                            if(isLinearLayout){
                                linearAdapter.buildData(response.data);
                            }else{
                                adapter.buildData(response.data);
                            }
                        }
                        maxId = response.data.get(response.data.size()-1).id;
                        disorder = response.data.get(response.data.size()-1).disorder;


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PGlobalBean.class);
        request.doRequest();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.title_global_grid_btn){
            isLinearLayout = false;
            listModeBtn.setSelected(false);
            gridModeBtn.setSelected(true);
            if(layoutManager == null){
                layoutManager = new GridLayoutManager(mActivity,2);
            }
            if(gridItemDecoration == null){
                gridItemDecoration = new GridSpacingItemDecoration(2, ScreenUtil.dipToPx(mActivity, 10), true);
            }else{
                recyclerView.removeItemDecoration(gridItemDecoration);
            }
            if(linearItemDecoration!=null)
                recyclerView.removeItemDecoration(linearItemDecoration);

            recyclerView.addItemDecoration(gridItemDecoration);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }else if(v.getId() == R.id.title_global_list_btn){
            isLinearLayout = true;
            listModeBtn.setSelected(true);
            gridModeBtn.setSelected(false);
            if(linearLayoutManager == null){
                linearLayoutManager = new LinearLayoutManager(mActivity);
            }
            if(linearItemDecoration == null){
                linearItemDecoration = new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(mActivity,10),false);
            }else{
                recyclerView.removeItemDecoration(linearItemDecoration);
            }

            if(gridItemDecoration!=null){
                recyclerView.removeItemDecoration(gridItemDecoration);
            }
            recyclerView.addItemDecoration(linearItemDecoration);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(linearAdapter);

        }
    }
}
