package com.wuliwuli.haitao.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.H5Activity;
import com.wuliwuli.haitao.MyCommentActivity;
import com.wuliwuli.haitao.MyLikeActivity;
import com.wuliwuli.haitao.MyOrderActivity;
import com.wuliwuli.haitao.MyRedbaoActivity;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.SettingActivity_;
import com.wuliwuli.haitao.base.AppBaseFragment;
import com.wuliwuli.haitao.bean.InfoBean;
import com.wuliwuli.haitao.bean.PPersonBean;
import com.wuliwuli.haitao.bean.PProductBean;
import com.wuliwuli.haitao.http.MCAppRequest;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/13.
 */
@EFragment
public class RedFragment extends AppBaseFragment{

    @ViewById
    ImageView red_user_face_iv;
    @ViewById
    TextView red_user_name_tv;
    @ViewById
    TextView red_user_id_tv;
    @ViewById
    TextView redbao_money_tv;

    PPersonBean.ContentBean dataBean;
    Picasso mPicasso = null;
    @AfterViews
    public void onViewCreate(){
        setMainTitle("我的红包");
        mPicasso = Picasso.with(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redbao,null,false);
    }

    @Click(R.id.red_user_top_iv)
    public void clickByBonus(){
        Intent intent = new Intent(mActivity, MyRedbaoActivity.class);
        startActivity(intent);
    }

    @Click(R.id.redbao_like_tv)
    public void clickByLike(){
        Intent intent = new Intent(mActivity, MyLikeActivity.class);
        startActivity(intent);
    }

    @Click(R.id.redbao_order_tv)
    public void clickByOrder(){
        Intent intent = new Intent(mActivity, MyOrderActivity.class);
        startActivity(intent);
    }

    @Click(R.id.redbao_comment_tv)
    public void clickByComment(){
        Intent intent = new Intent(mActivity, MyCommentActivity.class);
        startActivity(intent);
    }

    @Click(R.id.redbao_fuli_tv)
    public void clickByFuli(){
        Intent intent = new Intent(mActivity,H5Activity.class);
        intent.putExtra("type","fuli");
        startActivity(intent);
    }

    @Click(R.id.redbao_setting_tv)
    public void clickBySetting(){
        Intent intent = new Intent(mActivity, SettingActivity_.class);
        intent.putExtra("face",dataBean.face);
        intent.putExtra("name",dataBean.nick_name);
        startActivity(intent);
    }

    @Click(R.id.redbao_question_tv)
    public void clickByQuestion(){
        Intent intent = new Intent(mActivity,H5Activity.class);
        intent.putExtra("type","question");
        startActivity(intent);
    }

    private void showViewData(){
        if(dataBean == null)return;
        if(!TextUtils.isEmpty(dataBean.face))
            mPicasso.load(dataBean.face).transform(new CircleTransform()).into(red_user_face_iv);
        else{
            red_user_face_iv.setImageBitmap(AppUtil.initFace(mActivity));
        }
        red_user_name_tv.setText(dataBean.nick_name);
        red_user_id_tv.setText("ID:"+dataBean.id);
        redbao_money_tv.setText(dataBean.sum_bonus);
    }

    public void requestPersonData(){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));

        NormalPostRequest request = new NormalPostRequest(UrlManager.MY_INDEX,
                new Response.Listener<PPersonBean>() {
                    @Override
                    public void onResponse(PPersonBean response) {
                        if(!parseHeadObject(response))return;
                        if(response.data == null) return;
                        dataBean = response.data;
                        showViewData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,PPersonBean.class);
        request.doRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPersonData();
    }
}
