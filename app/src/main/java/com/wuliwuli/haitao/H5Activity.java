package com.wuliwuli.haitao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.fragment.WebFragment;
import com.wuliwuli.haitao.http.UrlManager;

public class H5Activity extends AppBaseActivity{

    /**
     * web页面
     */
    private WebFragment common_fragment;

    /**
     * 加载的URL
     */
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        setSystemBarTit();
        showBackButton();
        if(getIntent().hasExtra("type") && !TextUtils.isEmpty(getIntent().getStringExtra("type"))){
            String type = getIntent().getStringExtra("type");
            if("about".equals(type)){
                url = UrlManager.ABOUT_ME;
            }else if("question".equals(type)){
                url = UrlManager.QUESTION;
            }else if("fuli".equals(type)){
                url = UrlManager.FULI;
            }
        }else{
            getIntentData();
        }
        loadWebData();

    }

    /**
     * 开始加载网页数据
     */
    private void loadWebData() {
        common_fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        common_fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.commonh5_layout, common_fragment).commit();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
        }
    }

}
