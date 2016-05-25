package com.wuliwuli.haitao.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wuliwuli.haitao.R;
import com.wuliwuli.haitao.base.AppBaseFragment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mac-z on 16/3/3.
 */
public class WebFragment extends AppBaseFragment {

    WebView fragment_web_view;
    LinearLayout web_container;
    String webUrl = "";

    @Override
    public void onPause() {
        super.onPause();
        try {
            fragment_web_view.getClass().getMethod("onPause").invoke(fragment_web_view, (Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web,container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null)return;
        web_container = (LinearLayout) view.findViewById(R.id.web_container);
        fragment_web_view = (WebView) view.findViewById(R.id.fragment_web_view);
        initWebViewSettings();

        webUrl = getArguments().getString("url");
        if (TextUtils.isEmpty(webUrl)) {
            return;
        }
        mHandler.sendEmptyMessage(1);
        showBackButton();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            fragment_web_view.loadUrl(webUrl);

        }
    };


    private void initWebViewSettings(){
        WebSettings settings = fragment_web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

        fragment_web_view.setWebChromeClient(mWebChromeClient);
        fragment_web_view.setWebViewClient(mWebViewClient);

    }

    private WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setMainTitle(fragment_web_view.getTitle());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            fragment_web_view.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            fragment_web_view.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.startsWith("tel:")){
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return false;
        }


    };

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setMainTitle(title);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    };
}
