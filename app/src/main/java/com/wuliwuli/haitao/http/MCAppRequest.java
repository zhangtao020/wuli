package com.wuliwuli.haitao.http;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonSyntaxException;
import com.wuliwuli.haitao.BuildConfig;
import com.wuliwuli.haitao.util.JsonUtil;

import java.io.UnsupportedEncodingException;


public class MCAppRequest<T> extends JsonRequest<T> {

    private Class<T> mClass;

    private boolean filter; //是否显示错误提示  默认false 显示

    public MCAppRequest(String url, String requestBody, Response.Listener<T> listener,Response.ErrorListener errorListener, Class<T> tClass) {

        this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener,errorListener,tClass, false);
    }

    public MCAppRequest(int method,String url, String requestBody, Response.Listener<T> listener,Response.ErrorListener errorListener, Class<T> tClass) {

        this(method, url, requestBody, listener,errorListener,tClass, false);
    }

    public MCAppRequest(int method, String url, String requestBody, Response.Listener<T> listener,Response.ErrorListener errorListener, Class<T> tClass, boolean filter) {
        super(method, url, requestBody, listener,errorListener);
        this.mClass = tClass;
        this.filter = filter;
//        if(!filter){
//            setDefaultErrorHandler(new DefaultErrorHandler() {
//                @Override
//                public void onError(VolleyError error) {
//                    lastError = ErrorHandler.onError(lastError, error);
//                }
//            });
//        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            if(BuildConfig.DEBUG)
                Log.d("Response", jsonString);

//            if(deliverPreHandler != null){
//                deliverPreHandler.preHandler(jsonString);
//            }

            return Response.success(JsonUtil.jsonObjToJavaUnException(jsonString, mClass), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        } catch (Exception e){
            return Response.error(new ParseError(e));
        }
    }

    public void doRequest(){
//        complete = false;
//        setmRequestBody(handleBody(getRequestBody()));
        HttpManager.getInstance().doRequest(this);
    }

//    public String handleBody(String orinan) {
//        try {
//            JSONObject jsonObject = null;
//            if(TextUtils.isEmpty(orinan)){
//                jsonObject = new JSONObject();
//            }else {
//                jsonObject = new JSONObject(orinan);
//            }
//
//            String session = MiaocheConfig.getInstance().getStringInfoFromLocal(MiaocheConfig.SESSION_KEY,"");
//            if(!TextUtils.isEmpty(session)){
//                jsonObject.put("session_key", session);
//            }
//
//            if(!jsonObject.has("api_version")){
//                jsonObject.put("api_version", AppUtil.getVersionName());
//            }
//
//            return jsonObject.toString();
//        }catch (Exception e){
//
//        }
//        return orinan;
//    }
}
