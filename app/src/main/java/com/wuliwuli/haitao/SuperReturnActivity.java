package com.wuliwuli.haitao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.wuliwuli.haitao.adapter.SuperAdapter;
import com.wuliwuli.haitao.adapter.TopicAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.bean.PSuperBean;
import com.wuliwuli.haitao.bean.PTopicBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.listener.ShareIndiListener;
import com.wuliwuli.haitao.social.SocialService;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.transform.Transformation;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.util.ButtonType;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.GridSpacingItemDecoration;
import com.wuliwuli.haitao.view.RecyclerViewHeader;
import com.wuliwuli.haitao.view.ShareDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac-z on 16/5/22.
 */
public class SuperReturnActivity extends SocialService implements ShareDialog.AlertViewClick{

    private Picasso picasso = null;
    LinearLayout view_net_error;
    TextView net_error_reload_tv;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String mId;
    private int mPosition;
    private List<ProductBean> product;
    private PSuperBean.ContentBean contentBean;
    private SuperAdapter adapter;

    Dialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_super);

        setSystemBarTit();


        picasso = Picasso.with(this);
        mId =  getIntent().getStringExtra("id");
        mPosition = getIntent().getIntExtra("position",0);
        view_net_error = (LinearLayout) findViewById(R.id.view_net_error);
        net_error_reload_tv = (TextView) findViewById(R.id.net_error_reload_tv);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtil.dipToPx(this, 10), false));

        adapter = new SuperAdapter(AppApplication.DISPLAY_WIDTH,mId);
        recyclerView.setAdapter(adapter);
        initRefresh();
        if(AppUtil.isNetworkAvailable(this)){
            requestTopicData();
        }else{
            view_net_error.setVisibility(View.VISIBLE);
            net_error_reload_tv.setOnClickListener(this);
        }
        setMainTitle("新人专区");
        showBackButton();
        showShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
                    Intent intent = new Intent(SuperReturnActivity.this,LoginActivity_.class);
                    intent.putExtra("showback",true);
                    startActivity(intent);
                    return;
                }
                shareDialog = ShareDialog.showDialog(SuperReturnActivity.this,SuperReturnActivity.this);
            }
        });

    }

    private void initRefresh(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addHeadView(){

        RecyclerViewHeader headerView = (RecyclerViewHeader) findViewById(R.id.header_recyclerview);

        ImageView picIv = (ImageView) headerView.findViewById(R.id.item_pic_iv);
        headerView.attachTo(recyclerView);

        if(contentBean!=null && contentBean.supermoney!=null){
            picasso.load(contentBean.supermoney.cover_img).transform(new Transformation(AppApplication.DISPLAY_WIDTH)).placeholder(R.drawable.icon_list_site).into(picIv);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        shareBitmap = picasso.load(contentBean.supermoney.cover_img).get();
                    } catch (IOException e) {
                    }
                }
            }.start();
        }
    }

    public void requestTopicData(){
        if (TextUtils.isEmpty(mId)) return;
        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(SuperReturnActivity.this,LoginActivity_.class);
            intent.putExtra("showback",true);
            startActivity(intent);
            finish();
            return;
        }
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("super_id", mId);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.SUPER_RETURN,
                new Response.Listener<PSuperBean>() {
                    @Override
                    public void onResponse(PSuperBean response) {
                        view_net_error.setVisibility(View.GONE);
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
                        contentBean = response.data;
                        if(response.data.supermoney == null)return;
                        product = response.data.supermoney.product;
                        addHeadView();
                        adapter.buildData(product,contentBean);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                view_net_error.setVisibility(View.VISIBLE);
            }
        }, obj,PSuperBean.class);
        request.doRequest();
    }

    @Override
    public void reloadCurrentPage(){
        super.reloadCurrentPage();
        requestTopicData();
    }

    @Override
    public void onClick(int position, Dialog dialog) {

        switch (position){
            case ButtonType.WEIXIN_POSITION:
                sendWebPage(contentBean.supermoney.title,contentBean.supermoney.title, shareBitmap, contentBean.link, SendMessageToWX.Req.WXSceneSession);
                break;
            case ButtonType.WGROUP_POSITON:
                sendWebPage(contentBean.supermoney.title,contentBean.supermoney.title,shareBitmap,contentBean.link,SendMessageToWX.Req.WXSceneTimeline);
                break;
            case ButtonType.CANCEL_POSITION:
                dialog.dismiss();
                break;
        }
    }
}
