package com.wuliwuli.haitao.http;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wuliwuli.haitao.base.AppApplication;


public class HttpManager {
	private static HttpManager instance;
	private RequestQueue queue;
	public static HttpManager getInstance(){
		if(instance == null){
			instance = new HttpManager();
		}
		return instance;
	}

	private HttpManager(){
		queue = Volley.newRequestQueue(AppApplication.application);

	}

	public void doRequest(Request r){
		r.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,1.0f));
		queue.add(r);
	}
}
