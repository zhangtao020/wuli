package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.CommentBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.listener.BonusListener;
import com.wuliwuli.haitao.listener.BuyProductListener;
import com.wuliwuli.haitao.listener.CommentListener;
import com.wuliwuli.haitao.listener.LikeListener;
import com.wuliwuli.haitao.listener.TopicCommentListener;
import com.wuliwuli.haitao.listener.ViewClickListener;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.view.LinearLayoutForListView;

import java.util.List;

/**
 * Created by mac-z on 16/4/14.
 */
public class TopicAdapter extends RecyclerView.Adapter {

    List<ProductBean> data;
    Context mContext;
    Picasso picasso = null;
    int mScreenWidth;
    boolean showComment = true;

    public TopicAdapter(List<ProductBean> data,int width){
        this.data = data;
        this.mScreenWidth = width;
    }

    public TopicAdapter(List<ProductBean> data,int width,boolean comment){
        this.data = data;
        this.mScreenWidth = width;
        this.showComment = comment;
    }

    public void buildData(List<ProductBean> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_topic,null);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {

        ViewHolder holder = (ViewHolder) h;
        final ProductBean dataBean = data.get(position);
        if(showComment){
            if(!TextUtils.isEmpty(dataBean.face))
                picasso.load(dataBean.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(holder.faceIv);
            else{
                holder.faceIv.setImageBitmap(AppUtil.initFace(mContext));
            }

            holder.nameTv.setText(dataBean.uname);
            holder.descTv.setText(dataBean.description);
            holder.productTitleTv.setText(dataBean.name);
            holder.commentTv.setText(String.valueOf(dataBean.count_comment));
            holder.likeTv.setSelected(dataBean.is_liked == 1);
            holder.likeTv.setText(String.valueOf(dataBean.liked_total));
            holder.likeTv.setOnClickListener(new LikeListener(mContext, holder.likeTv, dataBean));
            holder.commentTv.setOnClickListener(new CommentListener(mContext,holder.commentTv,dataBean,this));
            holder.buyTv.setOnClickListener(new BonusListener(true,mContext,dataBean.product_id));
            holder.likecmtRl.setVisibility(View.VISIBLE);
        }
        picasso.load(dataBean.cover_img).config(Bitmap.Config.RGB_565).placeholder(R.drawable.icon_face_site).into(holder.productIv);
        picasso.load(dataBean.country_logo).into(holder.countryIv);
        holder.countryTv.setText(dataBean.country_name);
        holder.oldPriceTv.setText(dataBean.origin_price);
        holder.oldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discountTv.setText(dataBean.price);
        holder.redbaoIv.setVisibility(dataBean.is_bonus == 1 ? View.VISIBLE : View.GONE);
        holder.redbaoIv.setOnClickListener(new BonusListener(false,mContext));

        if(showComment && dataBean.comment!=null && !dataBean.comment.isEmpty()){
            holder.showMoreCommentIv.setVisibility(dataBean.comment.size() < 4 ? View.GONE : View.VISIBLE);
            holder.commentLv.removeAllViews();
            if (dataBean.isExpand){
                holder.commentLv.setAdapter(new TopicCommentAdapter(mContext, dataBean.comment));
            }else{
                List<CommentBean> temp = dataBean.comment;
                if(dataBean.comment.size()>3) {
                    temp = dataBean.comment.subList(0,3);
                }
                holder.commentLv.setAdapter(new TopicCommentAdapter(mContext, temp));
            }
            holder.showMoreCommentIv.setTag(R.string.key_tag,dataBean.product_id);
            holder.showMoreCommentIv.setOnClickListener(new TopicCommentListener(mContext,holder.showMoreCommentIv,holder.commentLv,dataBean));
        }else{
            holder.showMoreCommentIv.setVisibility(View.GONE);
        }

        holder.rootView.setOnClickListener(new ViewClickListener(mContext,dataBean.product_id, ProductInfoActivity_.class));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        ImageView faceIv;
        TextView nameTv;
        TextView descTv;
        ImageView productIv;
        TextView productTitleTv;
        ImageView countryIv;
        TextView countryTv;
        TextView oldPriceTv;
        TextView discountTv;
        ImageView redbaoIv;
        TextView buyTv;
        TextView commentTv;
        TextView likeTv;
        LinearLayoutForListView commentLv;
        ImageButton showMoreCommentIv;
        RelativeLayout likecmtRl;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            faceIv = (ImageView) view.findViewById(R.id.item_uface_iv);
            nameTv = (TextView) view.findViewById(R.id.item_uname_tv);
            descTv = (TextView) view.findViewById(R.id.item_desc_tv);
            productIv = (ImageView) view.findViewById(R.id.item_product_iv);
            productTitleTv = (TextView) view.findViewById(R.id.item_title_tv);
            countryIv = (ImageView) view.findViewById(R.id.item_country_iv);
            countryTv = (TextView) view.findViewById(R.id.item_country_tv);
            oldPriceTv = (TextView) view.findViewById(R.id.item_old_price_tv);
            discountTv = (TextView) view.findViewById(R.id.item_discount_price_tv);
            redbaoIv = (ImageView) view.findViewById(R.id.item_red_iv);
            buyTv = (TextView) view.findViewById(R.id.item_buy_tv);
            commentTv = (TextView) view.findViewById(R.id.item_comment_tv);
            likeTv = (TextView) view.findViewById(R.id.item_like_tv);
            commentLv = (LinearLayoutForListView) view.findViewById(R.id.item_comment_ll);
            showMoreCommentIv = (ImageButton) view.findViewById(R.id.item_comment_showmore_iv);
            likecmtRl = (RelativeLayout) view.findViewById(R.id.item_topic_cmt_rl);
        }
    }
}
