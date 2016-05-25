package com.wuliwuli.haitao.listener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.LoginActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseListener;
import com.wuliwuli.haitao.bean.CommentBean;
import com.wuliwuli.haitao.bean.PTopicBean;
import com.wuliwuli.haitao.bean.PWriteCommentBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac-z on 16/4/15.
 */
public class CommentListener extends AppBaseListener{

    TextView commentTv;
    ProductBean productBean;
    RecyclerView.Adapter mAdapter;

    public CommentListener(Context context, TextView comment, ProductBean bean, RecyclerView.Adapter adapter){
        super.mContext = context;
        this.productBean = bean;
        this.commentTv = comment;
        this.mAdapter = adapter;
    }

    @Override
    public void onClick(View v) {

        if(!WuliConfig.getInstance().getBooleanInfoFromLocal(WuliConfig.IS_LOGIN,false)){
            Intent intent = new Intent(mContext,LoginActivity_.class);
            intent.putExtra("showback",true);
            mContext.startActivity(intent);
            return;
        }
        showWriteCommentDialog();
    }

    public void sendWriteCommentData(final String comment){

        Map<String,String> obj = new HashMap<String,String>();
        obj.put("product_id", productBean.product_id);
        obj.put("comment",comment);
        obj.put("user_id", WuliConfig.getInstance().getStringInfoFromLocal(WuliConfig.USER_ID,""));


        NormalPostRequest request = new NormalPostRequest(UrlManager.USER_COMMENT,
                new Response.Listener<PWriteCommentBean>() {
                    @Override
                    public void onResponse(PWriteCommentBean response) {
                        if(response == null){
                            ToastUtil.show("解析失败");
                            return;
                        }

                        if(response.getResult() == 1){
                            commentTv.setText(String.valueOf(productBean.count_comment+1));
                            CommentBean commentBean = new  CommentBean();
                            commentBean.nick_name = response.data.comment.nick_name;
                            commentBean.content = response.data.comment.content;

                            productBean.comment.add(0,commentBean);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtil.show(response.getPrompt()+"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wuli", error.getMessage(), error);
            }
        }, obj,PWriteCommentBean.class);
        request.doRequest();
    }

    private void showWriteCommentDialog(){
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_write_comment,null);
        dialog.setContentView(view);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_write_edit);
        ImageView sendBtn = (ImageView) view.findViewById(R.id.dialog_write_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString())){
                    ToastUtil.show("评论不能为空");
                    return;
                }

                sendWriteCommentData(editText.getText().toString());
                dialog.dismiss();
            }
        });

        Window w = dialog.getWindow();
        WindowManager.LayoutParams params = w.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = AppApplication.DISPLAY_WIDTH;
        w.setAttributes(params);
        dialog.show();


    }
}
