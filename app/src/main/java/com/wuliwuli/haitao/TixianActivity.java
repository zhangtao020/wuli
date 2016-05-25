package com.wuliwuli.haitao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PTopicBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/21.
 */
public class TixianActivity extends AppBaseActivity {

    TextView moneyTv;
    EditText inputMoneyEt;
    Button submitBtn;

    int money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tixian);
        setSystemBarTit();

        setMainTitle("申请提现");
        showBackButton();

        moneyTv = (TextView) findViewById(R.id.tixian_money_tv);
        inputMoneyEt = (EditText) findViewById(R.id.tixian_input_et);
        submitBtn = (Button) findViewById(R.id.tixian_submit_btn);

        money = getIntent().getIntExtra("money",0);
        moneyTv.setText("可提现￥"+money);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    public void requestTopicData(){
//        if (TextUtils.isEmpty(mId)) return;
//        Map<String,String> obj = new HashMap<String,String>();
//        obj.put("topic_id", mId);
//        obj.put("user_id", "15004");
//
//
//        NormalPostRequest request = new NormalPostRequest(UrlManager.TOPIC_LIST,
//                new Response.Listener<PTopicBean>() {
//                    @Override
//                    public void onResponse(PTopicBean response) {
//                        if(!parseHeadObject(response))return;
//                        if(response.data == null) return;
//                        topicBean = response.data.topic;
//                        addHeadView();
//                        adapter.buildData(topicBean.product);
//                        recyclerView.scrollToPosition(mPosition);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("", error.getMessage(), error);
//            }
//        }, obj,PTopicBean.class);
//        request.doRequest();
//    }
}
