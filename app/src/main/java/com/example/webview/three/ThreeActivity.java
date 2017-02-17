package com.example.webview.three;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.webview.R;

/**
 * 互联网用：webView.loadUrl("http://www.google.com");
 * 本地文件用：webView.loadUrl("file:///android_asset/XX.html");  //本地文件存放在：assets文件中
 */

/**
 * ScrollView嵌套WebView
 */
public class ThreeActivity extends Activity {
    private WebView mWebView;
    private ProgressBar pb_loading;

    private WebSettings mWebSettings;
    private WebChromeClient wcc;

    private String URL="https://www.baidu.com";
    private String WEB_USERAGENT=";qnd-Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        mWebView= (WebView) this.findViewById(R.id.webView);
        pb_loading= (ProgressBar) this.findViewById(R.id.pb_loading);

        initWebView();
    }


    private void initWebView() {
        mWebSettings=mWebView.getSettings();

        String ua = mWebSettings.getUserAgentString();

        mWebSettings.setUserAgentString(ua + WEB_USERAGENT);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowContentAccess(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);//设置WebView可触摸放大缩小
        mWebSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setAppCacheEnabled(true);// 开启H5(APPCache)缓存功能。webView数据缓存分为两种：AppCache和DOM Storage（Web Storage）。
        mWebSettings.setDomStorageEnabled(true);// 开启 DOM storage 功能
        mWebSettings.setDatabaseEnabled(true);// 应用可以有数据库
        //自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //解决与ScrollView嵌套时的滑动冲突
                ((WebView) v).requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        /**
         * handler.proceed();表示等待证书响应
         * handler.cancel();表示挂起连接，为默认方式
         * handler.handleMessage(null);可做其他处理
         */
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                //调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                    return true;
                }

                Log.i("URL", url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isFinishing())
                    return;

                if (pb_loading != null) {
                    pb_loading.setVisibility(View.GONE);
                }
            }

        });

        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.loadUrl(URL);

        wcc = new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (isFinishing())
                    return;

                if (pb_loading != null) {
                    pb_loading.setProgress(progress);
                    if (progress > 95) {
                        pb_loading.setVisibility(View.GONE);
                    }
                }

            }
        };

        mWebView.setWebChromeClient(wcc);
    }


    /**
     * 如果用webview点链接看了很多页以后，如果不做任何处理，点击系统“Back”键，整个浏览器会调用finish()而结束自身，如果希望浏览的网页回退而不是退出浏览器，需要在当前Activity中处理并消费掉该Back事件。
     *
     * 设置回退，覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//goBack()表示返回webView的上一页面

                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
