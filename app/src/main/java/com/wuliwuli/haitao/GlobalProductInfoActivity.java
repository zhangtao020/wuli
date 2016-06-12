package com.wuliwuli.haitao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.wuliwuli.haitao.adapter.ProductImgAdapter;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.bean.PBuyUrlBean;
import com.wuliwuli.haitao.bean.PGlobalInfoBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.listener.BonusListener;
import com.wuliwuli.haitao.listener.GlobalLikeListener;
import com.wuliwuli.haitao.listener.ShareIndiListener;
import com.wuliwuli.haitao.listener.ViewPagerIndicator;
import com.wuliwuli.haitao.social.SocialService;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.util.ButtonType;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;
import com.wuliwuli.haitao.view.ShareDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac-z on 16/4/16.
 */
@EActivity(R.layout.activity_global_product_info)
public class GlobalProductInfoActivity extends SocialService implements ShareDialog.AlertViewClick {

    @ViewById
    LinearLayout btm_bar;
    @ViewById
    ScrollView info_content_view;
    @ViewById
    ImageView info_like2_tv;
    @ViewById
    LinearLayout info_like_ll;
    @ViewById
    LinearLayout info_buy_ib;
    @ViewById
    ViewPager info_view_pager;
    @ViewById
    RadioGroup home_index_radiogroup;
    @ViewById
    TextView info_product_title_tv;
    @ViewById
    ImageView info_product_bonus_iv;
    @ViewById
    TextView info_global_price_tv;
    @ViewById
    FrameLayout info_compare_container;
    @ViewById
    LinearLayout info_compare1;
    @ViewById
    TextView info_compare1_name_tv;
    @ViewById
    TextView info_compare1_price_tv;
    @ViewById
    LinearLayout info_compare2;
    @ViewById
    TextView info_compare2_name_tv;
    @ViewById
    TextView info_compare2_price_tv;
    @ViewById
    LinearLayout info_compare3;
    @ViewById
    TextView info_compare3_name_tv;
    @ViewById
    TextView info_compare3_price_tv;
    @ViewById
    ImageView info_country_iv;
    @ViewById
    TextView info_country_tv;
    @ViewById
    TextView info_link_tv;
    @ViewById
    TextView info_user_desc_tv;
    @ViewById
    LinearLayout info_product_img_ll;
    @ViewById
    TextView info_product_desc_tv;
    @ViewById
    LinearLayout view_net_error;
    @ViewById
    TextView net_error_reload_tv;

    PGlobalInfoBean.ContentBean mProductData;

    Picasso mPicasso;
    ProductImgAdapter mViewPagerAdapter;

    String productId;
    Dialog shareDialog;

    @AfterViews
    public void initView(){
        setSystemBarTit();
        setMainTitle("商品详情");
        showBackButton();
        showShareButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
                    Intent intent = new Intent(GlobalProductInfoActivity.this,LoginActivity_.class);
                    intent.putExtra("showback",true);
                    startActivity(intent);
                    return;
                }

