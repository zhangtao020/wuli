package com.wuliwuli.haitao.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.BaseResult;
import com.wuliwuli.haitao.util.ToastUtil;
import com.wuliwuli.haitao.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class AppBaseActivity extends AppCompatActivity implements View.OnClickListener{

    public RelativeLayout titleBar;

    public static List<Activity> activityList = new ArrayList<Activity>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        activityList.add(this);
    }

    protected void setMainTitle(String title){
        if(titleBar == null){
            titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        }
        if(titleBar != null){
            ((TextView)titleBar.findViewById(R.id.title_tv)).setText(title);
        }
    }

    public static void finishAllActivity(){
        if(activityList!=null && !activityList.isEmpty()){
            for(Activity act : activityList){
                if(act!=null && !act.isFinishing()){
                    act.finish();
                }
            }
        }
    }

    public void showBackButton(){
        if(titleBar != null){
            titleBar.findViewById(R.id.title_back_btn).setVisibility(View.VISIBLE);
            titleBar.findViewById(R.id.title_back_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppBaseActivity.this.finish();
                }
            });
        }
    }

    public ImageButton showShareButton(){
        if(titleBar != null){
            titleBar.findViewById(R.id.title_menu_btn).setVisibility(View.VISIBLE);
            return (ImageButton) titleBar.findViewById(R.id.title_menu_btn);
        }
        return null;
    }

//
//    @Override
//    public void setContentView(View view) {
//        if(needTitle()){
//            ViewGroup viewGroup = (ViewGroup) LinearLayout.inflate(this, R.layout.titlebar_activity, null);
//            viewGroup.addView(view, new ViewGroup.LayoutParams(-1, -1));
//            super.setContentView(viewGroup);
//        }else {
//            super.setContentView(view);
//        }
//        titleBar = (TitleBar)findViewById(com.miaoche.utilities.R.id.titleBar);
//    }
//
//    public void setMainTitle(String s){
//        if(titleBar != null){
//            titleBar.setMainTitle(s);
//        }
//    }
//
//    public void showSecondTitle(){
//        if(titleBar != null){
//            titleBar.showSecondTitle();
//        }
//    }
//
//    public View showBackButton(){
//        if(titleBar != null){
//           return titleBar.showBackButton();
//        }
//
//        return  null;
//    }
//
//    public TitleBar getTitleBar() {
//        return titleBar;
//    }
//
//    protected boolean needTitle(){
//        return false;
//    }
//
//    @Override
//    final protected boolean willFinish() {
//        super.willFinish();
//        Intent intent = new Intent(this, SplashActivity.class);
//        startActivity(intent);
//        return false;
//    }
//
    /**
     * 解析数据返回值
     * @param object
     * @return
     */
    public boolean parseHeadObject(BaseResult object){
        if(object == null){
            ToastUtil.show("解析失败");
            return false;
        }

//        if(object.getResult() == 10){
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.putExtra("repeat",true);
//            startActivity(intent);
//            finish();
//            return false;
//        }

        if(object.getResult()!=1){
            ToastUtil.show(object.getPrompt());
            return false;
        }

        return true;
    }
    LoadingDialog mLoadingDialog = null;
    public void showLoadingDialog(boolean cancelEnable){
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setCancelable(cancelEnable);
        if(!mLoadingDialog.isShowing())mLoadingDialog.show();
    }

    public void showLoadingDialog(boolean cancelEnable,String message){
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(this,message);
        }
        mLoadingDialog.setCancelable(cancelEnable);
        if(!mLoadingDialog.isShowing())mLoadingDialog.show();
    }

    public void hideLoadingDialog(){
        if(mLoadingDialog!=null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void setSystemBarTit(){
        setTranslucentStatus(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.bg_title_bar));
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.color_220b0b));
    }

    public void setSystemBarTitByColor(){
        setTranslucentStatus(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.net_error_reload_tv){
            reloadCurrentPage();
        }
    }

    public void reloadCurrentPage(){}


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
