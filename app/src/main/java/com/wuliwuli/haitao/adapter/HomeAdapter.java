package com.wuliwuli.haitao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.InfoBean;
import com.wuliwuli.haitao.listener.HomePicListener;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-z on 16/4/13.
 */
public class HomeAdapter extends RecyclerView.Adapter{

    private List<InfoBean.ContentBean> data;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;
    private boolean showFooter = true;

    public HomeAdapter(List<InfoBean.ContentBean> data,int width){
        this.data = data;
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<InfoBean.ContentBean>();
        }
    }

    public void buildData(List<InfoBean.ContentBean> data){
        if(data!=null)
            this.data.addAll(data);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_home,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pull_to_refresh_footer, null);
            view.setLayoutParams(lp);
            if(!showFooter){
                view.setVisibility(View.GONE);
            }
            return new FooterViewHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            InfoBean.ContentBean bean = data.get(position);
            picasso.load(bean.cover_img).config(Bitmap.Config.RGB_565).transform(new com.wuliwuli.haitao.transform.Transformation(mScreenWidth)).placeholder(R.drawable.icon_list_site).into(vHolder.picIv);
            vHolder.picIv.setOnClickListener(new HomePicListener(mContext,bean.id,bean.type,-1));

            if(bean.type == 1){//专题
                vHolder.likeTv.setText(bean.liked_total);
                vHolder.gallery.setVisibility(View.GONE);
                vHolder.btmRl.setVisibility(View.GONE);
                vHolder.centerInfoLl.setVisibility(View.VISIBLE);
                vHolder.likeContainerLl.setVisibility(View.VISIBLE);
                vHolder.centerTitleTv.setText(bean.title);
                vHolder.centerNameTv.setText(bean.uname);
                if(!TextUtils.isEmpty(bean.face))
                    picasso.load(bean.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(vHolder.centerFaceIv);
                else{
                    vHolder.centerFaceIv.setImageBitmap(AppUtil.initFace(mContext));
                }
                vHolder.gallery.setAdapter(null);
            }else if(bean.type == 2) {
                vHolder.likeTv.setText(bean.liked_total);
                vHolder.gallery.setVisibility(View.VISIBLE);
                vHolder.btmRl.setVisibility(View.VISIBLE);
                vHolder.centerInfoLl.setVisibility(View.GONE);
                vHolder.likeContainerLl.setVisibility(View.VISIBLE);
                vHolder.nameTv.setText(bean.uname);
                vHolder.descTv.setText(bean.description);
                if(!TextUtils.isEmpty(bean.face))
                    picasso.load(bean.face).transform(new CircleTransform()).placeholder(R.drawable.icon_face_site).into(vHolder.faceIv);
                else{
                    vHolder.faceIv.setImageBitmap(AppUtil.initFace(mContext));
                }
                if(bean.product!=null && !bean.product.isEmpty()){
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
                    vHolder.gallery.setLayoutManager(layoutManager);
                    vHolder.gallery.setAdapter(new SubHomeAdapter(bean.product,mScreenWidth/3,bean.id));
                }
            }else if(bean.type == 3){
                //超级返
                vHolder.gallery.setVisibility(View.GONE);
                vHolder.btmRl.setVisibility(View.GONE);
                vHolder.centerInfoLl.setVisibility(View.GONE);
                vHolder.likeContainerLl.setVisibility(View.GONE);
            }
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

        public RecyclerView gallery;
        public ImageView picIv;
        public TextView likeTv;
        public ImageView faceIv;
        public TextView nameTv;
        public TextView descTv;

        public RelativeLayout btmRl;
        public LinearLayout centerInfoLl;
        public TextView centerNameTv;
        public TextView centerTitleTv;
        public ImageView centerFaceIv;
        public LinearLayout likeContainerLl;

        public ViewHolder(View itemView) {
            super(itemView);

            picIv = (ImageView) itemView.findViewById(R.id.item_pic_iv);
            likeTv = (TextView) itemView.findViewById(R.id.item_liked_tv);
            nameTv = (TextView) itemView.findViewById(R.id.item_uname_tv);
            faceIv = (ImageView) itemView.findViewById(R.id.item_uface_iv);
            descTv = (TextView) itemView.findViewById(R.id.item_desc_tv);
            gallery = (RecyclerView) itemView.findViewById(R.id.item_product_gl);

            btmRl = (RelativeLayout) itemView.findViewById(R.id.item_btm_rl);
            centerInfoLl = (LinearLayout) itemView.findViewById(R.id.item_center_info_ll);
            centerNameTv = (TextView) itemView.findViewById(R.id.item_center_name_tv);
            centerTitleTv = (TextView) itemView.findViewById(R.id.item_center_title_tv);
            centerFaceIv = (ImageView) itemView.findViewById(R.id.item_center_face_iv);
            likeContainerLl = (LinearLayout) itemView.findViewById(R.id.item_like_container_ll);
        }
    }
}
