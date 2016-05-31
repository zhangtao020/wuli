package com.wuliwuli.haitao.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wuliwuli.haitao.social.SocialService;

/**
 * Created by mac_z on 15/6/9.
 */
public class WXEntryActivity extends SocialService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
