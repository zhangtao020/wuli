package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.wuliwuli.haitao.bean.PSuperBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.listener.BonusListener;
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
public class SuperAdapter extends RecyclerView.Adapter {

    PSuperBean.ContentBean contentBean;
    List<ProductBean> data;
    Context mContext;
    Picasso picasso = null;
    int mScreenWidth;
    String mSuperId;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;

    public SuperAdapter(int width,String superId){
        this.mScreenWidth = width;
        this.mSuperId = superId;
    }

    public void buildData(List<ProductBean> data,PSuperBean.ContentBean contentBean){
        this.data = data;
        this.contentBean = contentBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_super,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_super_foot, null);
            view.setLayoutParams(lp);
            return new FooterViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if(h instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) h;
            final ProductBean dataBean = data.get(position);
            picasso.load(dataBean.cover_img).config(Bitmap.Config.RGB_565).placeholder(R.drawable.icon_face_site).into(holder.productIv);
            picasso.load(dataBean.country_logo).into(holder.countryIv);
            holder.countryTv.setText(dataBean.country_name);
            holder.oldPriceTv.setText(dataBean.origin_price);
            holder.oldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discountTv.setText(dataBean.price);
            holder.superTv.setText("返"+contentBean.supermoney.supermoney+"元");
            holder.rootView.setOnClickListener(new ViewClickListener(mContext,dataBean.product_id,mSuperId, ProductInfoActivity_.class));
            if(!TextUtils.isEmpty(contentBean.supermoney.face))
                picasso.load(contentBean.supermoney.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(holder.faceIv);
            else{
                holder.faceIv.setImageBitmap(AppUtil.initFace(mContext));
            }

            holder.nameTv.setText(contentBean.supermoney.uname);
            holder.descTv.setText(dataBean.description);
            holder.productTitleTv.setText(dataBean.name);
        }else if(h instanceof FooterViewHolder){
            FooterViewHolder holder = (FooterViewHolder) h;
            holder.descTv.setText(Html.fromHtml(contentBean.supermoney.description));
        }

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
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
        TextView superTv;
        TextView buyTv;

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
            superTv = (TextView) view.findViewById(R.id.item_super_tv);
            buyTv = (TextView) view.findViewById(R.id.item_buy_tv);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView descTv;

        public FooterViewHolder(View view) {
            super(view);
            descTv = (TextView) view.findViewById(R.id.item_super_desc_tv);
        }
    }
}
