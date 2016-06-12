package com.wuliwuli.haitao;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.fb.FeedbackAgent;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.transform.CircleTransform;
import com.wuliwuli.haitao.util.AppUtil;
import com.wuliwuli.haitao.util.ScreenUtil;
import com.wuliwuli.haitao.util.WuliConfig;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Created by mac-z on 16/4/27.
 */
@EActivity
public class SettingActivity extends AppBaseActivity {

    Handler handler = new Handler();
    Picasso mPicasso = null;

    @ViewById
    ImageView red_user_face_iv;
    @ViewById
    TextView red_user_name_tv;
    @ViewById
    TextView setting_version_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_set);
        setSystemBarTit();
        setMainTitle("设置");
        showBackButton();

        mPicasso = Picasso.with(this);
        String face = getIntent().getStringExtra("face");
        if(!TextUtils.isEmpty(face))
            mPicasso.load(face).transform(new CircleTransform()).into(red_user_face_iv);
        else{
            red_user_face_iv.setImageBitmap(AppUtil.initFace(this));
        }
        red_user_name_tv.setText(getIntent().getStringExtra("name"));

        setting_version_tv.setText("版本号："+AppUtil.getVersionName());
    }

    @Click(R.id.setting_about_tv)
    public void clickByAbout(){
        Intent intent = new Intent(this,H5Activity.class);
        intent.putExtra("type","about");
        startActivity(intent);
    }

    @Click(R.id.setting_feedback_tv)
    public void clickByFeedback(){
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.startFeedbackActivity();
    }

    @Click(R.id.setting_clear_tv)
    public void clickByClear(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                showClearDialog();
            }
        },500);
    }

    @Click(R.id.setting_push_tv)
    public void clickByPush(){

    }

    @Click(R.id.setting_exit_tv)
    public void clickByExit(){
        showExitLoginDialog();
    }

    public void showExitLoginDialog(){
       final Dialog rejectDialog = new Dialog(this,R.style.Dialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_login,null);

        rejectDialog.setContentView(contentView);
        Window window = rejectDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ScreenUtil.getScreenWidth(this);
        window.setAttributes(params);
        TextView cancelTv = (TextView) contentView.findViewById(R.id.exit_login_cancel_tv);
        TextView sureTv = (TextView) contentView.findViewById(R.id.exit_login_tv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDialog.dismiss();
            }
        });
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WuliConfig.getInstance().saveBooleanInfoToLocal(WuliConfig.IS_LOGIN,false);
                WuliConfig.getInstance().saveStringInfoToLocal(WuliConfig.USER_ID,"");

                finishAllActivity();

                Intent intent = new Intent(SettingActivity.this,LoginActivity_.class);
                startActivity(intent);


            }
        });
        rejectDialog.show();

    }

    public void showClearDialog(){
        if(this.isFinishing()) return;
        final Dialog rejectDialog = new Dialog(this,R.style.Dialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_clear,null);
        rejectDialog.setContentView(contentView);
        rejectDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SettingActivity.this.isFinishing()) return;
                rejectDialog.dismiss();
            }
        },1500);
    }

}
