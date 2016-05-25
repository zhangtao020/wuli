package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.PSpecialBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class SpecialAdapter extends RecyclerView.Adapter{

    private List<PSpecialBean.TopicBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;
    private final int TYPE_HEADER = 113;

    public SpecialAdapter(List<PSpecialBean.TopicBean> data, int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<PSpecialBean.TopicBean>();
        }
    }

    public void buildData(List<PSpecialBean.TopicBean> data){
        if(data!=null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        if(viewType == TYPE_ITEM){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_special,null);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
//        }else if(viewType == TYPE_HEADER){
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_special, null);
//            view.setLayoutParams(lp);
//            return new HeaderViewHolder(view);
//        }
//
//        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ViewHolder vHolder = (ViewHolder) holder;
            PSpecialBean.TopicBean bean = data.get(position);
            picasso.load(bean.cover_img).config(Bitmap.Config.RGB_565).transform(new com.wuliwuli.haitao.transform.Transformation(mScreenWidth)).placeholder(R.drawable.icon_list_site).into(vHolder.picIv);

//            vHolder.picIv.setOnClickListener(new HomePicListener(mContext,bean,-1));

            if(bean.product!=null && !bean.product.isEmpty()){
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
                vHolder.gallery.setLayoutManager(layoutManager);
                vHolder.gallery.setAdapter(new TopicAdapter(bean.product,mScreenWidth,false));
            }
    }

//    @Override
//    public int getItemViewType(int position) {
//        // 最后一个item设置为footerView
//        if(position == 0){
//            return TYPE_HEADER;
//        }
//        else if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        }
//        else {
//            return TYPE_ITEM;
//        }
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

        public RecyclerView gallery;
        public ImageView picIv;

        public ViewHolder(View itemView) {
            super(itemView);

            picIv = (ImageView) itemView.findViewById(R.id.item_special_pic_iv);
            gallery = (RecyclerView) itemView.findViewById(R.id.item_special_product_lv);
        }
    }


}
