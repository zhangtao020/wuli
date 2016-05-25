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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.wuliwuli.haitao.adapter.SpecialAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.InfoBean;
import com.wuliwuli.haitao.bean.PSpecialBean;
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
import java.util.Map;

/**
 * Created by mac-z on 16/4/15.
 */
public class SpecialActivity extends SocialService implements ShareDialog.AlertViewClick{

    private String mId;
    private PSpecialBean.ContentBean specialBean;
    private Picasso picasso = null;
    LinearLayout view_net_error;
    TextView net_error_reload_tv;
    private RecyclerView recyclerView;
    private SpecialAdapter adapter;
    private LinearLayoutManager layoutManager;

    Dialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_special);
        setSystemBarTit();
        setMainTitle("专题");
        showBackButton();
        showShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
                    Intent intent = new Intent(SpecialActivity.this,LoginActivity_.class);
                    intent.putExtra("showback",true);
                    startActivity(intent);
                    return;
                }
                shareDialog = ShareDialog.showDialog(SpecialActivity.this,SpecialActivity.this);
            }
        });

        picasso = Picasso.with(this);
        mId =  getIntent().getStringExtra("id");
        onViewCreated();
        if(AppUtil.isNetworkAvailable(this)){
            requestSpecialData();
        }else{
            view_net_error.setVisibility(View.VISIBLE);
            net_error_reload_tv.setOnClickListener(this);
        }

    }

    private void addHeadView(){
        RecyclerViewHeader itemView = (RecyclerViewHeader) findViewById(R.id.special_header_view);

        ImageView picIv = (ImageView) itemView.findViewById(R.id.header_special_iv);
        TextView likeTv = (TextView) itemView.findViewById(R.id.header_like_tv);
        TextView nameTv = (TextView) itemView.findViewById(R.id.header_name_tv);
        ImageView faceIv = (ImageView) itemView.findViewById(R.id.header_face_iv);
        TextView descTv = (TextView) itemView.findViewById(R.id.header_desc_tv);
        TextView titleTv = (TextView) itemView.findViewById(R.id.header_title_tv);
        FrameLayout shareInd = (FrameLayout) itemView.findViewById(R.id.share_get_bonus_ll);
        itemView.attachTo(recyclerView);

        picasso.load(specialBean.special.cover_img).transform(new Transformation(ScreenUtil.getScreenWidth(this))).placeholder(R.drawable.icon_list_site).into(picIv);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    shareBitmap = picasso.load(specialBean.special.cover_img).get();
                } catch (IOException e) {
                }
            }
        }.start();
        picasso.load(specialBean.special.face).transform(new CircleTransform()).into(faceIv);
        likeTv.setText(specialBean.special.liked_sum);
        nameTv.setText(specialBean.special.uname);
        titleTv.setText("【"+specialBean.special.title+"】");
        descTv.setText(specialBean.special.description);
        shareInd.setOnClickListener(new ShareIndiListener(this,mHandler));

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == 1){
                sendWebPage(specialBean.special.title,specialBean.special.description, shareBitmap, specialBean.link,SendMessageToWX.Req.WXSceneTimeline);
            }else {
                sendWebPage(specialBean.special.title,specialBean.special.description, shareBitmap, specialBean.link, SendMessageToWX.Req.WXSceneSession);
            }


        }
    };

    public void onViewCreated() {
        view_net_error = (LinearLayout) findViewById(R.id.view_net_error);
        net_error_reload_tv = (TextView) findViewById(R.id.net_error_reload_tv);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,ScreenUtil.dipToPx(this,10),false));
        adapter = new SpecialAdapter(null, AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);
        initRefresh();

    }

    private void initRefresh(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void requestSpecialData(){
        if(TextUtils.isEmpty(mId))return;
        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(SpecialActivity.this,LoginActivity_.class);
            intent.putExtra("showback",true);
            startActivity(intent);
            finish();
            return;
        }
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("special_id", mId);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.SPECIAL_LIST,
                new Response.Listener<PSpecialBean>() {
                    @Override
                    public void onResponse(PSpecialBean response) {
                        view_net_error.setVisibility(View.GONE);
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
                        specialBean = response.data;
                        addHeadView();
                        if(specialBean.special.topic!=null)
                            adapter.buildData(specialBean.special.topic);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                view_net_error.setVisibility(View.VISIBLE);
            }
        }, obj,PSpecialBean.class);
        request.doRequest();
    }

    @Override
    public void reloadCurrentPage(){
        super.reloadCurrentPage();
        requestSpecialData();
    }

    @Override
    public void onClick(int position, Dialog dialog) {

        switch (position){
//            case ButtonType.SINA_WEIBO_POSITION:
//                sendWebPage(infoBean.share_h5_url,title,desc);
//                break;
            case ButtonType.WEIXIN_POSITION:
                sendWebPage(specialBean.special.title,specialBean.special.description, shareBitmap, specialBean.link, SendMessageToWX.Req.WXSceneSession);
                break;
            case ButtonType.WGROUP_POSITON:
                sendWebPage(specialBean.special.title,specialBean.special.description, shareBitmap, specialBean.link,SendMessageToWX.Req.WXSceneTimeline);
                break;
//            case ButtonType.QQ_WEIBO_POSITION:
//                break;
            case ButtonType.CANCEL_POSITION:
                dialog.dismiss();
                break;
        }
    }
}
