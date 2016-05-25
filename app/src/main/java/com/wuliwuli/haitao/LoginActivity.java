package com.wuliwuli.haitao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wuliwuli.haitao.bean.BaseResult;
import com.wuliwuli.haitao.bean.PLoginBean;
import com.wuliwuli.haitao.bean.PSmsBean;
import com.wuliwuli.haitao.bean.WeiXinBean;
import com.wuliwuli.haitao.bean.WeiXinInfoBean;
import com.wuliwuli.haitao.http.HttpManager;
import com.wuliwuli.haitao.http.MCAppRequest;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.interfaces.LoadPageListener;
import com.wuliwuli.haitao.social.SocialService;
import com.wuliwuli.haitao.util.JsonUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/20.
 */
@EActivity
public class LoginActivity extends SocialService implements LoadPageListener{

    @ViewById
    TextView oather_login_wechat_tv;
    @ViewById
    EditText login_edittext_phone;
    @ViewById
    ImageView mobile_clear_iv;
    @ViewById
    ImageView pwd_look_iv;
    @ViewById
    TextView login_getcode;
    @ViewById
    EditText login_edittext_code;
    @ViewById
    TextView login_login;
    @ViewById
    RelativeLayout login_verf_rl;
    @ViewById
    LinearLayout login_pwd_ll;
    @ViewById
    LinearLayout btm_view;
    @ViewById
    ImageButton title_back_btn;

