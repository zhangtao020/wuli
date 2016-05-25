package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.PCommentBean;
import com.wuliwuli.haitao.bean.POrderBean;
import com.wuliwuli.haitao.listener.ViewClickListener;
import com.wuliwuli.haitao.transform.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/18.
 */
public class CommentAdapter extends RecyclerView.Adapter{

    private List<PCommentBean.ContentBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;

    public CommentAdapter(List<PCommentBean.ContentBean> data, int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<PCommentBean.ContentBean>();
        }
    }

    public void buildData(List<PCommentBean.ContentBean> data){
        if(data!=null){
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment,null);
        view.setLayoutParams(lp);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            PCommentBean.ContentBean bean = data.get(position);

            picasso.load(bean.cover_img).into(vHolder.picIv);
            picasso.load(bean.face).transform(new CircleTransform()).into(vHolder.faceIv);
            vHolder.nameTv.setText(bean.nick_name);
            vHolder.timeTv.setText(bean.created);
            vHolder.descTv.setText(bean.content);
            vHolder.productTv.setText(bean.product_name);

//            vHolder.rootView.setOnClickListener(new ViewClickListener(mContext,bean.product_id, ProductInfoActivity_.class));
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        // 最后一个item设置为footerView
////        if (position + 1 == getItemCount()) {
////            return TYPE_FOOTER;
////        } else {
//            return TYPE_ITEM;
////        }
//    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View  rootView;
        public ImageView picIv;
        public TextView descTv;
        public ImageView faceIv;
        public TextView nameTv;
        public TextView timeTv;
        public TextView productTv;


        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            picIv = (ImageView) itemView.findViewById(R.id.info_product_img_iv);
            nameTv = (TextView) itemView.findViewById(R.id.info_user_name_tv);
            faceIv = (ImageView) itemView.findViewById(R.id.info_user_face_iv);
            timeTv = (TextView) itemView.findViewById(R.id.info_user_time_tv);
            descTv = (TextView) itemView.findViewById(R.id.info_comment_tv);
            productTv = (TextView) itemView.findViewById(R.id.info_product_name_tv);
        }
    }
}
