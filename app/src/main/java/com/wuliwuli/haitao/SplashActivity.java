package com.wuliwuli.haitao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.util.WuliConfig;

import cn.jpush.android.api.InstrumentedActivity;


/**
 * Created by mac-z on 16/4/22.
 */
public class SplashActivity extends InstrumentedActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isFirstUse = checkIsFirstUse();
                if (isFirstUse) {// 首次使用
                    WuliConfig.getInstance().saveBooleanInfoToLocal(WuliConfig.IS_FIRST_USE, false);
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (!isFirstUse){
                    Intent intent = new Intent();
                    boolean login = WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false);
                    String type = getIntent().getStringExtra("type");
                    if(login && "11".equals(type)){
                        intent.setClass(SplashActivity.this,MyRedbaoActivity.class);
                    }else{
                        intent.setClass(SplashActivity.this,login?MainActivity.class:LoginActivity_.class);
                    }
                    startActivity(intent);
                    SplashActivity.this.finish();
                }


            }
        },2000);
    }

    /**
     * 检查是否为首次使用
     */
    private boolean checkIsFirstUse() {
        boolean isFirstUse = WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_FIRST_USE, true);
        return isFirstUse;

    }
}
