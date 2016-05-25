package com.wuliwuli.haitao.base;

import android.content.Context;
import android.view.View;

import com.wuliwuli.haitao.bean.BaseResult;
import com.wuliwuli.haitao.util.ToastUtil;

/**
 * Created by mac-z on 16/4/15.
 */
public class AppBaseListener implements View.OnClickListener {

   public Context mContext;

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

    @Override
    public void onClick(View v) {

    }


}
