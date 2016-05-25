package com.wuliwuli.haitao.listener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.base.AppBaseListener;

/**
 * Created by mac-z on 16/5/10.
 */
public class BonusListener extends AppBaseListener {
    boolean hide;
    boolean isShow = false;
    String productId;
    Handler handler = new Handler();

    public BonusListener(boolean autoHide, Context context){
        this.hide = autoHide;
        this.mContext = context;
    }

    public BonusListener(boolean autoHide, Context context,String productId){
        this.hide = autoHide;
        this.mContext = context;
        this.productId = productId;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (isShow) return;
        showClearDialog();
    }

    public void showClearDialog(){
        isShow = true;
        final Dialog rejectDialog = new Dialog(mContext, R.style.TrunDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_bonus,null);
        ImageView closeIv = (ImageView) contentView.findViewById(R.id.dialog_bonus_close_iv);

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
        rejectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShow = false;
            }
        });
        if(hide){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rejectDialog.dismiss();

                    new BuyProductListener(mContext, productId).onClick(null);
                }
            },4000);
        }
    }
}
