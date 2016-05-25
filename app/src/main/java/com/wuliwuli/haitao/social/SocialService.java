package com.wuliwuli.haitao.social;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.interfaces.LoadPageListener;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.Util;

/**
 * Created by mac-z on 16/3/18.
 */
public class SocialService extends AppBaseActivity implements IWXAPIEventHandler {//, IWeiboHandler.Response
    public String APP_ID = "wx6f1ec1ca6a219e0e"; //微信key
    public String AppSecret = "8bc852749f0a08c0011d843d2a018318";//微信secret

    String APP_KEY  = "93110515";         //微博key

    String UMENG_KEY = "5719e868e0f55ace27000e7b";

    IWXAPI api;
//    IWeiboShareAPI mWeiboShareAPI = null;


//    public static PayCallBackListener payCallBackListener;
    public static LoadPageListener loadPageListener;

    public Bitmap shareBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);

//        initWeiboApi();
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
//        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
//            if(payCallBackListener == null)return;
//            payCallBackListener.doPayCallback(String.valueOf(baseResp.errCode));
        }else {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if(baseResp instanceof SendAuth.Resp){
                        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                        loadPageListener.doLoadSuccess(resp.code,resp.state);
                    }else{
                        loadPageListener.doLoadSuccess();
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    loadPageListener.doLoadFail();
                    break;
                default:
                    break;
            }
        }
        finish();
    }

    public void sendWebPage(String title, String desc, Bitmap bitmap, String url, int scene) {

        if(!isInstall()) {
            ToastUtil.show("未安装微信客户端");
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        if(bitmap!=null){
            msg.thumbData = Util.bmpToByteArray(bitmap,false);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    public void sendLogin(){
        if(!isInstall()) {
            ToastUtil.show("未安装微信客户端");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "com.wuliwuli.haitao.social.SocialService";
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public boolean isInstall() {
        return api.isWXAppInstalled();
    }


    //-----------------------sina-----------

//    private void initWeiboApi(){
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, APP_KEY);
//        boolean register = mWeiboShareAPI.registerApp();
//
//    }
//
//    public void sendWebPage(String url,String title,String desc){
//        mWeiboShareAPI.registerApp();
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        weiboMessage.mediaObject = getTextObj(url,title,desc);
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;
//
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.sendRequest(this, request);
//    }
//
//    private TextObject getTextObj(String url,String title,String desc) {
//        TextObject textObject = new TextObject();
//        textObject.text = title+"\n"+desc+url;
//        return textObject;
//    }
//
//    @Override
//    public void onResponse(BaseResponse baseResponse) {
//        if(baseResponse!= null){
//            switch (baseResponse.errCode) {
//                case 0:
//                    ToastUtil.show("分享成功");
//                    loadPageListener.doLoadSuccess();
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    ToastUtil.show("分享失败");
//                    break;
//            }
//        }
//    }
}
