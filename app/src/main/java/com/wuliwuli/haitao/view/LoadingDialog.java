package com.wuliwuli.haitao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppApplication;


/**
 * Created by mac-z on 15/8/25.
 */
public class LoadingDialog extends Dialog {

    Context mContext;
    TextView messageTv;

    String message;
    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }


    public LoadingDialog(Context context, String message) {
        super(context);
        this.mContext = context;
        this.message = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);


        if(!TextUtils.isEmpty(message)){
            messageTv = (TextView) findViewById(R.id.message);
            messageTv.setText(message);
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (AppApplication.DISPLAY_WIDTH/2.6);
        params.height = (int) (AppApplication.DISPLAY_WIDTH/2.6);
        getWindow().setAttributes(params);

    }

}
