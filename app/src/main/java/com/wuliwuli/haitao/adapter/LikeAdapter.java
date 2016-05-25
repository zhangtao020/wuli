package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.InfoBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.listener.HomePicListener;
import com.wuliwuli.haitao.listener.ViewClickListener;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.transform.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/18.
 */
public class LikeAdapter extends RecyclerView.Adapter{

    private List<ProductBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;

    public LikeAdapter(List<ProductBean> data, int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<ProductBean>();
        }
    }

    public void buildData(List<ProductBean> data){
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

//        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_like,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
//        }else if(viewType == TYPE_FOOTER){
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pull_to_refresh_footer, null);
//            view.setLayoutParams(lp);
//            return new FooterViewHolder(view);
//        }
//
//        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            ProductBean bean = data.get(position);
            picasso.load(bean.cover_img).transform(new Transformation(mScreenWidth)).into(vHolder.picIv);
            picasso.load(bean.country_logo).into(vHolder.countryIv);
            vHolder.nameTv.setText(bean.name);
            vHolder.discountTv.setText(bean.price);
            vHolder.oldPriceTv.setText(bean.origin_price);
            vHolder.rootView.setOnClickListener(new ViewClickListener(mContext,bean.product_id, ProductInfoActivity_.class));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
            return TYPE_ITEM;
//        }
    }

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
        public TextView discountTv;
        public ImageView countryIv;
        public TextView nameTv;
        public TextView oldPriceTv;


        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            picIv = (ImageView) itemView.findViewById(R.id.item_like_product_iv);
            nameTv = (TextView) itemView.findViewById(R.id.item_like_name_tv);
            countryIv = (ImageView) itemView.findViewById(R.id.item_like_country_iv);
            discountTv = (TextView) itemView.findViewById(R.id.item_like_discount_price_tv);
            oldPriceTv = (TextView) itemView.findViewById(R.id.item_like_old_price_tv);

        }
    }
}
