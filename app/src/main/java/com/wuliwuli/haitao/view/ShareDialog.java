package com.wuliwuli.haitao.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.util.ButtonType;
import com.wuliwuli.haitao.util.ScreenUtil;


public class ShareDialog {


    private static LinearLayout initDialogLayout(Context context, int btnId, int background, int tvName, int layoutId, OnClickListener ls) {

        LinearLayout layout = new LinearLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(-2, -2);
        params1.gravity = Gravity.CENTER;

        ImageButton btn = new ImageButton(context);
        btn.setId(btnId);
        btn.setBackgroundResource(background);

        TextView textView = new TextView(context);
        textView.setText(tvName);
        textView.setTextColor(Color.BLACK);

        layout.setOrientation(1);
        layout.setLayoutParams(params);
        layout.addView(btn, params1);
        layout.addView(textView, params1);

        btn.setOnClickListener(ls);
        layout.setOnClickListener(ls);
        layout.setId(layoutId);
        return layout;

    }

    public static Dialog showDialog(Activity context, AlertViewClick viewClick) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout container = (LinearLayout)inflater.inflate(R.layout.alert_share, null);

        Dialog alertDialog = new Dialog(context, R.style.Dialog);
        alertDialog.setContentView(container);
        ButtonListener listener = new ButtonListener(viewClick, alertDialog);

        LinearLayout weixinChild = (LinearLayout) container.findViewById(R.id.weixin_layout);
        LinearLayout wgroupChild = (LinearLayout) container.findViewById(R.id.wgroup_layout);
//        LinearLayout qzoneChild = (LinearLayout) container.findViewById(R.id.qq_layout);
//        LinearLayout weiboChild = (LinearLayout) container.findViewById(R.id.weibo_layout);
        TextView cancelTv = (TextView) container.findViewById(R.id.cancel_tv);

        weixinChild.setOnClickListener(listener);
        wgroupChild.setOnClickListener(listener);
//        weiboChild.setOnClickListener(listener);
//        qzoneChild.setOnClickListener(listener);
        cancelTv.setOnClickListener(listener);

        Window w = alertDialog.getWindow();
        LayoutParams params = w.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ScreenUtil.getScreenWidth(context);
        w.setAttributes(params);

        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();
        return alertDialog;
    }

    private static class ButtonListener implements OnClickListener {

        AlertViewClick viewClick;
        Dialog dialog;

        public ButtonListener(AlertViewClick _click, Dialog _dialog) {
            viewClick = _click;
            dialog = _dialog;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
//            if (id == R.id.weibo_layout) {
//                viewClick.onClick(ButtonType.SINA_WEIBO_POSITION, dialog);
//            } else if (id == R.id.qq_layout) {
//                viewClick.onClick(ButtonType.QQ_WEIBO_POSITION, dialog);
//            } else

            if (id == R.id.weixin_layout) {
                viewClick.onClick(ButtonType.WEIXIN_POSITION, dialog);
            } else if (id == R.id.wgroup_layout) {
                viewClick.onClick(ButtonType.WGROUP_POSITON, dialog);
            } else if (id == R.id.cancel_tv){
                viewClick.onClick(ButtonType.CANCEL_POSITION, dialog);
            }
        }
    }

    public interface AlertViewClick {
        public void onClick(int position, Dialog dialog);
    }

}
