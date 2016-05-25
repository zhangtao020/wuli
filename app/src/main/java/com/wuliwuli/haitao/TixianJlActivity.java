package com.wuliwuli.haitao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.adapter.BonusAdapter;
import com.wuliwuli.haitao.adapter.TixianAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PMyBonusBean;
import com.wuliwuli.haitao.bean.PRecordBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/27.
 */
public class TixianJlActivity extends AppBaseActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TixianAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_redbao);

        setSystemBarTit();
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(this, 1), false));

        adapter = new TixianAdapter(AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);
        initRefresh();
        requestTopicData();
        setMainTitle("提现记录");
        showBackButton();
    }

    private void initRefresh(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public void requestTopicData(){
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));

        NormalPostRequest request = new NormalPostRequest(UrlManager.MY_RECORD,
                new Response.Listener<PRecordBean>() {
                    @Override
                    public void onResponse(PRecordBean response) {
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;

                        adapter.buildData(response.data);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PRecordBean.class);
        request.doRequest();
    }
}
