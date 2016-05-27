package com.wuliwuli.haitao.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.wuliwuli.haitao.MyRedbaoActivity;
import com.wuliwuli.haitao.ProductInfoActivity_;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.TixianActivity;
import com.wuliwuli.haitao.TixianJlActivity;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.bean.OpenBonusBean;
import com.wuliwuli.haitao.bean.PMyBonusBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;
import com.wuliwuli.haitao.listener.BuyProductListener;
import com.wuliwuli.haitao.listener.ViewClickListener;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac-z on 16/4/18.
 */
public class BonusAdapter extends RecyclerView.Adapter{

    private List<PMyBonusBean.BonusBean> data;
    private PMyBonusBean.CountBean headerData;
    private Picasso picasso = null;
    private int mScreenWidth;
    private Context mContext;

    private final int TYPE_ITEM = 111;
    private final int TYPE_FOOTER = 112;
    private final int TYPE_HEADER = 113;


    public BonusAdapter(int width){
        this.mScreenWidth = width;
        if(this.data == null){
            this.data = new ArrayList<PMyBonusBean.BonusBean>();
        }
    }

    public void buildData(List<PMyBonusBean.BonusBean> data,PMyBonusBean.CountBean headerData){
        if(data!=null){
            this.data.clear();
            this.data.addAll(data);
        }
        this.headerData = headerData;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        picasso = Picasso.with(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.header_myred,null);
            view.setLayoutParams(lp);
            return  new HeaderViewHolder(view);
        }else if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bonus,null);
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder && headerData!=null){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.priceTv.setText(headerData.kt);
            headerViewHolder.price2Tv.setText(headerData.dj);
            headerViewHolder.tixianTv.setOnClickListener(headerListener);
            headerViewHolder.jiluTv.setOnClickListener(headerListener);
        }else if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder) holder;
            final PMyBonusBean.BonusBean bean = data.get(position-1);

            picasso.load(bean.cover_img).into(vHolder.picIv);
            vHolder.titleTv.setText(bean.nick_name+" 购买了这个商品");
            vHolder.timeTv.setText(TextUtils.isEmpty(bean.order_time)?"":bean.order_time);
            vHolder.numberTv.setText("订单编号："+bean.order_sn);

            if(bean.is_open == 0){
                vHolder.bonusIv.setVisibility(View.VISIBLE);
                vHolder.bonusIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestBonus(bean);
                    }
                });
                vHolder.statusTv.setVisibility(View.GONE);
                vHolder.priceTv.setVisibility(View.GONE);
            }else{
                //0 为冻结中 1 可领取 2已经领取 -1失效
                if(bean.status == 0){
                    vHolder.statusTv.setText("冻结");
                    vHolder.priceTv.setText("￥"+bean.bonus_user);
                    vHolder.bonusIv.setVisibility(View.GONE);
                    vHolder.priceTv.setVisibility(View.VISIBLE);
                    vHolder.statusTv.setVisibility(View.VISIBLE);
                }else if(bean.status == 1){
                    vHolder.statusTv.setText("已确认");
                }else if(bean.status == 2){
                    vHolder.statusTv.setText("已完成");
                    vHolder.priceTv.setText("￥"+bean.bonus_user);
                    vHolder.bonusIv.setVisibility(View.GONE);
                    vHolder.priceTv.setVisibility(View.VISIBLE);
                    vHolder.statusTv.setVisibility(View.VISIBLE);
                }else{
                    vHolder.statusTv.setText("失效");
                    vHolder.bonusIv.setVisibility(View.GONE);
                    vHolder.priceTv.setVisibility(View.GONE);
                    vHolder.statusTv.setVisibility(View.VISIBLE);
                }
            }

            vHolder.rootView.setOnClickListener(new ViewClickListener(mContext,bean.product_id, ProductInfoActivity_.class));
        }
    }

    private View.OnClickListener headerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.red_tixian_tv){
                Intent intent = new Intent(mContext,TixianActivity.class);
                intent.putExtra("money", TextUtils.isEmpty(headerData.kt)?0:Integer.parseInt(headerData.kt));
                mContext.startActivity(intent);
            }else if(v.getId() == R.id.red_tixianjilu_tv){
                Intent intent = new Intent(mContext,TixianJlActivity.class);
                mContext.startActivity(intent);
            }
        }
    };

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
        return data==null?-1:data.size()+1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView priceTv ;
        TextView price2Tv ;
        TextView tixianTv ;
        TextView jiluTv ;

        public HeaderViewHolder(View headerView) {
            super(headerView);

            priceTv = (TextView) headerView.findViewById(R.id.red_price_tv);
            price2Tv = (TextView) headerView.findViewById(R.id.red_price1_tv);
            tixianTv = (TextView) headerView.findViewById(R.id.red_tixian_tv);
            jiluTv = (TextView) headerView.findViewById(R.id.red_tixianjilu_tv);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View  rootView;
        public ImageView picIv;
        public TextView titleTv;
        public TextView timeTv;
        public TextView statusTv;
        public TextView numberTv;
        public ImageView bonusIv;
        public TextView priceTv;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            picIv = (ImageView) itemView.findViewById(R.id.item_bonus_product_iv);
            titleTv = (TextView) itemView.findViewById(R.id.item_bonus_title_tv);
            bonusIv = (ImageView) itemView.findViewById(R.id.item_bonus_status_iv);
            priceTv = (TextView) itemView.findViewById(R.id.item_bonus_price_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_bonus_time_tv);
            statusTv = (TextView) itemView.findViewById(R.id.item_bonus_status_tv);
            numberTv = (TextView) itemView.findViewById(R.id.item_order_id_iv);
        }
    }

    public void requestBonus(final PMyBonusBean.BonusBean bean){
        Map<String,String> obj = new HashMap<String,String>();
        obj.put("id", bean.op_id);
        obj.put("status", String.valueOf(bean.status));

        NormalPostRequest request = new NormalPostRequest(UrlManager.OPEN_BONUS,
                new Response.Listener<OpenBonusBean>() {
                    @Override
                    public void onResponse(OpenBonusBean response) {
                        if(response==null){
                            ToastUtil.show("解析失败");
                            return;
                        }
                        showBonusDialog(bean.bonus_user);
                        bean.status = response.status;
                        bean.is_open = 1;
                        BonusAdapter.this.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        }, obj,OpenBonusBean.class);
        request.doRequest();
    }

    public void showBonusDialog(String money){
        final Dialog rejectDialog = new Dialog(mContext, R.style.Dialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_open_bonus,null);
        ImageView closeIv = (ImageView) contentView.findViewById(R.id.dialog_bonus_close_iv);
        TextView moneyTv = (TextView) contentView.findViewById(R.id.dialog_open_bonus_money_tv);
        moneyTv.setText(money);

        Window window = rejectDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = AppApplication.DISPLAY_WIDTH;
        params.height = AppApplication.DISPLAY_HEIGHT;
        window.setAttributes(params);

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDialog.dismiss();
            }
        });
        rejectDialog.setContentView(contentView);
        rejectDialog.show();
    }
}
