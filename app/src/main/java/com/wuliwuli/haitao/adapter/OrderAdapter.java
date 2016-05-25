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
import com.wuliwuli.haitao.bean.POrderBean;
import com.wuliwuli.haitao.bean.ProductBean;
import com.wuliwuli.haitao.listener.ViewClickListener;
import com.wuliwuli.haitao.transform.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/18.
 */
public class OrderAdapter extends RecyclerView.Adapter{

    private List<POrderBean.ContentBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;

    public OrderAdapter(List<POrderBean.ContentBean> data, int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<POrderBean.ContentBean>();
        }
    }

    public void buildData(List<POrderBean.ContentBean> data){
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order,null);
        view.setLayoutParams(lp);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            POrderBean.ContentBean bean = data.get(position);

            picasso.load(bean.cover_img).into(vHolder.picIv);
            vHolder.nameTv.setText(bean.business);
            vHolder.timeTv.setText(bean.order_time);
            vHolder.priceTv.setText(bean.order_amount);
            vHolder.numberTv.setText("订单编号："+bean.order_sn);

            //0 未确认，已确认，-1已失效
            if(bean.status == 0){
                vHolder.statusTv.setText("未确认");
                vHolder.statusTv.setTextColor(Color.parseColor("#ff3842"));
                vHolder.statusIv.setBackgroundColor(Color.parseColor("#ff3842"));
            }else if(bean.status == 1){
                vHolder.statusTv.setText("已确认");
                vHolder.statusTv.setTextColor(Color.parseColor("#7ed321"));
                vHolder.statusIv.setBackgroundColor(Color.parseColor("#7ed321"));
            }else{
                vHolder.statusTv.setText("已失效");
                vHolder.statusTv.setTextColor(Color.parseColor("#9b9b9b"));
                vHolder.statusIv.setBackgroundColor(Color.parseColor("#9b9b9b"));
            }

            vHolder.rootView.setOnClickListener(new ViewClickListener(mContext,bean.product_id, ProductInfoActivity_.class));
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
        public TextView priceTv;
        public ImageView statusIv;
        public TextView nameTv;
        public TextView timeTv;
        public TextView statusTv;
        public TextView numberTv;


        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            picIv = (ImageView) itemView.findViewById(R.id.item_order_product_iv);
            nameTv = (TextView) itemView.findViewById(R.id.item_order_name_tv);
            statusIv = (ImageView) itemView.findViewById(R.id.item_order_status_iv);
            priceTv = (TextView) itemView.findViewById(R.id.item_order_price_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_order_time_tv);
            statusTv = (TextView) itemView.findViewById(R.id.item_order_status_tv);
            numberTv = (TextView) itemView.findViewById(R.id.item_order_id_tv);
        }
    }
}
