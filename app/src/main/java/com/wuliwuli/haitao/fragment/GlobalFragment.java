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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.adapter.LikeAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseFragment;
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


    Button listModeBtn,gridModeBtn;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    LikeAdapter adapter;
    GridLayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;

    int lastVisibleItem = 0;
    String maxId="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global,null,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listModeBtn = (Button) view.findViewById(R.id.title_global_list_btn);
        gridModeBtn = (Button) view.findViewById(R.id.title_global_grid_btn);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_refreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(mActivity,10),false));
        adapter = new LikeAdapter(null, AppApplication.DISPLAY_WIDTH/2);
        recyclerView.setAdapter(adapter);

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

                        List<ProductBean> da = new ArrayList<ProductBean>();
                        da.addAll(response.data);
                        da.addAll(response.data);
                        da.addAll(response.data);
                        da.addAll(response.data);
                        da.addAll(response.data);
                        da.addAll(response.data);
                        adapter.buildData(da);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PLikeBean.class);
        request.doRequest();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.title_global_grid_btn){
            if(layoutManager == null){
                layoutManager = new GridLayoutManager(mActivity,2);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtil.dipToPx(mActivity, 10), true));
            }
            recyclerView.setLayoutManager(layoutManager);
        }else if(v.getId() == R.id.title_global_list_btn){
            if(linearLayoutManager == null){
                linearLayoutManager = new LinearLayoutManager(mActivity);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(mActivity,10),false));
            }
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
