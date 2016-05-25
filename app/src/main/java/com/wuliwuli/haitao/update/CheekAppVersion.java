package com.wuliwuli.haitao.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wuliwuli.haitao.base.AppBaseActivity;
import com.wuliwuli.haitao.bean.AppInfoBean;
import com.wuliwuli.haitao.http.NormalPostRequest;
import com.wuliwuli.haitao.http.UrlManager;

import java.util.HashMap;


public class CheekAppVersion implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
	public static final String IGNORE_VERSION_CODE = "ignore_version_code";
	private Context sContext;
	private Context lastContxt;
	private AlertDialog sDialog;
	private AppInfoBean sAppInfo;
	private String sDes;
	private CheckComplete sCheckComplete;
	private boolean sDisplayDialog;
	private String sUrl;
	private HashMap<String,String> sRequestBody;
	private boolean sHaveNewVersion = false;
	private int sLogo;
	private static CheekAppVersion cheekAppVersion;
	private DownloadApp down;

	
	public CheekAppVersion(Context context, boolean dialog, String url, HashMap<String,String> requestBody){
		sContext = context;
		sDisplayDialog = dialog;
		this.sUrl = url;
		this.sRequestBody = requestBody;
		if(dialog)
			check();
	}

	public  void repeatSetContext(Context context){
		sContext = context;
	}

	public void check(){
//		if(down != null && (down.isInDownload() || down.isComplete())){
//			if(down.isComplete()){
//				haveNewVersion();
//				showUpdate();
//			}
//			return;
//		}

		//
		NormalPostRequest request = new NormalPostRequest(UrlManager.CHECK_UPDATE,
				new Response.Listener<AppInfoBean>() {
					@Override
					public void onResponse(AppInfoBean response) {
						sAppInfo = response;
						if(response != null && response.data!=null && response.data.status >=1){
							sHaveNewVersion = true;
							haveNewVersion();
						}

						showUpdate();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("", error.getMessage(), error);
			}
		}, sRequestBody, AppInfoBean.class);
		request.doRequest();
	}
	
	private void haveNewVersion(){
		if(sCheckComplete != null && sHaveNewVersion)
			sCheckComplete.haveNewVersion(sHaveNewVersion);
	}
	
	private void showUpdate(){
		if(sDisplayDialog){
			showUpdateDialog();
		}
	}

	/**
	 * 手动检测更新
	 * @return 是否更新
	 */
	public boolean isShowUpdate(){
		return sDisplayDialog;
	}

	/**
	 * 2c app 手动检测更新调用
	 * @return
	 */
	public boolean isHaveNewVersion() {
		return sHaveNewVersion;
	}


	
	public void showUpdateDialog(){
		boolean compar = (sContext instanceof Activity && ((Activity)sContext).isFinishing());
		if(sContext == null || sContext.getTheme() == null || compar){
			return;
		}
		String desc = "此版本为最新版本！";
		if(sHaveNewVersion){
			desc = sAppInfo.data.content;
			if (desc == null) {
				desc = "";
			}
		}
		if(sDialog != null && sDialog.isShowing() && lastContxt != sContext){
			sDialog.dismiss();
		}

		if(sDialog == null || lastContxt != sContext ){
			AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
			builder.setTitle("提示").setCancelable(false).setPositiveButton("确定", this);
			if (sAppInfo.data.status == 0) {
				builder.setNegativeButton("取消", this);
				builder.setOnCancelListener(this);
				builder.setCancelable(true);
			}
			sDialog = builder.create();
			sDialog.setCanceledOnTouchOutside(false);
			lastContxt = sContext;
		}

		sDialog.setMessage(Html.fromHtml(desc));

		if(!sDialog.isShowing())
			sDialog.show();

	}

	public void setLogo(int logo){
		sLogo = logo;
	}



	@Override
	public void onClick(DialogInterface dialogInterface, int i) {
		if(sHaveNewVersion && i == DialogInterface.BUTTON_POSITIVE && !TextUtils.isEmpty(sAppInfo.data.url)){
			if(down != null && down.isComplete()) {
				down.downLoadComplete(false);
			}else{
				down = new DownloadApp(sContext, "WuliWuli全球购", sAppInfo.data.url, sLogo,updateListener);
				down.setInstall(true);
				down.toDownload();
				// 有更新时,点击确定
				if (updateListener != null) {
					updateListener.onSureClickForUpdate(sAppInfo.data.status);
				}
			}
		}else if(i == DialogInterface.BUTTON_NEGATIVE){
			onCancel(null);
		}
	}

	@Override
	public void onCancel(DialogInterface dialogInterface) {
		if(sAppInfo.data.status > 0){
			AppBaseActivity.finishAllActivity();
		}
	}

	public static interface CheckComplete{
		void haveNewVersion(boolean newVersion);
	}


	public void setCheckComplete(CheckComplete sCheckComplete) {
		this.sCheckComplete = sCheckComplete;
	}


	public static CheekAppVersion showNewAppVersion(Context context, String url, HashMap<String,String> body, int sLogo){
		return showNewAppVersion(context, url, body, sLogo, false);
	}

	public static CheekAppVersion showNewAppVersion(Context context, String url, HashMap<String,String> body, int sLogo, final boolean currentActivity){
		if(cheekAppVersion == null) {
			cheekAppVersion = new CheekAppVersion(context, false, url, body);
			cheekAppVersion.setCheckComplete(new CheckComplete() {
				@Override
				public void haveNewVersion(boolean newVersion) {

					if (newVersion) {
						cheekAppVersion.showUpdateDialog();
					}
				}
			});
			cheekAppVersion.setLogo(sLogo);
		}else{
			cheekAppVersion.repeatSetContext(context);
		}
		cheekAppVersion.check();
		return cheekAppVersion;
	}

	/**
	 * 软件更新接口回调
	 */
	UpdateListener updateListener;
	public void setUpdateListener(UpdateListener updateListener) {
		this.updateListener = updateListener;
	}
	
}
