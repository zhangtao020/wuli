package com.wuliwuli.haitao;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.wuliwuli.haitao.adapter.TopicAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.PTopicBean;
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

import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/14.
 */
public class TopicActivity extends SocialService implements ShareDialog.AlertViewClick{

    private Picasso picasso = null;
    LinearLayout view_net_error;
    TextView net_error_reload_tv;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String mId;
    private int mPosition;
    private PTopicBean.SpecBean topicBean;
    private PTopicBean.ContentBean contentBean;
    private TopicAdapter adapter;

    Dialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_topic);

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

        adapter = new TopicAdapter(null, AppApplication.DISPLAY_WIDTH);
        recyclerView.setAdapter(adapter);
        initRefresh();
        if(AppUtil.isNetworkAvailable(this)){
            requestTopicData();
        }else{
            view_net_error.setVisibility(View.VISIBLE);
            net_error_reload_tv.setOnClickListener(this);
        }

        setMainTitle("话题");
        showBackButton();
        showShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
                    Intent intent = new Intent(TopicActivity.this,LoginActivity_.class);
                    intent.putExtra("showback",true);
                    startActivity(intent);
                    return;
                }
                shareDialog = ShareDialog.showDialog(TopicActivity.this,TopicActivity.this);
            }
        });

    }

    private void initRefresh(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addHeadView(){

        RecyclerViewHeader headerView = (RecyclerViewHeader) findViewById(R.id.header_recyclerview);

        ImageView picIv = (ImageView) headerView.findViewById(R.id.item_pic_iv);
        TextView nameTv = (TextView) headerView.findViewById(R.id.item_uname_tv);
        ImageView faceIv = (ImageView) headerView.findViewById(R.id.item_uface_iv);
        TextView timeTv = (TextView) headerView.findViewById(R.id.item_time_tv);
        TextView descTv = (TextView) headerView.findViewById(R.id.item_topic_desc_tv);
        TextView likeSumTv = (TextView) headerView.findViewById(R.id.item_topic_like_sum_tv);
        FrameLayout shareInd = (FrameLayout) headerView.findViewById(R.id.share_get_bonus_ll);
        LinearLayout headerContainer = (LinearLayout) headerView.findViewById(R.id.item_topic_like_head_ll);
        headerView.attachTo(recyclerView);

        if(topicBean!=null){
            picasso.load(topicBean.cover_img).transform(new Transformation(AppApplication.DISPLAY_WIDTH)).placeholder(R.drawable.icon_list_site).into(picIv);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        shareBitmap = picasso.load(topicBean.cover_img).get();
                    } catch (IOException e) {
                    }
                }
            }.start();

            if(!TextUtils.isEmpty(topicBean.face))
                picasso.load(topicBean.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(faceIv);
            else{
                faceIv.setImageBitmap(AppUtil.initFace(this));
            }
            likeSumTv.setText(topicBean.liked_sum);
            nameTv.setText(topicBean.uname);
            descTv.setText(topicBean.description);
            timeTv.setText(topicBean.created);

            if(topicBean.user_liked!=null && !topicBean.user_liked.isEmpty()){
                headerContainer.removeAllViews();
                int width = ScreenUtil.dipToPx(this,33);
                for(PTopicBean.FaceBean faceBean : topicBean.user_liked){
                    if(faceBean == null)continue;
                    if((width*(headerContainer.getChildCount()+1) + (headerContainer.getChildCount()+1)*ScreenUtil.dipToPx(TopicActivity.this,10))>=AppApplication.DISPLAY_WIDTH-width)break;
                    ImageView imageView = new ImageView(this);
                    if(!TextUtils.isEmpty(faceBean.face))
                        picasso.load(faceBean.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(imageView);
                    else{
                        imageView.setImageBitmap(AppUtil.initFace(this));
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
                    params.rightMargin = ScreenUtil.dipToPx(TopicActivity.this,10);
                    params.gravity = Gravity.CENTER;
                    headerContainer.addView(imageView,params);
                }
            }

        }
        shareInd.setOnClickListener(new ShareIndiListener(this,mHandler));
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == 1){
                sendWebPage(contentBean.topic.title,"发现一个可以买到全球好货的App“WuliWuli全球购”。",shareBitmap,contentBean.link,SendMessageToWX.Req.WXSceneTimeline);
            }else {
                sendWebPage(contentBean.topic.title,"发现一个可以买到全球好货的App“WuliWuli全球购”。", shareBitmap, contentBean.link, SendMessageToWX.Req.WXSceneSession);
            }
        }
    };

    public void requestTopicData(){
        if (TextUtils.isEmpty(mId)) return;
        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(TopicActivity.this,LoginActivity_.class);
            intent.putExtra("showback",true);
            startActivity(intent);
            finish();
            return;
        }
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("topic_id", mId);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.TOPIC_LIST,
                new Response.Listener<PTopicBean>() {
                    @Override
                    public void onResponse(PTopicBean response) {
                        view_net_error.setVisibility(View.GONE);
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
                        contentBean = response.data;
                        topicBean = response.data.topic;
                        addHeadView();
                        adapter.buildData(topicBean.product);
                        recyclerView.scrollToPosition(mPosition);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                net_error_reload_tv.setVisibility(View.VISIBLE);
            }
        }, obj,PTopicBean.class);
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
                sendWebPage(contentBean.topic.title,"发现一个可以买到全球好货的App“WuliWuli全球购”。", shareBitmap, contentBean.link, SendMessageToWX.Req.WXSceneSession);
                break;
            case ButtonType.WGROUP_POSITON:
                sendWebPage(contentBean.topic.title,"发现一个可以买到全球好货的App“WuliWuli全球购”。",shareBitmap,contentBean.link,SendMessageToWX.Req.WXSceneTimeline);
                break;
            case ButtonType.CANCEL_POSITION:
                dialog.dismiss();
                break;
        }
    }
}
