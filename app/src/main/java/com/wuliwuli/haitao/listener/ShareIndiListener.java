package com.wuliwuli.haitao.listener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseListener;

/**
 * Created by mac-z on 16/5/10.
 */
public class ShareIndiListener extends AppBaseListener {

    boolean isShow =false;
    Handler mHandler;
    public ShareIndiListener(Context context, Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Log.d("zt","onClick======="+isShow);
        if (isShow) return;
        showClearDialog();
    }

    public void showClearDialog(){
        isShow = true;
        final Dialog rejectDialog = new Dialog(mContext, R.style.TrunDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_share_indirector,null);
        ImageView closeIv = (ImageView) contentView.findViewById(R.id.dialog_bonus_close_iv);
        TextView groupTv = (TextView) contentView.findViewById(R.id.share_group_tv);
        TextView friendTv = (TextView) contentView.findViewById(R.id.share_wechat_tv);

        groupTv.setOnClickListener(new WechatListener(1));
        friendTv.setOnClickListener(new WechatListener(2));
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDialog.dismiss();
            }
        });
        rejectDialog.setContentView(contentView);
        rejectDialog.show();
        rejectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShow = false;
            }
        });
    }

    public class WechatListener implements View.OnClickListener{

        int type = 1;
        public WechatListener(int type){
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            mHandler.sendEmptyMessage(type);
        }
    }
}