    String fromPage;
    ProgressDialog mDialog;

//    PSmsBean.SmsBean smsBean;
    @AfterViews
    public void initViews() {
        boolean showBack = getIntent().getBooleanExtra("showback",false);
        if(showBack){
            title_back_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        setSystemBarTitByColor();

        loadPageListener = this;
    }

    @Click(R.id.oather_login_wechat_tv)
    public void clickByWechat() {
        sendLogin();
    }

    @Click(R.id.title_back_btn)
    public void clickByBack(){
        finish();
    }

    @Click(R.id.login_login)
    public void clickByLogin() {
//            //TODO 微信登录
//            sendLogin();

            if (TextUtils.isEmpty(login_edittext_code.getText())) {
                ToastUtil.show("验证码不能为空");
                return;
            }
            mDialog = ProgressDialog.show(this, "", "login...", true, true);
            requestLogin();
    }

    @Click(R.id.login_getcode)
    public void clickBySms() {
        requestSendSms(1);
    }

    @Click(R.id.login_enter_main)
    public void clcikByEnterMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void requestLogin() {

        if (TextUtils.isEmpty(login_edittext_phone.getText())) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (TextUtils.isEmpty(login_edittext_code.getText())) {
            ToastUtil.show("验证码不能为空");
            return;
        }

        Map<String, String> obj = new HashMap<String, String>();
        obj.put("mobile", login_edittext_phone.getText().toString());
        obj.put("code", login_edittext_code.getText().toString());

        NormalPostRequest request = new NormalPostRequest(UrlManager.EASY_LOGIN,
                new Response.Listener<PLoginBean>() {
                    @Override
                    public void onResponse(PLoginBean response) {
                        mDialog.dismiss();
                        if (!parseHeadObject(response)) {
                            return;
                        }
                        if (response.data == null) {
                            return;
                        }

                        if(TextUtils.isEmpty(response.data.id) || response.data.id == null){
                            ToastUtil.show("登录失败,请重新登录");
                            return;
                        }
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_ID, response.data.id);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_NAME, response.data.nick_name);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_FACE, response.data.face);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.TOKEN_ID, response.data.token);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.OPEN_ID, response.data.open_id);
                        WuliConfig.getInstance().saveBooleanInfoToLocal(WuliConfig.IS_LOGIN, true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                mDialog.dismiss();
            }
        }, obj, PLoginBean.class);
        request.doRequest();
    }


    public void requestRegistByWx(final WeiXinInfoBean bean) {
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("open_id", bean.openid);
        obj.put("unionid", bean.unionid);

        NormalPostRequest request = new NormalPostRequest(UrlManager.WX_LOGIN,
                new Response.Listener<LoginBean>() {
                    @Override
                    public void onResponse(LoginBean response) {
                        if (!parseHeadObject(response)) {
                            return;
                        }
                        if (response.data == null) {
                            return;
                        }
                        if(TextUtils.isEmpty(response.data.id) || response.data.id == null){
                            ToastUtil.show("登录失败,请重新登录");
                            return;
                        }
                        if (response.data.is_type == 1) {
                            WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_NAME, response.data.nick_name);
                            WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_FACE, response.data.face);
                            WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.TOKEN_ID, response.data.token);
                            WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.OPEN_ID, response.data.open_id);
                            WuliConfig.getInstance().saveBooleanInfoToLocal(WuliConfig.IS_LOGIN, true);
                            WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_ID,response.data.id);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else if (response.data.is_type == 2) {
                            requestRegist(bean);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                mDialog.dismiss();
            }
        }, obj, LoginBean.class);
        request.doRequest();
    }

    public void requestRegist(WeiXinInfoBean bean) {
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("open_id", bean.openid);
        obj.put("unionid",bean.unionid);
        obj.put("nick_name", bean.nickname);
        obj.put("face",bean.headimgurl);

        NormalPostRequest request = new NormalPostRequest(UrlManager.WX_REGIST,
                new Response.Listener<PLoginBean>() {
                    @Override
                    public void onResponse(PLoginBean response) {
                        if (!parseHeadObject(response)) {
                            return;
                        }
                        if (response.data == null) {
                            return;
                        }
                        if(TextUtils.isEmpty(response.data.id) || response.data.id == null){
                            ToastUtil.show("登录失败,请重新登录");
                            return;
                        }
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_ID,response.data.id);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_NAME, response.data.nick_name);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_FACE, response.data.face);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.TOKEN_ID, response.data.token);
                        WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.OPEN_ID, response.data.open_id);
                        WuliConfig.getInstance().saveBooleanInfoToLocal(WuliConfig.IS_LOGIN, true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                mDialog.dismiss();
            }
        }, obj, PLoginBean.class);
        request.doRequest();
    }

    public void requestSendSms(int type) {

        if (TextUtils.isEmpty(login_edittext_phone.getText())) {
            ToastUtil.show("手机号不能为空");
            return;
        }


        Map<String, String> obj = new HashMap<String, String>();
        obj.put("mobile", login_edittext_phone.getText().toString());
        obj.put("type", String.valueOf(type));

        NormalPostRequest request = new NormalPostRequest(UrlManager.SMS_LOGIN,
                new Response.Listener<BaseResult>() {
                    @Override
                    public void onResponse(BaseResult response) {
                        if (!parseHeadObject(response)) {
                            return;
                        }
                        // 验证码获取成功,服务端返回: 验证码已发送 时:开始60s倒计时
                        login_getcode.setEnabled(false);
                        login_getcode.setText("60s");
                        reTime();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj, BaseResult.class);
        request.doRequest();
    }

    int time = 60;
    /**
     * 倒计时
     */
    private void reTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (time > 0) {
                    try {
                        time -= 1;
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.what = 998;
                        message.arg1 = time;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 998) {
                int t = msg.arg1;
                if (t == 0) {
                    login_getcode.setText("获取验证码");
                    login_getcode.setEnabled(true);
                    time = 60;
                } else {
                    login_getcode.setText(t+"s");

                }

            }
            return false;
        }
    });


    @Override
    public void doLoadSuccess(String msg, String state) {
        requestWXToken(msg);
    }

    @Override
    public void doLoadSuccess() {

    }

    @Override
    public void doLoadFail() {

    }

    public void requestWXToken(String code) {

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APP_ID+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.d("zt","requestWXToken----"+response);

                WeiXinBean bean = JsonUtil.jsonObjToJava(response, WeiXinBean.class);

                if("40029".equals(bean.errcode)){
                    ToastUtil.show(bean.errmsg);
                    return;
                }
                refreshWXToken(bean.openid,bean.refresh_token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        HttpManager.getInstance().doRequest(request);
    }

//    https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET

    public void refreshWXToken(final String openid,String refreshtoken) {

        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?grant_type=refresh_token&appid="+APP_ID+"&secret="+AppSecret+"&refresh_token="+refreshtoken;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.d("zt","refreshWXToken----"+response);

                try {
                    JSONObject obj = new JSONObject(response);
                    requestWeixinInfo(obj.getString("access_token"),openid);
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        HttpManager.getInstance().doRequest(request);
    }
    //https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID

    private void requestWeixinInfo(String token,String openid){
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openid;
        MCAppRequest request = new MCAppRequest(Request.Method.GET,url,"",new Response.Listener<WeiXinInfoBean>() {
            @Override
            public void onResponse(WeiXinInfoBean response) {
                Log.d("zt","requestWeixinInfo----"+response.nickname);


                requestRegistByWx(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },WeiXinInfoBean.class);
        request.doRequest();
    }

    class LoginBean extends BaseResult{
        public ContentBean data;

        public class ContentBean{
            public int is_type ;
            public String id;
            public String user_name;
            public String nick_name;
            public String face;
            public String open_id;
            public String token;
        }
    }

}