                shareDialog = ShareDialog.showDialog(GlobalProductInfoActivity.this,GlobalProductInfoActivity.this);
            }
        });

        mPicasso = Picasso.with(this);

        productId = getIntent().getStringExtra("id");
        if(AppUtil.isNetworkAvailable(this)){
            requestProductData();
        }else{
            view_net_error.setVisibility(View.VISIBLE);
            net_error_reload_tv.setOnClickListener(this);
        }
    }

    private void showContentView(){
        if(mProductData.floor == null){
            ToastUtil.show("请求失败");
            return;
        }

        btm_bar.setVisibility(ViewPager.VISIBLE);
        info_content_view.setVisibility(ViewPager.VISIBLE);

        List<ImageView> bannerViewList = new ArrayList<ImageView>();
        for(PBuyUrlBean.BuyUrlBean urlBean : mProductData.floor.img){
            ImageView imageView = new ImageView(this);
            imageView.setTag(urlBean.url);
            bannerViewList.add(imageView);
        }

        mViewPagerAdapter = new ProductImgAdapter(this,bannerViewList,info_view_pager);
        info_view_pager.setAdapter(mViewPagerAdapter);
        initViewPagerIndicator();

        if(mProductData.floor.img!=null && !mProductData.floor.img.isEmpty()){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        shareBitmap = mPicasso.load(mProductData.floor.img.get(0).url).get();
                        handler.sendEmptyMessage(1);
                    } catch (IOException e) {
                    }
                }
            }.start();
        }

        info_product_title_tv.setText(mProductData.floor.title);
        info_global_price_tv.setText(mProductData.floor.price_title);

        if(!TextUtils.isEmpty(mProductData.floor.jd_price) || !TextUtils.isEmpty(mProductData.floor.weipin_price) || !TextUtils.isEmpty(mProductData.floor.kaola_price)){
            info_compare_container.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(mProductData.floor.jd_price)){
                info_compare1_name_tv.setText(mProductData.floor.jd_price);
                info_compare1.setVisibility(View.VISIBLE);
            }

            if(!TextUtils.isEmpty(mProductData.floor.weipin_price)){
                info_compare2_name_tv.setText(mProductData.floor.weipin_price);
                info_compare2.setVisibility(View.VISIBLE);
            }

            if(!TextUtils.isEmpty(mProductData.floor.kaola_price)){
                info_compare3_name_tv.setText(mProductData.floor.kaola_price);
                info_compare3.setVisibility(View.VISIBLE);
            }
        }
        mPicasso.load(mProductData.floor.country_logo).into(info_country_iv);
        info_country_tv.setText(mProductData.floor.country_name);
        info_link_tv.setText(mProductData.floor.business);
        info_user_desc_tv.setText(mProductData.floor.impression);
        info_product_desc_tv.setText(mProductData.floor.description);

        info_like2_tv.setSelected(mProductData.floor.is_liked == 1);
        info_like_ll.setOnClickListener(new GlobalLikeListener(this, info_like2_tv, mProductData.floor));

        info_product_bonus_iv.setOnClickListener(new ShareIndiListener(this,mHandler));
        info_buy_ib.setOnClickListener(new BonusListener(true,this,mProductData.floor.id));
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(shareBitmap == null)return;
            double aspectRatio = (double) shareBitmap.getHeight() / (double) shareBitmap.getWidth();
            int targetHeight = (int) (AppApplication.DISPLAY_WIDTH * aspectRatio);
            FrameLayout.LayoutParams pagerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,targetHeight);
            info_view_pager.setLayoutParams(pagerParams);
        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == 1){
                sendWebPage("限时价￥"+mProductData.floor.price_title+"，"+mProductData.floor.title,mProductData.floor.description, shareBitmap, mProductData.link,SendMessageToWX.Req.WXSceneTimeline);
            }else {
                sendWebPage("限时价￥"+mProductData.floor.price_title+"，"+mProductData.floor.title,mProductData.floor.description, shareBitmap, mProductData.link, SendMessageToWX.Req.WXSceneSession);
            }
        }
    };

    private void initViewPagerIndicator(){

        for (int k = 0; k < mProductData.floor.img.size(); k++) {

            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.index_radiobutton, null, false);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ScreenUtil.dipToPx(this,17), ScreenUtil.dipToPx(this,17));
            layoutParams.setMargins(0, 0, ScreenUtil.dipToPx(this, 10), 0);
            home_index_radiogroup.addView(radioButton, layoutParams);
        }
        RadioButton childAt = (RadioButton) home_index_radiogroup.getChildAt(0);
        if (childAt != null) {
            childAt.setChecked(true);
        }

        info_view_pager.addOnPageChangeListener(new ViewPagerIndicator(home_index_radiogroup));
    }

    public void requestProductData(){
        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(this,LoginActivity_.class);
            intent.putExtra("showback",true);
            startActivity(intent);
            finish();
            return;
        }
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("floor_id", productId);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.GLOBAL_PRODUCT_INFO,
                new Response.Listener<PGlobalInfoBean>() {
                    @Override
                    public void onResponse(PGlobalInfoBean response) {
                        view_net_error.setVisibility(View.GONE);
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
                        mProductData = response.data;
                        showContentView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                net_error_reload_tv.setVisibility(View.VISIBLE);
            }
        }, obj,PGlobalInfoBean.class);
        request.doRequest();
    }

    @Override
    public void reloadCurrentPage(){
        super.reloadCurrentPage();
        requestProductData();
    }

    @Override
    public void onClick(int position, Dialog dialog) {

        switch (position){
//            case ButtonType.SINA_WEIBO_POSITION:
//                sendWebPage(infoBean.share_h5_url,title,desc);
//                break;
            case ButtonType.WEIXIN_POSITION:
                sendWebPage("限时价￥"+mProductData.floor.price_title+"，"+mProductData.floor.title,mProductData.floor.description, shareBitmap, mProductData.link, SendMessageToWX.Req.WXSceneSession);
                break;
            case ButtonType.WGROUP_POSITON:
                sendWebPage("限时价￥"+mProductData.floor.price_title+"，"+mProductData.floor.title,mProductData.floor.description, shareBitmap, mProductData.link,SendMessageToWX.Req.WXSceneTimeline);
                break;
//            case ButtonType.QQ_WEIBO_POSITION:
//                break;
            case ButtonType.CANCEL_POSITION:
                dialog.dismiss();
                break;
        }
    }
 }
