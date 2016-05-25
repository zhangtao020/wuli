package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.listener.BonusListener;
import com.wuliwuli.haitao.listener.HomePicListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class SubHomeAdapter extends RecyclerView.Adapter{

    private List<ProductBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;
    private String id;

    public SubHomeAdapter(List<ProductBean> data, int width,String id){
        this.data = data;
        this.mScreenWidth = width;
        this.id = id;
        if(this.data == null){
            this.data = new ArrayList<ProductBean>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sub_home,null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mScreenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        picasso = Picasso.with(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vHolder = (ViewHolder) holder;
        ProductBean bean = data.get(position);
        picasso.load(bean.cover_img).config(Bitmap.Config.RGB_565).placeholder(R.drawable.icon_list_site).into(vHolder.picIv);
        vHolder.priceTv.setText(bean.origin_price);
        vHolder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        vHolder.discountTv.setText(bean.price);
        vHolder.titleTv.setText(bean.name);
        vHolder.redIv.setVisibility(bean.is_bonus == 1 ? View.VISIBLE : View.GONE);

        vHolder.redIv.setOnClickListener(new BonusListener(false,mContext));
        vHolder.rootView.setOnClickListener(new HomePicListener(mContext,id,2,position));
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View rootView;
        public ImageView picIv;
        public TextView titleTv;
        public TextView priceTv;
        public ImageView redIv;
        public TextView discountTv;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.sub_root_view);
            picIv = (ImageView) itemView.findViewById(R.id.item_sub_pic_iv);
            redIv = (ImageView) itemView.findViewById(R.id.item_sub_red_iv);
            titleTv = (TextView) itemView.findViewById(R.id.item_sub_title_tv);
            priceTv = (TextView) itemView.findViewById(R.id.item_sub_old_price_tv);
            discountTv = (TextView) itemView.findViewById(R.id.item_sub_discount_price_tv);
        }
    }

}
