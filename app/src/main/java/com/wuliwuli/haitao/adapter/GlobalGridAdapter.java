package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.GlobalProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.PGlobalBean;
import com.wuliwuli.haitao.transform.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/18.
 */
public class GlobalGridAdapter extends RecyclerView.Adapter{

    private List<PGlobalBean.GlobalBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;
    private boolean showFooter = true,isList = true;

    public GlobalGridAdapter(List<PGlobalBean.GlobalBean> data, int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<PGlobalBean.GlobalBean>();
        }
    }

    public void buildData(List<PGlobalBean.GlobalBean> data){
        if(data!=null){
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void changeAdapterType(boolean list){
        isList = list;
        notifyDataSetChanged();
    }

    public void hideFootView(){
        showFooter = false;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_golbal_grid,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pull_to_refresh_footer, null);
            view.setLayoutParams(lp);
            return new FooterViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            final PGlobalBean.GlobalBean bean = data.get(position);
            picasso.load(bean.cover_img).transform(new Transformation(mScreenWidth)).into(vHolder.picIv);
            vHolder.titleTv.setText(bean.title);
            vHolder.descriptionIv.setText(bean.price_title);
            vHolder.businessTv.setText(bean.business);
            vHolder.timeTv.setText(bean.created);
            vHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GlobalProductInfoActivity_.class);
                    intent.putExtra("id",bean.id);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount() && showFooter) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
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
        public TextView titleTv;
        public TextView descriptionIv;
        public TextView businessTv;
        public TextView timeTv;


        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            picIv = (ImageView) itemView.findViewById(R.id.item_global_list_iv);
            titleTv = (TextView) itemView.findViewById(R.id.item_global_list_name_tv);
            descriptionIv = (TextView) itemView.findViewById(R.id.item_global_list_1tv);
            businessTv = (TextView) itemView.findViewById(R.id.item_global_list_business_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_global_list_time_tv);
        }
    }
}
