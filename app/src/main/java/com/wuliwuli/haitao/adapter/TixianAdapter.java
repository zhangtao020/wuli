package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.TixianActivity;
import com.wuliwuli.haitao.TixianJlActivity;
import com.wuliwuli.haitao.bean.PMyBonusBean;
import com.wuliwuli.haitao.bean.PRecordBean;
import com.wuliwuli.haitao.listener.ViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/18.
 */
public class TixianAdapter extends RecyclerView.Adapter{

    private PRecordBean.ContentBean data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;
    private final int TYPE_HEADER = 113;


    public TixianAdapter(int width){
        this.mScreenWidth = width;
    }

    public void buildData(PRecordBean.ContentBean data){
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.header_tixian,null);
            view.setLayoutParams(lp);
            return  new HeaderViewHolder(view);
        }else if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tixian,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder && data!=null){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.moneyTv.setText("已提现￥"+data.sum);
        }else if(holder instanceof ViewHolder && data!=null){
            ViewHolder vHolder = (ViewHolder) holder;

            PRecordBean.RecordBean bean = data.record.get(position-1);

            vHolder.moneyTv.setText("申请提现￥"+bean.amount);
            vHolder.timeTv.setText(bean.created);
            vHolder.statusTv.setText(bean.user_type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if(data == null)return  -1;
        if(data.record==null || data.record.isEmpty())return 1;
        return data.record.size()+1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView moneyTv ;

        public HeaderViewHolder(View headerView) {
            super(headerView);

            moneyTv = (TextView) headerView.findViewById(R.id.tixian_money_tv);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View  rootView;
        public TextView moneyTv;
        public TextView timeTv;
        public TextView statusTv;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            moneyTv = (TextView) itemView.findViewById(R.id.item_tixian_money_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_tixian_time_tv);
            statusTv = (TextView) itemView.findViewById(R.id.item_tixian_status_tv);
        }
    }
}
