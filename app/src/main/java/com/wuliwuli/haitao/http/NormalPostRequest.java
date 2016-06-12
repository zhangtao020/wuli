package com.wuliwuli.haitao.http;

import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.wuliwuli.haitao.util.JsonUtil;
import com.wuliwuli.haitao.util.ToastUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by mac-z on 16/4/14.
 */
public class NormalPostRequest<T> extends Request<T> {
    private Map<String, String> mMap;
    private Response.Listener<T> mListener;

    private Class<T> mClass;

    public NormalPostRequest(String url, Response.Listener<T> listener,Response.ErrorListener errorListener, Map<String, String> map,Class<T> tClass) {
        super(Request.Method.POST, url, errorListener);

        mListener = listener;
        mMap = map;
        mClass = tClass;

        Log.d("zt", url);
    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            Log.d("zt", jsonString);
            JSONObject obj = new JSONObject(jsonString);
            if(obj.getInt("result") != 1){
                Looper.prepare();
                ToastUtil.show(obj.getString("prompt"));
                Looper.loop();
                throw  new Exception("--"+obj.getString("prompt"));
            }

            return Response.success(JsonUtil.jsonObjToJavaUnException(jsonString, mClass), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        } catch (Exception e){
            return Response.error(new ParseError(e));
        }
    }
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    public void doRequest(){
        HttpManager.getInstance().doRequest(this);
    }
}
