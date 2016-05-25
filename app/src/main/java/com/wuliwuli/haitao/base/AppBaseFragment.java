package com.wuliwuli.haitao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.bean.BaseResult;
import com.wuliwuli.haitao.util.ToastUtil;

/**
 * Created by mac-z on 16/2/19.
 */
public class AppBaseFragment extends Fragment {

    public Activity mActivity;
    public RelativeLayout titleBar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



//    @Override
//    @Deprecated
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if(savedInstanceState != null)return new View(inflater.getContext());
//        if(titleBar != null && needNewView()){
//            titleBar.removeFromHistory();
//        }
//        if (mView == null || needNewView()) {
//            View view = getView(inflater, container, savedInstanceState);
//            if(needTitle()){
//                ViewGroup group = (ViewGroup)inflater.inflate(R.layout.titlebar_activity, null);
//                group.addView(view, new ViewGroup.LayoutParams(-1, -1));
//                mView = group;
//            }else{
//                mView = view;
//            }
//        } else {
//            if (mView.getParent() != null) {
//                ((ViewGroup) mView.getParent()).removeView(mView);
//            }
//        }
//        if(mView != null){
//            titleBar = (TitleBar)mView.findViewById(com.miaoche.utilities.R.id.titleBar);
//            return mView;
//        }
//
//        View view1 =  super.onCreateView(inflater, container, savedInstanceState);
//        titleBar = (TitleBar)view1.findViewById(com.miaoche.utilities.R.id.titleBar);
//        return view1;
//    }
//
//    public boolean needNewView(){
//        return true;
//    }
//
//    protected boolean needTitle(){
//        return false;
//    }
//
//    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        return null;
//    }
//
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null)return;
        if(titleBar == null){
            titleBar = (RelativeLayout) view.findViewById(R.id.title_bar);
        }
    }
//
//    public void onView(View view, Bundle savedInstanceState){
//
//    }
//
    protected void setMainTitle(String title){
        if(titleBar != null){
            ((TextView)titleBar.findViewById(R.id.title_tv)).setText(title);
        }
    }

    public void showBackButton(){
        if(titleBar != null){
            titleBar.findViewById(R.id.title_back_btn).setVisibility(View.VISIBLE);
            titleBar.findViewById(R.id.title_back_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }
//
//    public void showBackButton(View.OnClickListener listener){
//        if(titleBar != null){
//            titleBar.showBackButton(listener);
//        }
//    }
//
//    public void showCancelButton(View.OnClickListener listener){
//        if(titleBar != null){
//            titleBar.showCancelButton(listener);
//        }
//    }
//
//    public void showSecondTitle(){
//        if(titleBar != null){
//            titleBar.showSecondTitle();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(titleBar != null){
//            titleBar.removeFromHistory();
//        }
//    }
//
//    public int getCurrentStatusBarColor() {
//        return -1;
//    }
//
//    public void newMessage(int num){};
//
//
    public RelativeLayout getTitleBar() {
        return titleBar;
    }
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
//            if(getActivity() == null){
//                ToastUtil.show("登录失效，请重新登录");
//                return false;
//            }
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            intent.putExtra("repeat",true);
//            startActivity(intent);
//            getActivity().finish();
//            return false;
//        }

        if(object.getResult()!=1){
            ToastUtil.show(object.getPrompt());
            return false;
        }

        return true;
    }
//
//    LoadingDialog mLoadingDialog = null;
//    public void showLoadingDialog(boolean cancelEnable){
//        if(mLoadingDialog == null){
//            mLoadingDialog = new LoadingDialog(getActivity());
//        }
//        mLoadingDialog.setCancelable(cancelEnable);
//        if(!mLoadingDialog.isShowing())mLoadingDialog.show();
//    }
//
//    public void showLoadingDialog(boolean cancelEnable,String message){
//        if(mLoadingDialog == null){
//            FragmentActivity activity = getActivity();
//            if (activity != null) {
//                mLoadingDialog = new LoadingDialog(activity, message);
//            }
//        }
//        mLoadingDialog.setCancelable(cancelEnable);
//        if(!mLoadingDialog.isShowing())mLoadingDialog.show();
//    }
//
//    public void hideLoadingDialog(){
//        if(getActivity()!=null && mLoadingDialog!=null && mLoadingDialog.isShowing()){
//            mLoadingDialog.dismiss();
//        }
//    }

}